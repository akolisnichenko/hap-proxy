package com.kain.hap.proxy.state.setup.device;

import com.kain.hap.proxy.service.SessionRegistrationService;
import com.kain.hap.proxy.state.BehaviourState;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.Method;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.BasePacket;
import com.kain.hap.proxy.tlv.packet.MethodPacket;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InitialStep implements BehaviourState {
	private final SessionRegistrationService sessionService;
	
	@Override
	public BasePacket handle(StateContext context) {
		sessionService.registerDevice(context.getDeviceId());
		return new MethodPacket(State.M1, Method.PAIR_SETUP);
	}

}
