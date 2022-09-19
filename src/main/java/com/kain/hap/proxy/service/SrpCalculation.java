package com.kain.hap.proxy.service;

import java.math.BigInteger;

import com.kain.hap.proxy.srp.Group;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SrpCalculation {
	
	public static byte[] generateClientPublic(Group group, byte[] privateKey) {
		return group.getG().modPow(new BigInteger(1, privateKey) , group.getN()).toByteArray();
	}

}
