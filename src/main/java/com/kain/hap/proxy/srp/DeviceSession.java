	package com.kain.hap.proxy.srp;

import static com.kain.hap.proxy.srp.SrpCalculation.hash;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import com.google.crypto.tink.subtle.Ed25519Sign;
import com.google.crypto.tink.subtle.Ed25519Sign.KeyPair;
import com.kain.hap.proxy.tlv.type.Salt;
import com.kain.hap.proxy.tlv.type.PublicKey;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

//Client
// TODO: add stoppers to avoid repeatable call
@Slf4j
public final class DeviceSession extends SrpSession {
	
	@Getter
	private String deviceId;

	// internal data 
	@Getter
	private final byte[] privateKey; // a
	@Getter
	private byte[] publicKey; // A
	
	
	// external data
	private byte[] externalPubKey; // B
	private byte[] salt;
	private byte[] secret;
	private byte[] M1;
	private String username;
	private byte[] pw;
	
	private KeyPair edKeyPair;

	// For tests only 
	public DeviceSession(Group group, byte[] testKey, String username, String password) {
		privateKey = testKey;
		publicKey = SrpCalculation.generateClientPublic(group, privateKey);
		this.username = username;
		pw = hash(username, ":", password);
	}

	public DeviceSession(String setupCode, String deviceId) {
		this.deviceId= deviceId;
		this.username = IDENTIFIER;
		privateKey = generate(KEY_LENGTH);
		publicKey = SrpCalculation.generateClientPublic(Group.G_3072_BIT, privateKey);
		pw = hash(username, ":", setupCode);
	}
	
	public byte[] respond(PublicKey externalKey, Salt salt) {
		externalPubKey = externalKey.getKey();
		this.salt = salt.getSalt();
		byte[] x = hash(this.salt, pw);
		
		int padLength = (GROUP.getN().bitLength() + 7) / 8 ;
		byte[] u = hash(SrpCalculation.paddedBigInt(new BigInteger(1, publicKey), padLength),
				SrpCalculation.paddedBigInt(new BigInteger(1,externalPubKey), padLength));
		byte[] k = hash(SrpCalculation.paddedBigInt(GROUP.getN(), padLength), SrpCalculation.paddedBigInt(GROUP.getG(), padLength));
		
		final BigInteger exp = new BigInteger(1,u).multiply(new BigInteger(1, x)).add(new BigInteger(1,privateKey));
		final BigInteger tmp = GROUP.getG().modPow(new BigInteger(1, x), GROUP.getN()).multiply(new BigInteger(1, k));
		secret = SrpCalculation.trimBigInt(new BigInteger(1, externalPubKey).subtract(tmp).modPow(exp, GROUP.getN()));
		M1 = calculateM();
		return M1;
	}
	

	private byte[] calculateM() {
		byte[] hN = hash(GROUP.getNAsArr());
		byte[] hG = hash(GROUP.getGAsArr());
		byte[] xorResult = xor(hN, hG);
		byte[] K = hash(secret);
		byte[] hU = hash(username);
		return hash(xorResult, hU, salt, publicKey, externalPubKey, K);
	}


	public boolean verify(byte[] data) {
		boolean result = Arrays.equals(data, hash(publicKey, M1, hash(secret)));
		if (result) {
			generateEd25519Keys();
		}
		return result;
	}
	
	private byte[] xor(byte[] b1, byte[] b2) {
		byte[] result = new byte[b1.length];
		for (int i = 0; i < b1.length; i++) {
			result[i] = (byte) (b1[i] ^ b2[i]);
		}
		return result;
	}

	//FIXME: replace by correct using of session
	public byte[] getSharedSessionKey() {
		return hash(secret);
	}
	
	public byte[] getDeviceLTPK() {
		return edKeyPair.getPublicKey();
	}
	
	public byte[] getDeviceLTSK() {
		return edKeyPair.getPrivateKey();
	}
	
	private void generateEd25519Keys() {
		try {
			edKeyPair = KeyPair.newKeyPair();
		} catch (GeneralSecurityException e) {
			log.error("Key pair was not generated:", e);
		}
	}
	
}
