package com.kain.hap.proxy.state;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kain.hap.proxy.service.SessionRegistrationService;
import com.kain.hap.proxy.state.setup.DeviceProofStep;
import com.kain.hap.proxy.state.setup.ErrorStep;
import com.kain.hap.proxy.state.setup.ProofStep;
import com.kain.hap.proxy.state.setup.SaltStep;
import com.kain.hap.proxy.tlv.State;

@Component
public class BehaviourMap {

	@Autowired
	private SessionRegistrationService sessionService;
	
	
	private Map<State, BehaviourState> nextSteps;
	
	//TODO: move to configuration file 
	@PostConstruct
	private void init() {
		nextSteps = Map.of(//null, new InitialStep(),
				State.M1, new SaltStep(sessionService),
				State.M3, new ProofStep(sessionService),
				
				
				State.M2, new DeviceProofStep(sessionService));
	}
	
	public BehaviourState getNextStep(State state) {
		return nextSteps.getOrDefault(state, new ErrorStep());
	}
	

}
