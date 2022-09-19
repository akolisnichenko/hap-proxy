package com.kain.hap.proxy.state;

import com.kain.hap.proxy.tlv.State;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class StateContext {
	private State state; 

}
