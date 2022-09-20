package com.kain.hap.proxy.state.setup;

import com.kain.hap.proxy.state.BehaviourState;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.Method;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.BasePacket;
import com.kain.hap.proxy.tlv.packet.MethodPacket;

public class DeviceProofStep implements BehaviourState{

	@Override
	public BasePacket handle(StateContext context) {
		return new MethodPacket(State.M3, Method.PAIR_SETUP);
	}

}
