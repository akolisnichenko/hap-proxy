package com.kain.hap.proxy.state.setup;

import com.kain.hap.proxy.service.DeviceSession;
import com.kain.hap.proxy.service.SessionRegistrationService;
import com.kain.hap.proxy.state.BehaviourState;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.BasePacket;
import com.kain.hap.proxy.tlv.packet.SaltPacket;
import com.kain.hap.proxy.tools.Salt;
import com.kain.hap.proxy.tools.SrpPublicKey;


public class SaltStep implements BehaviourState {
	private final SessionRegistrationService sessionService = SessionRegistrationService.INSTANCE;

	@Override
	public BasePacket handle(StateContext context) {
		DeviceSession session = sessionService.registerSession(context.getDeviceId());
		return new SaltPacket(State.M2, new SrpPublicKey(session.getPublicKey()), new Salt(session.getSalt()));
	}

}
