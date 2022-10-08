package com.kain.hap.proxy.srp;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.google.common.hash.Hashing;
import com.google.common.primitives.Bytes;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SrpCalculation {

	public static byte[] generateClientPublic(Group group, final byte[] privateKey) {
		return trimBigInt(group.getG().modPow(new BigInteger(1, privateKey), group.getN()));
	}

	public static byte[] generateServerPublic(Group group, final byte[] verifier, final byte[] privateKey) {
		// TODO: move constant to group

		int padLength = (group.getN().bitLength() + 7) / 8 ;
		byte[] k = hash(paddedBigInt(group.getN(), padLength), paddedBigInt(group.getG(), padLength));
		return trimBigInt(group.getG().modPow(new BigInteger(1, privateKey), group.getN()).add(new BigInteger(1, verifier).multiply(new BigInteger(1, k))).mod(group.getN()));
	}

	public static byte[] calculateVerifier(Group group, final byte[] hashData) {
		return trimBigInt(group.getG().modPow(new BigInteger(1, hashData), group.getN()));
	}

	public static byte[] trimBigInt(BigInteger value) {
		byte[] result = value.toByteArray();
		if (result[0] == 0) {
			return Arrays.copyOfRange(result, 1, result.length);
		}
		return result;
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
