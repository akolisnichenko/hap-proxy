package com.kain.hap.proxy.crypto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KeyPair {
	private byte[] secret;
	private byte[] pubKey;
}
