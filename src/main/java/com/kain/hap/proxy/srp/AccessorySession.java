package com.kain.hap.proxy.srp;

import static com.kain.hap.proxy.srp.SrpCalculation.calculateVerifier;
import static com.kain.hap.proxy.srp.SrpCalculation.generateServerPublic;
import static com.kain.hap.proxy.srp.SrpCalculation.hash;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.kain.hap.proxy.tlv.type.Proof;
import com.kain.hap.proxy.tlv.type.SrpPublicKey;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class AccessorySession extends SrpSession {

	
	//internal data
	@Getter
	private final byte[] salt;
	private final byte[] privateKey; // b
	@Getter
	private byte[] publicKey; // B
	
	private byte[] x;
	private byte[] verifier;
	
	//external data
	private byte[] externalKey; // A
	private String username;
	private byte[] secret;
	
	// For tests only 
	public AccessorySession(Group group, String username, String password, byte[] salt, byte[] privateKey) {
		this.username = username;
		this.salt = salt;
		x = hash(salt, hash(username, ":", password));
		verifier = calculateVerifier(group, x);
		this.privateKey = privateKey;
		publicKey = generateServerPublic(group, verifier, this.privateKey);
	}
	
	
	public AccessorySession(String setupCode) {
		username = IDENTIFIER;
		salt = generate(SALT_LENGTH);
		x = hash(salt, hash(IDENTIFIER, ":", setupCode));
		verifier = calculateVerifier(GROUP, x);
		privateKey = generate(KEY_LENGTH);
		publicKey = generateServerPublic(GROUP, verifier, privateKey);
	}
	
	public byte[] respond(SrpPublicKey clientKey, Proof proof) {
		externalKey = clientKey.getKey();
		
		int padLength = (GROUP.getN().bitLength() + 7) / 8 ;
		
		byte[] u = hash(SrpCalculation.paddedBigInt(new BigInteger(1, externalKey), padLength),
				SrpCalculation.paddedBigInt(new BigInteger(1,publicKey), padLength));
		
		BigInteger A = new BigInteger(1, externalKey);
		if(A.mod(GROUP.getN()).equals(BigInteger.ZERO)) {
			// BAD PUBLIC KEY ERROR
		}
	
		secret = SrpCalculation.trimBigInt(new BigInteger(1, verifier).modPow(new BigInteger(1,u), GROUP.getN()).multiply(A).modPow(new BigInteger(1, privateKey), GROUP.getN()));
		byte[] K = hash(secret);
		
		byte[] validM = calculateM();
		
		if (!Arrays.equals(validM, proof.getProof())) {
			log.error("Not equal proof");
		}
		return hash(externalKey, validM, K);
	}
	
	private byte[] calculateM() {
		byte[] hN = hash(GROUP.getNAsArr());
		byte[] hG = hash(GROUP.getGAsArr());
		byte[] K = hash(secret);
		byte[] hU = hash(username);
		
		byte[] M = hash(xor(hN, hG), hU, salt, externalKey, publicKey, K);
		return M;
		
	}
	
	 private byte[] xor(byte[] b1, byte[] b2) {
		    byte[] result = new byte[b1.length];
		    for (int i = 0; i < b1.length; i++) {
		      result[i] = (byte) (b1[i] ^ b2[i]);
		    }
		    return result;
		  }
}
