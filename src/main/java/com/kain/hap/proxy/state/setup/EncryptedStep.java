package com.kain.hap.proxy.state.setup;

import com.kain.hap.proxy.state.BehaviourState;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.Method;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.Packet;

public class EncryptedStep implements BehaviourState{

	@Override
	public Packet handle(StateContext context) {
		return Packet.builder().state(State.M5).method(Method.PAIR_SETUP).build();
	}
}
