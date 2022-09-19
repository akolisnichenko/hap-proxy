package com.kain.hap.proxy.state.setup;

import com.kain.hap.proxy.state.BehaviourState;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.ErrorCode;
import com.kain.hap.proxy.tlv.packet.BasePacket;
import com.kain.hap.proxy.tlv.packet.ErrorPacket;

public class ErrorStep implements BehaviourState{

	@Override
	public BasePacket handle(StateContext context) {
		return new ErrorPacket(context.getState(), ErrorCode.NA);
	}

}
