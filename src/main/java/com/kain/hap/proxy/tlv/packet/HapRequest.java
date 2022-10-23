package com.kain.hap.proxy.tlv.packet;

import java.util.Map;

import com.google.common.collect.Maps;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HapRequest {
	private String method;
	private String endpoint;
	@Builder.Default
	private Map<String, String> headers = Maps.newHashMap();
	private Packet body;

	public void addHeader(String rawString) {
		String[] splittedHeader = rawString.split(":", 2);
		if (splittedHeader.length == 2) {
			headers.put(splittedHeader[0].trim(), splittedHeader[1].trim());
		}else {
			log.warn("not header value: {}", rawString);
		}
	}  
	
}
