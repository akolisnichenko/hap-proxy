package com.kain.hap.proxy.tlv;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum State {
	M1((byte)0x01),
	M2((byte)0x02),
	M3((byte)0x03),
	M4((byte)0x04);
	
	private final byte value;

}
