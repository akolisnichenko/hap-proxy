package com.kain.hap.proxy.state.setup;

import com.kain.hap.proxy.state.BehaviourState;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.ErrorCode;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.BasePacket;
import com.kain.hap.proxy.tlv.packet.ErrorPacket;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorStep implements BehaviourState{

	@Override
	public BasePacket handle(StateContext context) {
		State nextState = State.toState((byte)(context.getState().ordinal() + 1));
		if (nextState == null) {
			log.error("Step has not defined for this logic");
			throw new RuntimeException("Not acceptable behaviour: step should be defined in map");
		}
		return new ErrorPacket(nextState, ErrorCode.NA);
	}

}
