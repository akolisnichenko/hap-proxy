package com.kain.hap.proxy.tools;

import java.security.SecureRandom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Salt {
	private static final int SALT_LENGTH = 16;
	private static final SecureRandom random = new SecureRandom();
	private final byte[] salt;
	
	public static Salt generate() {
		byte[] generated = new byte[SALT_LENGTH];
		random.nextBytes(generated);
		return new Salt(generated);
	}
}
