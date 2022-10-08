package com.kain.hap.proxy.tlv.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.stereotype.Component;

import com.kain.hap.proxy.tlv.packet.BasePacket;
import com.kain.hap.proxy.tlv.packet.HapRequest;

@Component
public class ResponseTransformer implements GenericTransformer<BasePacket, HapRequest>{
	@Value("${real.accessory.host:localhost}")
	private String realAccessoryHost;
	@Value("${real.accessory.port}")
	private int realAccessoryPort;
	
	private static final String CRLF = "\r\n";

	@Override
	public HapRequest transform(BasePacket source) {
		HapRequest request = new HapRequest();
		request.addHeader("Host:" + realAccessoryHost + ":" + realAccessoryPort + CRLF);
		request.setBody(source);
		return request;
	}

}
