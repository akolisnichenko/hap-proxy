package com.kain.hap.proxy.tlv.packet;

import com.kain.hap.proxy.tlv.Method;
import com.kain.hap.proxy.tlv.State;

import lombok.Getter;

@Getter
public class MethodPacket extends BasePacket{
	private final Method method;
	
	public MethodPacket(State state, Method method) {
		super(state);
		this.method = method;
	}

}
