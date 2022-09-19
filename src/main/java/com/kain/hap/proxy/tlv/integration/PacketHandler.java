package com.kain.hap.proxy.tlv.integration;

import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import com.kain.hap.proxy.state.AccessoryStateService;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.packet.HapRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class PacketHandler implements GenericHandler<HapRequest>{
	private final AccessoryStateService stateService;

	@Override
	public Object handle(HapRequest payload, MessageHeaders headers) {
		log.debug("Here is packet: {}", payload);
		StateContext context = StateContext.builder()
				.state(payload.getBody().getState())
				.deviceId(headers.get("id").toString())
				.build(); 
		return stateService.onNext(context);

	}
}
