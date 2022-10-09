package com.kain.hap.proxy.state;

import com.kain.hap.proxy.tlv.packet.Packet;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StateContext {
	private String endpoint;
	private String deviceId;
	private Packet income;
	
}
