package com.kain.hap.proxy.tlv.packet;

import com.kain.hap.proxy.tlv.ErrorCode;
import com.kain.hap.proxy.tlv.State;

import lombok.Getter;

@Getter
public class ErrorPacket extends BasePacket {
	
	private final ErrorCode error;

	public ErrorPacket(State state, ErrorCode error) {
		super(state);
		this.error = error;
	}

}
