package com.kain.hap.proxy.state.verify.device;

import com.kain.hap.proxy.service.DeviceSessionService;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.state.setup.device.BaseDeviceStep;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.Packet;
import com.kain.hap.proxy.tlv.type.PublicKey;

public class VerifyStartRequestStep extends BaseDeviceStep {

	public VerifyStartRequestStep(DeviceSessionService sessionService) {
		super(sessionService);
	}

	@Override
	public Packet handle(StateContext context) {
		return Packet.builder()
				.state(State.M1)
				.key(new PublicKey(getSessionService().getLTPK()))
				.build();
	}

}
