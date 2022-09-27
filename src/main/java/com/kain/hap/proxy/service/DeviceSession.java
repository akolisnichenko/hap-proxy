package com.kain.hap.proxy.service;

import static com.kain.hap.proxy.service.SrpCalculation.hash;

import java.math.BigInteger;

import com.kain.hap.proxy.srp.Group;
import com.kain.hap.proxy.tlv.type.Salt;
import com.kain.hap.proxy.tlv.type.SrpPublicKey;

import lombok.Getter;

//Client
public final class DeviceSession extends SrpSession {
	
	// internal data 
	private final byte[] privateKey; // a
	@Getter
	private byte[] publicKey; // A
	
	
	// external data
	private byte[] externalPubKey; // B
	private byte[] salt;

	// For tests only 
	public DeviceSession(Group group, byte[] testKey) {
		privateKey = testKey;
		publicKey = SrpCalculation.generateClientPublic(group, privateKey);
	}

	public DeviceSession() {
		privateKey = generate(KEY_LENGTH);
		publicKey = SrpCalculation.generateClientPublic(Group.G_3072_BIT, privateKey);
	}
	
	public byte[] respond(SrpPublicKey externalKey, Salt salt) {
		externalPubKey = externalKey.getKey();
		this.salt = salt.getSalt();
		
		int padLength = (GROUP.getN().bitLength() + 7) / 8 ;
		byte[] u = hash(SrpCalculation.paddedBigInt(new BigInteger(1, externalPubKey), padLength),
				SrpCalculation.paddedBigInt(new BigInteger(1,publicKey), padLength));
				
		return new byte[0];
		
	}

	
}
