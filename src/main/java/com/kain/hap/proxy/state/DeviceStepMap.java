package com.kain.hap.proxy.state;

import java.util.Collections;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.kain.hap.proxy.service.DeviceSessionService;
import com.kain.hap.proxy.state.setup.device.DeviceProofStep;
import com.kain.hap.proxy.state.setup.device.ExchangeRequestStep;
import com.kain.hap.proxy.state.setup.device.FinalSetupStep;
import com.kain.hap.proxy.state.setup.device.InitialStep;
import com.kain.hap.proxy.state.verify.device.VerifyStartRequestStep;
import com.kain.hap.proxy.tlv.State;

@Component
public class DeviceStepMap {

	@Autowired
	private DeviceSessionService sessionService;

	private Map<String, Map<State, DeviceStep>> routingMap;

	// TODO: move to configuration file
	@PostConstruct
	private void init() {
		Map<State, DeviceStep> setupSteps = Maps.newHashMap();

		setupSteps.put(null, new InitialStep(sessionService));
		setupSteps.put(State.M2, new DeviceProofStep(sessionService));
		setupSteps.put(State.M4, new ExchangeRequestStep(sessionService));
		setupSteps.put(State.M6, new FinalSetupStep(sessionService));

		Map<State, DeviceStep> verifySteps = Maps.newHashMap();
		verifySteps.put(null, new VerifyStartRequestStep(sessionService));

		routingMap = Maps.newHashMap();

		routingMap.put("/pair-setup", setupSteps);
		routingMap.put("/pair-verify", verifySteps);

	}

	public BehaviourStep getNextStep(StateContext context) {
		return routingMap.getOrDefault(context.getEndpoint(), Collections.emptyMap())
				.get(context.getIncome().getState());
	}

}
