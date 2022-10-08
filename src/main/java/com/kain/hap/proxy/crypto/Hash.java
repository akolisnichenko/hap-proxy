package com.kain.hap.proxy.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Hash {

	public static byte[] hmacSha512(byte[] secret, byte[] input) {
		byte[] result;
		try {
			Mac hmacSHA512 = Mac.getInstance("HmacSHA512");
			SecretKeySpec secretKeySpec = new SecretKeySpec(secret, "HmacSHA512");
			hmacSHA512.init(secretKeySpec);

			result = hmacSHA512.doFinal(input);
			
		} catch (IllegalStateException | InvalidKeyException | NoSuchAlgorithmException ex) {
			throw new RuntimeException("Problemas calculando HMAC", ex);
		}
		return result;
	}
	
	public static byte[] hmacSha256(byte[] secret, byte[] input) {
		byte[] result;
		try {
			Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKeySpec = new SecretKeySpec(secret, "HmacSHA256");
			hmacSHA256.init(secretKeySpec);

			result = hmacSHA256.doFinal(input);
			
		} catch (IllegalStateException | InvalidKeyException | NoSuchAlgorithmException ex) {
			throw new RuntimeException("Problemas calculando HMAC", ex);
		}
		return result;
	}
}
