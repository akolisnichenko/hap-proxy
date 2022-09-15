package com.kain.hap.proxy.tlv.packet;

import com.kain.hap.proxy.tlv.State;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class BasePacket {
	private final State state;
	

	public String toString() {
		return null;
	}
}
