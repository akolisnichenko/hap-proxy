package com.kain.hap.proxy.state;

import java.util.Map;

import com.kain.hap.proxy.state.setup.ErrorStep;
import com.kain.hap.proxy.state.setup.InitialStep;
import com.kain.hap.proxy.state.setup.SaltStep;
import com.kain.hap.proxy.tlv.State;

public class BehaviourMap {
	
	//private Map<String, ?> methodMap
	
	private Map<State, BehaviourState> nextSteps = Map.of(null, new InitialStep(),
			State.M1, new SaltStep());
	
	public BehaviourState getNextStep(State state) {
		return nextSteps.getOrDefault(state, new ErrorStep());
	}
	

}
