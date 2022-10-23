package com.kain.hap.proxy.tlv.integration;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import com.kain.hap.proxy.state.DeviceStateService;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.packet.HapRequest;
import com.kain.hap.proxy.tlv.packet.HapResponse;
import com.kain.hap.proxy.tlv.packet.Packet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResponseHandler implements GenericHandler<HapResponse> {
	@Value("${real.accessory.host:localhost}")
	private String realAccessoryHost;
	@Value("${real.accessory.port}")
	private int realAccessoryPort;

	private final DeviceStateService stateService;

	@Override
	public Object handle(HapResponse payload, MessageHeaders headers) {
		log.debug("Here is response: {}", payload.getCode());
		StateContext context = StateContext.builder()
				.income(payload.getBody())
				.endpoint(stateService.getCurrentState().getEndpoint())
				.build();
		if (payload.getBody().getError() != null) {
			log.error("Got HAP Error: {}", payload.getBody().getError());
			return null;
		}
		return Optional.ofNullable(stateService.onNext(context))
				.map(this::wrapToRequest)
				.orElse(null);
	}

	private HapRequest wrapToRequest(Packet body) {
		return HapRequest.builder()
				.body(body)
				.endpoint(stateService.getCurrentState().getEndpoint())
				.headers(Map.of("Host", realAccessoryHost + ":" + realAccessoryPort)).build();
	}
}