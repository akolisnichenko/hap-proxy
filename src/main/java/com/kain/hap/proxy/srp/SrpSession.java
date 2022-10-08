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
	
	protected String toHex(byte[] val) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (byte b : val) {
			if(i % 8 == 0) {
				sb.append("0x");
			}
			sb.append(String.format("%02x", b));
			i++;
			if( i % 8 == 0 && i != 0) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}
	

}
