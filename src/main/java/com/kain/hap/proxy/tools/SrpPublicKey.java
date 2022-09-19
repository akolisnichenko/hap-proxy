package com.kain.hap.proxy.tools;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter

public class SrpPublicKey {
	private final byte[] key;

}
