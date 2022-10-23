package com.kain.hap.proxy.state;

import java.util.Collections;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.kain.hap.proxy.service.AccessorySessionService;
import com.kain.hap.proxy.state.setup.accessory.ExchangeResponseStep;
import com.kain.hap.proxy.state.setup.accessory.ProofStep;
import com.kain.hap.proxy.state.setup.accessory.SaltStep;
import com.kain.hap.proxy.state.verify.accessory.VerifyStartRersponseStep;
import com.kain.hap.proxy.tlv.State;

@Component
public class AccessoryStepMap {

	@Autowired
	private AccessorySessionService sessionService;
	
	
	private Map<String, Map<State, AccessoryStep>> routingMap;
	
	//TODO: move to configuration file 
	@PostConstruct
	private void init() {
		Map<State, AccessoryStep> setupSteps = Maps.newHashMap();

		// means to run response for state M1 do step with state inside it (M2)
		// accessory steps
		setupSteps.put(State.M1, new SaltStep(sessionService));
		setupSteps.put(State.M3, new ProofStep(sessionService));
		setupSteps.put(State.M5, new ExchangeResponseStep(sessionService)); 

		Map<State, AccessoryStep> verifySteps = Maps.newHashMap();
		verifySteps.put(State.M1, new VerifyStartRersponseStep(sessionService));

		
		routingMap = Maps.newHashMap();
		
		routingMap.put("/pair-setup", setupSteps);
		routingMap.put("/pair-verify", verifySteps);
		
	}
	
	public BehaviourStep getNextStep(StateContext context) {
		return routingMap.getOrDefault(context.getEndpoint(), Collections.emptyMap())
				.get(context.getIncome().getState());
	}
	

}
