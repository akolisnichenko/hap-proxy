package com.kain.hap.proxy.tlv.integration;

import java.util.Optional;

import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import com.kain.hap.proxy.state.AccessoryStateService;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.packet.HapResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResponseHandler implements GenericHandler<HapResponse>{
	private final AccessoryStateService stateService;
	//TODO: add logic for response to get endpoint 

	@Override
	public Object handle(HapResponse payload, MessageHeaders headers) {
		//TODO: remove transformation into separate class
		log.debug("Here is response: {}", payload.getCode());
		StateContext context = StateContext.builder()
				.endpoint("/pair-setup")
				.deviceId(Optional.ofNullable(headers.get("ip_connectionId"))
						.map(Object::toString)
						.orElse(null))
				.income(payload.getBody())
				.build(); 
		if (payload.getBody().getError() != null) {
			log.error("Got HAP Error: {}", payload.getBody().getError());
			return null;
		}
		return stateService.onNext(context);
	}
}