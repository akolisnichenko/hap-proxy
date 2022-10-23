package com.kain.hap.proxy.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kain.hap.proxy.tlv.packet.HapResponse;
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
	@Autowired
	private final MessageChannel responseChannel;
	
	@GetMapping("/hap/device/setup/M1")
	@ResponseStatus(code = HttpStatus.OK)
	public void startSetupPacket() {
		
		//Fake response to initialise communication
		HapResponse response = new HapResponse();
		response.setBody(Packet.builder().state(null).build());
		responseChannel.send(MessageBuilder.withPayload(response).build());

	}
}
