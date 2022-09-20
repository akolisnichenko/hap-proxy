package com.kain.hap.proxy.state;

import com.kain.hap.proxy.tlv.State;

import lombok.Builder;
import lombok.Data;


//TODO: context will be replaced after tune protocol

@Builder
@Data
public class StateContext {
	private String deviceId;
	private State state;
}
