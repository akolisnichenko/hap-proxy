package com.kain.hap.proxy.state;

import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.BasePacket;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StateContext {
	private String endpoint;
	private String deviceId;
	private State state;
	private BasePacket income;
	
}
