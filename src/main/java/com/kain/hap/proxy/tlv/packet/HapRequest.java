package com.kain.hap.proxy.tlv.packet;

import java.util.Map;

import com.google.common.collect.Maps;

import lombok.Data;

@Data
public class HapRequest {
	private String method;
	private String endpoint;
	private Map<String, String> headers = Maps.newHashMap();
	private BasePacket body;

	public void addHeader(String rawString) {
		String[] splittedHeader = rawString.split(":", 2);
		headers.put(splittedHeader[0].trim(), splittedHeader[1].trim());
	}  
	
}
