package com.kain.hap.proxy.admin.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kain.hap.proxy.crypto.Curve25519Tool;
import com.kain.hap.proxy.crypto.KeyPair;
import com.kain.hap.proxy.tlv.Method;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.HapRequest;
import com.kain.hap.proxy.tlv.packet.Packet;
import com.kain.hap.proxy.tlv.type.PublicKey;

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

	
	@GetMapping("/hap/device/setup/M1")
	@ResponseStatus(code = HttpStatus.OK)
	public void startSetupPacket() {
		HapRequest initialRequest = new HapRequest();
		initialRequest.setEndpoint("/pair-setup");
		initialRequest.addHeader("Host:" + realAccessoryHost + ":" + realAccessoryPort + CRLF);
		initialRequest.setBody(Packet.builder().state(State.M1).method(Method.PAIR_SETUP).build());
		outcome.send(MessageBuilder.withPayload(initialRequest).build());
	
	}
	
	@GetMapping("/hap/device/verify/M1")
	@ResponseStatus(code = HttpStatus.OK)
	public void startVerifyPacket() {
		KeyPair pair = Curve25519Tool.generateKeys();
		Packet initial = Packet.builder()
				.state(State.M1)
				.key(new PublicKey(pair.getPubKey()))
				.build();
		
		HapRequest initialRequest = new HapRequest();
		initialRequest.setEndpoint("/pair-verify");
		initialRequest.addHeader("Host:" + realAccessoryHost + ":" + realAccessoryPort + CRLF);
		initialRequest.setBody(initial);
		outcome.send(MessageBuilder.withPayload(initialRequest).build());
	
	}
	

}
