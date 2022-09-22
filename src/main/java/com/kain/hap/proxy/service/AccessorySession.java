package com.kain.hap.proxy.service;

import static com.kain.hap.proxy.service.SrpCalculation.calculateVerifier;
import static com.kain.hap.proxy.service.SrpCalculation.generateServerPublic;
import static com.kain.hap.proxy.service.SrpCalculation.hash;

import java.math.BigInteger;

import com.kain.hap.proxy.srp.Group;
import com.kain.hap.proxy.tlv.type.Proof;
import com.kain.hap.proxy.tlv.type.SrpPublicKey;

import lombok.Getter;

public class AccessorySession extends SrpSession {
	//constants
	private static final String IDENTIFIER = "Pair-Setup";
	
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
	
	private byte[] secret;
	
	
	public AccessorySession(String setupCode) {
		salt = generate(SALT_LENGTH);
		x = hash(salt, hash(IDENTIFIER, ":", setupCode));
		verifier = calculateVerifier(Group.G_3072_BIT, x);
		privateKey = generate(KEY_LENGTH);
		publicKey = generateServerPublic(Group.G_3072_BIT, verifier, privateKey);
	}
	
	public byte[] respond(SrpPublicKey clientKey, Proof proof) {
		externalKey = clientKey.getKey();
		byte[] u = hash(externalKey, publicKey);
		
		BigInteger A = new BigInteger(1, externalKey);
		if(A.mod(Group.G_3072_BIT.getN()).equals(BigInteger.ZERO)) {
			// BAD PUBLIC KEY ERROR
		}

		//(A * v^u) ^ b % N
		secret =  A
		.multiply(new BigInteger(1, verifier).multiply(new BigInteger(1, u)))
		.modPow(new BigInteger(1, privateKey), Group.G_3072_BIT.getN()).toByteArray();
		//shared session key
		byte[] K = hash(secret);
		byte[] M2 = hash(externalKey, proof.getProof(), K);
		return M2;
	}
}
