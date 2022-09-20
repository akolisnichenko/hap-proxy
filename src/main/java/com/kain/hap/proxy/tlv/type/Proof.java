package com.kain.hap.proxy.tlv.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Proof {
	private final byte[] proof;
}
