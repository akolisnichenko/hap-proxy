package com.kain.hap.proxy.tlv;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Method {
	PAIR_SETUP((byte)0x00),
	PAIR_SETUP_WITH_AUTH((byte)0x01),
	PAIR_VERIFY((byte)0x02),
	ADD_PAIRING((byte)0x03),
	REMOVE_PAIRING((byte)0x04),
	LIST_PAIRING((byte)0x05),
	RESERVED((byte)0x06); // 6-255 

	private final byte value;
}
