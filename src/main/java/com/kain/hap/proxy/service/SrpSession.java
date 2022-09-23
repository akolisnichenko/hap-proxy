package com.kain.hap.proxy.service;

import java.security.SecureRandom;

import com.kain.hap.proxy.srp.Group;


// TODO: add group and predefined settings from group
public abstract class SrpSession {
	protected static final int SALT_LENGTH = 16;
	protected static final int KEY_LENGTH = 64;
	protected static final SecureRandom random = new SecureRandom();
	
	//protected static final Group GROUP = Group.G_1024_BIT;
	protected static final Group GROUP = Group.G_3072_BIT;
	
	protected byte[] generate(int length) {
		byte[] generated = new byte[length];
		random.nextBytes(generated);
		return generated;
	}
	

}
