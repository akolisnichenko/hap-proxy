package com.kain.hap.proxy.state.setup.device;

import com.kain.hap.proxy.service.DeviceSessionService;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.Method;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.Packet;

public class InitialStep extends BaseDeviceStep {
	
	public InitialStep(DeviceSessionService sessionService) {
		super(sessionService);
	}

	@Override
	public Packet handle(StateContext context) {
		getSessionService().registerSession();
		return Packet.builder()
				.state(State.M1)
				.method(Method.PAIR_SETUP)
				.build();
	}

}
