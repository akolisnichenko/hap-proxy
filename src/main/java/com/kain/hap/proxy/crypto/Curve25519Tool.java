package com.kain.hap.proxy.crypto;

import java.security.SecureRandom;

public class Curve25519Tool{
	
	private static final SecureRandom sr = new SecureRandom();
	
	private static byte[] generateRandom(int length) {
		byte[] bytes = new byte[length];
		sr.nextBytes(bytes);
		return bytes;
	}

	public static KeyPair generateKeys() {
		byte[] privateKey = generateRandom(32);
		privateKey[31] &= 0x7F;
		privateKey[31] |= 0x40;
		privateKey[ 0] &= 0xF8;
		
		byte[] publicKey = new byte[32];
		Curve25519.core(publicKey, null, privateKey, null);
		return new KeyPair(privateKey, publicKey);
	}
	
	public static byte[] generateShared(byte[] secret, byte[] externalPubkey) {
		byte[] shared = new byte[32];
		Curve25519.core(shared, null, secret, externalPubkey);
		return shared;
	}
}
