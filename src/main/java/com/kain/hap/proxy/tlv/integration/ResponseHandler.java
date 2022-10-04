package com.kain.hap.proxy.tlv.integration;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import com.kain.hap.proxy.state.AccessoryStateService;
import com.kain.hap.proxy.state.StateContext;
import com.kain.hap.proxy.tlv.packet.ErrorPacket;
import com.kain.hap.proxy.tlv.packet.HapRequest;
import com.kain.hap.proxy.tlv.packet.HapResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResponseHandler implements GenericHandler<HapResponse>{
	//TODO: move to correct place
	@Value("${real.accessory.host:localhost}")
	private String realAccessoryHost;
	@Value("${real.accessory.port}")
	private int realAccessoryPort;
	
	private final AccessoryStateService stateService;
	
	private static final String CRLF = "\r\n";


	@Override
	public Object handle(HapResponse payload, MessageHeaders headers) {
		log.debug("Here is response: {}", payload.getCode());
		StateContext context = StateContext.builder()
				.state(payload.getBody().getState())
				.deviceId(Optional.ofNullable(headers.get("ip_connectionId"))
						.map(Object::toString)
						.orElse(null))
				.income(payload.getBody())
				.build(); 
		if (payload.getBody() instanceof ErrorPacket packet) {
			log.error("Got HAP Error: {}", packet.getError());
			return null;
		}
		
		
		HapRequest request = new HapRequest();
		//FIXME: move const header to another place
		request.addHeader("Host:" + realAccessoryHost + ":" + realAccessoryPort + CRLF);
		request.setBody(stateService.onNext(context));
		return request;
	}
}