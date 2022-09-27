package com.kain.hap.proxy.state;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.kain.hap.proxy.service.SessionRegistrationService;
import com.kain.hap.proxy.state.setup.ErrorStep;
import com.kain.hap.proxy.state.setup.accessory.ProofStep;
import com.kain.hap.proxy.state.setup.accessory.SaltStep;
import com.kain.hap.proxy.state.setup.device.DeviceProofStep;
import com.kain.hap.proxy.state.setup.device.InitialStep;
import com.kain.hap.proxy.tlv.State;

@Component
public class BehaviourMap {

	@Autowired
	private SessionRegistrationService sessionService;
	
	
	private Map<State, BehaviourState> nextSteps;
	
	//TODO: move to configuration file 
	@PostConstruct
	private void init() {
		nextSteps = Maps.newHashMap();

		// means to run response for state M1 do step with state inside it (M2)
		// accessory steps
		nextSteps.put(State.M1, new SaltStep(sessionService));
		nextSteps.put(State.M3, new ProofStep(sessionService));
				
		//device steps (client)
		nextSteps.put(null, new InitialStep(sessionService));
		nextSteps.put(State.M2, new DeviceProofStep(sessionService));
	}
	
	public BehaviourState getNextStep(State state) {
		return nextSteps.getOrDefault(state, new ErrorStep());
	}
	

}
