package com.kain.hap.proxy.admin.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kain.hap.proxy.tlv.Method;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.HapRequest;
import com.kain.hap.proxy.tlv.packet.Packet;

import lombok.RequiredArgsConstructor;

/**
 *  internal endpoint to monitor session, steps, and so on. And start communication process.
 *  Should be disabled after end of development  
 * 
 *
 */

@RestController
@RequiredArgsConstructor
public class AdminToolController {
	
	//TODO: move to correct place
	@Value("${real.accessory.host:localhost}")
	private String realAccessoryHost;
	@Value("${real.accessory.port}")
	private int realAccessoryPort;
	
	private final MessageChannel outcome;
	
	private static final String CRLF = "\r\n";

	
	@GetMapping("/hap/device/M1")
	@ResponseStatus(code = HttpStatus.OK)
	public void startDevicePacket() {
		HapRequest initialRequest = new HapRequest();
		initialRequest.setEndpoint("/pair-setup");
		initialRequest.addHeader("Host:" + realAccessoryHost + ":" + realAccessoryPort + CRLF);
		initialRequest.setBody(Packet.builder().state(State.M1).method(Method.PAIR_SETUP).build());
		outcome.send(MessageBuilder.withPayload(initialRequest).build());
	
	}
	

}
