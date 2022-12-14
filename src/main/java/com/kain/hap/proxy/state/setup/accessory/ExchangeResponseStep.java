package com.kain.hap.proxy.state.setup.accessory;

import java.security.GeneralSecurityException;
import java.util.UUID;

import com.google.common.primitives.Bytes;
import com.google.crypto.tink.subtle.Ed25519Sign;
import com.google.crypto.tink.subtle.Ed25519Verify;
import com.kain.hap.proxy.crypto.AEADResult;
import com.kain.hap.proxy.crypto.ChaCha20;
import com.kain.hap.proxy.crypto.HKDF;
import com.kain.hap.proxy.crypto.Hash;
import com.kain.hap.proxy.exception.MaxPeersException;
import com.kain.hap.proxy.service.SessionRegistrationService;
import com.kain.hap.proxy.srp.AccessorySession;
import com.kain.hap.proxy.state.BehaviourState;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.ErrorCode;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.BasePacket;
import com.kain.hap.proxy.tlv.packet.DataPacket;
import com.kain.hap.proxy.tlv.packet.EncryptedPacket;
import com.kain.hap.proxy.tlv.packet.ErrorPacket;
import com.kain.hap.proxy.tlv.serialize.TlvMapper;
import com.kain.hap.proxy.tlv.type.Encrypted;
import com.kain.hap.proxy.tlv.type.Identifier;
import com.kain.hap.proxy.tlv.type.PublicKey;
import com.kain.hap.proxy.tlv.type.Signature;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class ExchangeResponseStep implements BehaviourState {
	private final SessionRegistrationService sessionService;

	@Override
	public BasePacket handle(StateContext context) {
		AccessorySession session = sessionService.getSession(context.getDeviceId());
		HKDF hkdf = new HKDF(Hash::hmacSha512);
		hkdf.extract("Pair-Setup-Encrypt-Salt".getBytes(), session.getSharedSessionKey());
		byte[] inKey = hkdf.expand("Pair-Setup-Encrypt-Info".getBytes(), 32);
		EncryptedPacket encrypted = (EncryptedPacket) context.getIncome();

		byte[] data = encrypted.getData().getArray();
		try {
			byte[] res = ChaCha20.decode(inKey, "PS-Msg05".getBytes(), new AEADResult(data), new byte[0]);
			DataPacket dataPacket = (DataPacket) TlvMapper.INSTANCE.readPacket(res);

			byte[] iosDevicePairingId = dataPacket.getIdentifier().getId();
			byte[] iosDeviceLTPK = dataPacket.getKey().getKey(); // long-term public key
			byte[] iosDeviceSignature = dataPacket.getSignature().getValue();

			hkdf.extract("Pair-Setup-Controller-Sign-Salt".getBytes(), session.getSharedSessionKey());
			byte[] iOSDeviceX = hkdf.expand("Pair-Setup-Controller-Sign-Info".getBytes(), 32);

			byte[] iOSDeviceInfo = Bytes.concat(iOSDeviceX, iosDevicePairingId, iosDeviceLTPK);

			// TODO: rewrite by own implementation
			try {
				Ed25519Verify verify = new Ed25519Verify(iosDeviceLTPK);
				verify.verify(iosDeviceSignature, iOSDeviceInfo);
			} catch (GeneralSecurityException e) {
				return new ErrorPacket(State.M6, ErrorCode.AUTHENTICATION);
			}

			try {
				UUID iosDevicePairingUUID = UUID.fromString(new String(iosDevicePairingId));
				sessionService.registerDevicePairing(iosDevicePairingUUID, iosDeviceLTPK);
			}

			catch (MaxPeersException ex) {
				return new ErrorPacket(State.M6, ErrorCode.MAX_PEERS);
			}

			hkdf.extract("Pair-Setup-Accessory-Sign-Salt".getBytes(), session.getSharedSessionKey());
			byte[] outKey = hkdf.expand("Pair-Setup-Accessory-Sign-Info".getBytes(), 32);

			try {
				Ed25519Sign.KeyPair keyPair = Ed25519Sign.KeyPair.newKeyPair();
				byte[] accessoryId = session.getAccessoryIdentifier().getBytes();
				UUID sessionAccId = UUID.nameUUIDFromBytes(accessoryId);
				sessionService.registerAccessoryPairing(sessionAccId, keyPair);
				byte[] accessoryInfo = Bytes.concat(outKey, accessoryId, keyPair.getPublicKey());
				Ed25519Sign signer = new Ed25519Sign(keyPair.getPrivateKey());
				byte[] accessorySign = signer.sign(accessoryInfo);
				DataPacket outSubPacket = new DataPacket(new Identifier(accessoryId),
						new PublicKey(keyPair.getPublicKey()), new Signature(accessorySign));
				byte[] outData = TlvMapper.INSTANCE.writeValue(outSubPacket);
				AEADResult outResult = ChaCha20.aead(inKey, "PS-Msg06".getBytes(), outData, new byte[0]);
				return new EncryptedPacket(State.M6, new Encrypted(outResult.getConcatenated()));
			} catch (GeneralSecurityException e) {
				return new ErrorPacket(State.M6, ErrorCode.AUTHENTICATION);
			}

		} catch (Exception e) {
			log.error("Invalid exchange step", e);
			return new ErrorPacket(State.M6, ErrorCode.AUTHENTICATION);
		}
	}

}
