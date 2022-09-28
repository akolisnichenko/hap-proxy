package com.kain.hap.proxy.srp;

import java.security.SecureRandom;


// TODO: add group and predefined settings from group
public abstract class SrpSession {
	protected static final int SALT_LENGTH = 16;
	protected static final int KEY_LENGTH = 64;
	protected static final SecureRandom random = new SecureRandom();
	
	//protected static final Group GROUP = Group.G_1024_BIT;
	protected static final Group GROUP = Group.G_3072_BIT;
	//constants
	protected static final String IDENTIFIER = "Pair-Setup";
	
	protected byte[] generate(int length) {
		byte[] generated = new byte[length];
		random.nextBytes(generated);
		return generated;
	}
	

}
