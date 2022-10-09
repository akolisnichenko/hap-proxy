package com.kain.hap.proxy.tlv.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.stereotype.Component;

import com.kain.hap.proxy.tlv.packet.HapRequest;
import com.kain.hap.proxy.tlv.packet.Packet;

@Component
public class ResponseTransformer implements GenericTransformer<Packet, HapRequest>{
	@Value("${real.accessory.host:localhost}")
	private String realAccessoryHost;
	@Value("${real.accessory.port}")
	private int realAccessoryPort;
	
	private static final String CRLF = "\r\n";

	@Override
	public HapRequest transform(Packet source) {
		HapRequest request = new HapRequest();
		request.addHeader("Host:" + realAccessoryHost + ":" + realAccessoryPort + CRLF);
		request.setBody(source);
		//TODO: replace by value from packet
		request.setEndpoint("/pair-setup");
		
		return request;
	}

}
