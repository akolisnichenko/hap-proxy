package com.kain.hap.proxy.state.setup;

import com.kain.hap.proxy.state.BehaviourStep;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.ErrorCode;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.Packet;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorStep implements BehaviourStep{

	@Override
	public Packet handle(StateContext context) {
		//TODO: add optional
		State nextState = State.toState((byte)(context.getIncome().getState().ordinal() + 1));
		if (nextState == null) {
			log.error("Step has not defined for this logic");
			//TODO: replace by custom error
			throw new RuntimeException("Not acceptable behaviour: step should be defined in map");
		}
		return Packet.builder().state(nextState).error(ErrorCode.NA).build();
	}

}
