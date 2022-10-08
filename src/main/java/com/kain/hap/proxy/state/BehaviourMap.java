package com.kain.hap.proxy.state;

import java.util.Collections;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.kain.hap.proxy.service.SessionRegistrationService;
import com.kain.hap.proxy.state.setup.accessory.ExchangeResponseStep;
import com.kain.hap.proxy.state.setup.accessory.ProofStep;
import com.kain.hap.proxy.state.setup.accessory.SaltStep;
import com.kain.hap.proxy.state.setup.device.DeviceProofStep;
import com.kain.hap.proxy.state.setup.device.ExchangeRequestStep;
import com.kain.hap.proxy.state.setup.device.InitialStep;
import com.kain.hap.proxy.tlv.State;

@Component
public class BehaviourMap {

	@Autowired
	private SessionRegistrationService sessionService;
	
	private Map<String, Map<State, BehaviourState>> routingMap;
	
	//TODO: move to configuration file 
	@PostConstruct
	private void init() {
		Map<State, BehaviourState> setupSteps = Maps.newHashMap();

		// means to run response for state M1 do step with state inside it (M2)
		// accessory steps
		setupSteps.put(State.M1, new SaltStep(sessionService));
		setupSteps.put(State.M3, new ProofStep(sessionService));
		setupSteps.put(State.M5, new ExchangeResponseStep(sessionService)); 
				
		//device steps (client)
		setupSteps.put(null, new InitialStep(sessionService));
		setupSteps.put(State.M2, new DeviceProofStep(sessionService));
		setupSteps.put(State.M4, new ExchangeRequestStep(sessionService));
		
		Map<State, BehaviourState> verifySteps = Maps.newHashMap();
		
		routingMap = Maps.newHashMap();
		
		routingMap.put("/pair-setup", setupSteps);
		routingMap.put("/pair-verify", verifySteps);
		
	}
	
	public BehaviourState getNextStep(StateContext context) {
		return routingMap.getOrDefault(context.getEndpoint(), Collections.emptyMap())
				.get(context.getState());
	}
	

}
