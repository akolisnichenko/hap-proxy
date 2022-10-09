package com.kain.hap.proxy.state.verify.accessory;

import java.security.GeneralSecurityException;

import com.google.common.primitives.Bytes;
import com.google.crypto.tink.subtle.Ed25519Sign;
import com.kain.hap.proxy.crypto.AEADResult;
import com.kain.hap.proxy.crypto.ChaCha20;
import com.kain.hap.proxy.crypto.Curve25519Tool;
import com.kain.hap.proxy.crypto.HKDF;
import com.kain.hap.proxy.crypto.Hash;
import com.kain.hap.proxy.crypto.KeyPair;
import com.kain.hap.proxy.service.SessionRegistrationService;
import com.kain.hap.proxy.srp.AccessorySession;
import com.kain.hap.proxy.state.BehaviourState;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.ErrorCode;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.Packet;
import com.kain.hap.proxy.tlv.serialize.TlvMapper;
import com.kain.hap.proxy.tlv.type.Encrypted;
import com.kain.hap.proxy.tlv.type.Identifier;
import com.kain.hap.proxy.tlv.type.PublicKey;
import com.kain.hap.proxy.tlv.type.Signature;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class VerifyStartRersponseStep implements BehaviourState {
	private final SessionRegistrationService sessionService;

	@Override
	public Packet handle(StateContext context) {
		AccessorySession session = sessionService.getSession(context.getDeviceId());
		KeyPair pair = Curve25519Tool.generateKeys();
		byte[] externalPubkey = context.getIncome().getKey().getKey();

		byte[] sharedSecret = Curve25519Tool.generateShared(pair.getSecret(), externalPubkey);
		byte[] accessoryInfo = Bytes.concat(pair.getPubKey(), session.getAccessoryIdentifier().getBytes(),
				externalPubkey);

		byte[] ltsk = sessionService.getLongTermSecretKey(session.getAccessoryIdentifier());

		try {
			Ed25519Sign sign = new Ed25519Sign(ltsk);
			byte[] signature = sign.sign(accessoryInfo);

			Packet subTlv = Packet.builder().id(new Identifier(accessoryInfo)).signature(new Signature(signature))
					.build();
			byte[] outData = TlvMapper.INSTANCE.writeValue(subTlv);

			HKDF hkdf = new HKDF(Hash::hmacSha512);
			hkdf.extract("Pair-Verify-Encrypt-Salt".getBytes(), sharedSecret);
			byte[] sessionKey = hkdf.expand("Pair-Verify-Encrypt-Info".getBytes(), 32);
			AEADResult outResult = ChaCha20.aead(sessionKey, "PV-Msg02".getBytes(), outData, new byte[0]);
			
			return Packet.builder().state(State.M2).key(new PublicKey(pair.getPubKey()))
					.encrypted(new Encrypted(outResult.getConcatenated())).build();


		} catch (GeneralSecurityException e) {
			log.error("Ed25519 couldn't sign info", e);

		}
		return Packet.builder().state(State.M2).error(ErrorCode.AUTHENTICATION).build();
	}

}