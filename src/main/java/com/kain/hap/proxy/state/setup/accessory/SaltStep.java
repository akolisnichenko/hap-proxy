package com.kain.hap.proxy.state.setup.accessory;

import com.kain.hap.proxy.service.SessionRegistrationService;
import com.kain.hap.proxy.srp.AccessorySession;
import com.kain.hap.proxy.state.BehaviourState;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.Packet;
import com.kain.hap.proxy.tlv.type.PublicKey;
import com.kain.hap.proxy.tlv.type.Salt;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class SaltStep implements BehaviourState {
	private final SessionRegistrationService sessionService;

	@Override
	public Packet handle(StateContext context) {
		AccessorySession session = sessionService.registerSession(context.getDeviceId());
		return Packet.builder()
				.state(State.M2)
				.salt(new Salt(session.getSalt()))
				.key(new PublicKey(session.getPublicKey()))
				.build();
	}

}
