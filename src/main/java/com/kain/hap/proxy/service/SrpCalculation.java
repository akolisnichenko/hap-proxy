package com.kain.hap.proxy.service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.google.common.hash.Hashing;
import com.google.common.primitives.Bytes;
import com.kain.hap.proxy.srp.Group;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SrpCalculation {

	public static byte[] generateClientPublic(Group group, byte[] privateKey) {
		return trimBigInt(group.getG().modPow(new BigInteger(1, privateKey), group.getN()));
	}

	public static byte[] generateServerPublic(Group group, byte[] verifier, byte[] privateKey) {
		// TODO: move constant to group
		/*byte[] k = hash(Group.G_3072_BIT.getNAsArr(), SrpCalculation.trimBigInt(Group.G_3072_BIT.getG()));
		return trimBigInt(new BigInteger(1, k).multiply(new BigInteger(1, verifier))
				.add(group.getG().modPow(new BigInteger(1, privateKey), group.getN()))); */
		
		// old implementation
		
		byte[]k = hash(group.getNAsArr(), SrpCalculation.trimBigInt(group.getG()));
		
		BigInteger B1 = new BigInteger(1, k).multiply(new BigInteger(1, verifier));
		BigInteger B2 =	group.getG().modPow(new BigInteger(1, privateKey), group.getN());
		BigInteger sum = B1.add(B2);
		BigInteger publicValue = sum.mod(group.getN());
		return trimBigInt(publicValue);
	}

	public static byte[] calculateVerifier(Group group, byte[] hashData) {
		return trimBigInt(group.getG().modPow(new BigInteger(1, hashData), group.getN()));
	}

	public static byte[] trimBigInt(BigInteger value) {
		byte[] result = value.toByteArray();
		int i = 0;
		if (result[0] == 0) {
			i++;
		}
		return Arrays.copyOfRange(result, i, result.length);
	}

	public static byte[] paddedBigInt(BigInteger value, int length) {
		byte[] bs = trimBigInt(value);
		if (bs.length < length) {
			byte[] tmp = new byte[length];
			System.arraycopy(bs, 0, tmp, length - bs.length, bs.length);
			bs = tmp;
		}
		return bs;
	}

	public static byte[] hash(String... args) {
		return Hashing.sha512().hashString(String.join("", args), StandardCharsets.UTF_8).asBytes();
	}

	public static byte[] hash(byte[]... args) {
		return Hashing.sha512().hashBytes(Bytes.concat(args)).asBytes();
	}

}
