package com.kain.hap.proxy.crypto;

import java.nio.ByteBuffer;
import java.util.Arrays;

import com.google.common.primitives.Bytes;

public class HKDF {
	// sha512 - 64 octets(bytes)
	@FunctionalInterface
	public interface Function<One, Two, Three> {
		public Three apply(One one, Two two);
	}

	private byte[] prk;
	private Function<byte[], byte[], byte[]> hashFunction;

	public HKDF(Function<byte[], byte[], byte[]> hash) {
		hashFunction = hash;
	}

	public byte[] extract(byte[] salt, byte[] ikm) {
		byte[] result = hashFunction.apply(salt, ikm);
		prk = result;
		return result;
	}

	public byte[] expand(byte[] info, int length) {
		// see hashLength - the length of the hash function output in octets
		int n = (int) Math.ceil((double) length / (double) 64);
		byte[] tResult = new byte[0];
		byte[] t = new byte[0];
		for (int i = 1; i <= n; i++) {
			t = hashFunction.apply(prk, Bytes.concat(t, info, new byte[] { (byte) i }));
			tResult = Bytes.concat(tResult, t);
		}
		return Arrays.copyOf(tResult, length);
	}

	public static void main(String[] args) {
		byte[] ikm = new byte[] { 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b,
				0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b, 0x0b };
		byte[] salt = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c };

		byte[] info = ByteBuffer.allocate(10).putLong(0xf0f1f2f3f4f5f6f7L).putChar((char) 0xf8f9).array();
		int length = 42;

		HKDF hkdf = new HKDF(Hash::hmacSha512);
		hkdf.extract(salt, ikm);
		byte[] result = hkdf.expand(info, length);
		System.out.println("Result:" + result);
	}

}
