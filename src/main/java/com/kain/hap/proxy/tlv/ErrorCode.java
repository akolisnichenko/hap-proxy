package com.kain.hap.proxy.tlv;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ErrorCode {
	NA,
	UNKNOWN,
	AUTHENTICATION,
	BACKOFF,
	MAX_PEERS,
	MAX_TRIES,
	UNAVAILABLE,
	BUSY,
	RESERVED;   //0x08-0xFF
	
    private static final Map<Byte, ErrorCode> mappedValues = Stream.of(values()).collect(Collectors.toMap(e -> (byte)e.ordinal(), Function.identity() , (s1,s2) -> s1));
	
	public static ErrorCode toError(byte value) {
		return mappedValues.get(value);
	}

}
