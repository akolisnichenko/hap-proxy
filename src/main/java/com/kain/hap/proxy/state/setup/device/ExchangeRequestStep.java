package com.kain.hap.proxy.state.setup.device;

import java.security.GeneralSecurityException;

import com.google.common.primitives.Bytes;
import com.google.crypto.tink.subtle.Ed25519Sign;
import com.kain.hap.proxy.crypto.AEADResult;
import com.kain.hap.proxy.crypto.ChaCha20;
import com.kain.hap.proxy.crypto.HKDF;
import com.kain.hap.proxy.crypto.Hash;
import com.kain.hap.proxy.service.DeviceSessionService;
import com.kain.hap.proxy.srp.Session;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.ErrorCode;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.Packet;
import com.kain.hap.proxy.tlv.serialize.TlvMapper;
import com.kain.hap.proxy.tlv.type.Encrypted;
import com.kain.hap.proxy.tlv.type.Identifier;
import com.kain.hap.proxy.tlv.type.PublicKey;
import com.kain.hap.proxy.tlv.type.Signature;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExchangeRequestStep extends BaseDeviceStep {

	public ExchangeRequestStep(DeviceSessionService sessionService) {
		super(sessionService);
	}

	@Override
	public Packet handle(StateContext context) {
		Session session = getSessionService().getSession();
		
		if (!session.verify(context.getIncome().getProof().getProof())) {
			return Packet.builder()
					.state(State.M5)
					.error(ErrorCode.AUTHENTICATION)
					.build();
		}else {
			getSessionService().generateKeyPair();
		}

		HKDF hkdf = new HKDF(Hash::hmacSha512);
		hkdf.extract("Pair-Setup-Controller-Sign-Salt".getBytes(), session.getSharedSessionKey());
		byte[] iOSDeviceX = hkdf.expand("Pair-Setup-Controller-Sign-Info".getBytes(), 32);
		byte[] deviceId = session.getIdentifier();
		// TODO: decide where to store long term keys 
		byte[] iOSDeviceInfo = Bytes.concat(iOSDeviceX, deviceId, getSessionService().getLTPK());

		try {
			Ed25519Sign sign = new Ed25519Sign(getSessionService().getLTSK());
			byte[] iOSDeviceSignature = sign.sign(iOSDeviceInfo);

			Packet subTlv = Packet.builder().id(new Identifier(deviceId))
					.key(new PublicKey(getSessionService().getLTPK()))
					.signature(new Signature(iOSDeviceSignature))
					.build();
			byte[] subTlvData = TlvMapper.writeValue(subTlv);
			
			hkdf.extract("Pair-Setup-Encrypt-Salt".getBytes(), session.getSharedSessionKey());
			byte[] encryptKey = hkdf.expand("Pair-Setup-Encrypt-Info".getBytes(), 32);
			AEADResult outResult = ChaCha20.aead(encryptKey, "PS-Msg05".getBytes(), subTlvData, new byte[0]);
			return Packet.builder().state(State.M5)
					.encrypted(new Encrypted(outResult.getConcatenated()))
					.build();

		} catch (GeneralSecurityException e) {
			log.error("Couldn't sign data", e);
		}
		return Packet.builder()
				.state(State.M5)
				.error(ErrorCode.UNAVAILABLE)
				.build();
	}
}
