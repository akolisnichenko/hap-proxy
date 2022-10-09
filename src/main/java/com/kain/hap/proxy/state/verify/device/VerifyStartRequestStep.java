package com.kain.hap.proxy.state.verify.device;

import com.kain.hap.proxy.crypto.Curve25519Tool;
import com.kain.hap.proxy.crypto.KeyPair;
import com.kain.hap.proxy.service.SessionRegistrationService;
import com.kain.hap.proxy.state.BehaviourState;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.Packet;
import com.kain.hap.proxy.tlv.type.PublicKey;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VerifyStartRequestStep implements BehaviourState {
	private final SessionRegistrationService sessionService;
	
	@Override
	public Packet handle(StateContext context) {
		KeyPair pair = Curve25519Tool.generateKeys();
		return Packet.builder()
				.state(State.M1)
				.key(new PublicKey(pair.getPubKey()))
				.build();
	}

}
