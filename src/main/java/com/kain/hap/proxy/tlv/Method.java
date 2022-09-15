package com.kain.hap.proxy.tlv;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	
    private static final Map<Byte, Method> mappedValues = Stream.of(values()).collect(Collectors.toMap(Method::getValue, Function.identity() , (s1,s2) -> s1));
	
	public static Method toMethod(byte value) {
		return mappedValues.get(value);
	}
}
