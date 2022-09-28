package com.kain.hap.proxy.service;

import static com.kain.hap.proxy.service.SrpCalculation.hash;

import java.math.BigInteger;

import com.kain.hap.proxy.srp.Group;
import com.kain.hap.proxy.tlv.type.Salt;
import com.kain.hap.proxy.tlv.type.SrpPublicKey;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

//Client
// TODO: add stoppers to avoid repeatable call
@Slf4j
public final class DeviceSession extends SrpSession {
	
	// internal data 
	private final byte[] privateKey; // a
	@Getter
	private byte[] publicKey; // A
	private String setupCode;
	
	
	// external data
	private byte[] externalPubKey; // B
	private byte[] salt;
	private byte[] secret;
	private byte[] M1;
	private byte[] pw;

	// For tests only 
	public DeviceSession(Group group, byte[] testKey, String username, String password) {
		privateKey = testKey;
		publicKey = SrpCalculation.generateClientPublic(group, privateKey);
		pw = hash(username, ":", password);
	}

	public DeviceSession(String setupCode) {
		this.setupCode = setupCode;
		privateKey = generate(KEY_LENGTH);
		publicKey = SrpCalculation.generateClientPublic(Group.G_3072_BIT, privateKey);
		pw = hash(IDENTIFIER, ":", setupCode);
		
	}
	
	public byte[] respond(SrpPublicKey externalKey, Salt salt) {
		externalPubKey = externalKey.getKey();
		this.salt = salt.getSalt();
		byte[] x = hash(this.salt, pw);
		
		int padLength = (GROUP.getN().bitLength() + 7) / 8 ;
		byte[] u = hash(SrpCalculation.paddedBigInt(new BigInteger(1, externalPubKey), padLength),
				SrpCalculation.paddedBigInt(new BigInteger(1,publicKey), padLength));
		byte[] k = hash(GROUP.getNAsArr(), SrpCalculation.paddedBigInt(GROUP.getG(), padLength));
		
		final BigInteger exp = new BigInteger(1,u).multiply(new BigInteger(1, x)).add(new BigInteger(1,privateKey));
		final BigInteger tmp = GROUP.getG().modPow(new BigInteger(1, x), GROUP.getN()).multiply(new BigInteger(1, k));
		secret = new BigInteger(1, externalPubKey).subtract(tmp).modPow(exp, GROUP.getN()).toByteArray();
		M1 = hash(publicKey, externalPubKey, secret);
				
		return M1;
	}
	

	public void verify(byte[] data) {
		byte[] M2 = hash(publicKey, M1, secret);
		if (data != M2) {
			log.error("Not valid result evidance message");
		}
	}
	
}
