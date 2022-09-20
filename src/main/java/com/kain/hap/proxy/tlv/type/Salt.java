package com.kain.hap.proxy.tlv.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Salt {
	private final byte[] salt;
}
