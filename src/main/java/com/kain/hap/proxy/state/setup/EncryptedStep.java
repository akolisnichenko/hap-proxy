package com.kain.hap.proxy.state.setup;

import com.kain.hap.proxy.state.BehaviourStep;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.Method;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.Packet;

public class EncryptedStep implements BehaviourStep{

	@Override
	public Packet handle(StateContext context) {
		return Packet.builder().state(State.M5).method(Method.PAIR_SETUP).build();
	}
}
