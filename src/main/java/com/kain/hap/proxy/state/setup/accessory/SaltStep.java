package com.kain.hap.proxy.state.setup.accessory;

import com.kain.hap.proxy.service.SessionRegistrationService;
import com.kain.hap.proxy.srp.AccessorySession;
import com.kain.hap.proxy.state.BehaviourState;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.BasePacket;
import com.kain.hap.proxy.tlv.packet.SaltPacket;
import com.kain.hap.proxy.tlv.type.Salt;
import com.kain.hap.proxy.tlv.type.PublicKey;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class SaltStep implements BehaviourState {
	private final SessionRegistrationService sessionService;

	@Override
	public BasePacket handle(StateContext context) {
		AccessorySession session = sessionService.registerSession(context.getDeviceId());
		return new SaltPacket(State.M2, new PublicKey(session.getPublicKey()), new Salt(session.getSalt()));
	}

}
