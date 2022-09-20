package com.kain.hap.proxy.state;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.kain.hap.proxy.state.setup.ErrorStep;
import com.kain.hap.proxy.state.setup.ProofStep;
import com.kain.hap.proxy.state.setup.SaltStep;
import com.kain.hap.proxy.tlv.State;

@Component
public class BehaviourMap {
	
	//private Map<String, ?> methodMap
	
	private Map<State, BehaviourState> nextSteps = Map.of(//null, new InitialStep(),
			State.M1, new SaltStep(),
			State.M3, new ProofStep());
	//
	
	public BehaviourState getNextStep(State state) {
		return nextSteps.getOrDefault(state, new ErrorStep());
	}
	

}
