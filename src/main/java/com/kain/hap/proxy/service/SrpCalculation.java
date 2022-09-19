package com.kain.hap.proxy.service;

import java.math.BigInteger;
import java.util.Arrays;

import com.kain.hap.proxy.srp.Group;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SrpCalculation {
	
	public static byte[] generateClientPublic(Group group, byte[] privateKey) {
		return trimBigInt(group.getG().modPow(new BigInteger(1, privateKey), group.getN()));
	}
	
	private static byte[] trimBigInt(BigInteger value) {
		byte[] result = value.toByteArray();
		int i = 0;
		if (result[0] == 0) {
			i++;
		}
		return Arrays.copyOfRange(result, i, result.length);
	}


}
