package com.kain.hap.proxy.tlv;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	
	private static final Map<Byte, State> mappedValues = Stream.of(values()).collect(Collectors.toMap(State::getValue, Function.identity() , (s1,s2) -> s1));
	
	public static State toState(byte value) {
		return mappedValues.get(value);
	}

}
