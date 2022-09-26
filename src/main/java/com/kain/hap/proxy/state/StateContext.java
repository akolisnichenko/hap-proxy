package com.kain.hap.proxy.state;

import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.BasePacket;

import lombok.Builder;
import lombok.Data;


//TODO: context will be replaced after tune protocol

@Builder
@Data
public class StateContext {
	private String deviceId;
	private State state;
	private BasePacket income;
	
}
