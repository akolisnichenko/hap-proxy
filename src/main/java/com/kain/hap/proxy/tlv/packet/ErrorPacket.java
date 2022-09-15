package com.kain.hap.proxy.tlv.packet;

import com.kain.hap.proxy.tlv.State;

import lombok.Getter;

@Getter
public class ErrorPacket extends BasePacket {
	
	private final Error error;

	public ErrorPacket(State state, Error error) {
		super(state);
		this.error = error;
	}

}
