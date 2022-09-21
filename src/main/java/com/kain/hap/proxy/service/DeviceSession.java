package com.kain.hap.proxy.service;

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
	@Getter
	private byte[] salt;



	public DeviceSession() {
		privateKey = generate(KEY_LENGTH);
		publicKey = SrpCalculation.generateClientPublic(Group.G_3072_BIT, privateKey);
	}
	
	public byte[] respond(SrpPublicKey pubKey, Salt salt) {
		externalPubKey = pubKey.getKey();
		this.salt = salt.getSalt();
		return new byte[0];
		
	}

	
}
