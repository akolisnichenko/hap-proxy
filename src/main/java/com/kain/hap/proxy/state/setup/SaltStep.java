package com.kain.hap.proxy.state.setup;

import com.kain.hap.proxy.state.BehaviourState;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.BasePacket;
import com.kain.hap.proxy.tlv.packet.SaltPacket;

public class SaltStep implements BehaviourState {

	@Override
	public BasePacket handle(StateContext context) {
		return new SaltPacket(State.M2, null, null);
	}

}
