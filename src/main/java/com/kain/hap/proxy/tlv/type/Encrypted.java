package com.kain.hap.proxy.tlv.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Encrypted {
	private final byte[] data;
}
