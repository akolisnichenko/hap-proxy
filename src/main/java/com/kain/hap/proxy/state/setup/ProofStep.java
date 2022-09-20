package com.kain.hap.proxy.state.setup;

import com.kain.hap.proxy.service.DeviceSession;
import com.kain.hap.proxy.service.SessionRegistrationService;
import com.kain.hap.proxy.state.BehaviourState;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.BasePacket;
import com.kain.hap.proxy.tlv.packet.ProofPacket;
import com.kain.hap.proxy.tlv.type.Proof;

public class ProofStep implements BehaviourState {
	private final SessionRegistrationService sessionService = SessionRegistrationService.INSTANCE;

	@Override
	public BasePacket handle(StateContext context) {
		DeviceSession session = sessionService.getSession(context.getDeviceId());
		
		// here will be proof 
		return new ProofPacket(State.M4, new Proof(new byte[] {}));
	}
}
