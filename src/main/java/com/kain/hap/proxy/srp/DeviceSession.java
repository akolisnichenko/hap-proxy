	package com.kain.hap.proxy.srp;

import static com.kain.hap.proxy.srp.SrpCalculation.hash;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

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
	
	
	// external data
	private byte[] externalPubKey; // B
	private byte[] salt;
	private byte[] secret;
	private byte[] M1;
	private String username;
	private byte[] pw;

	// For tests only 
	public DeviceSession(Group group, byte[] testKey, String username, String password) {
		privateKey = testKey;
		publicKey = SrpCalculation.generateClientPublic(group, privateKey);
		this.username = username;
		pw = hash(username, ":", password);
	}

	public DeviceSession(String setupCode) {
		this.username = IDENTIFIER;
		privateKey = generate(KEY_LENGTH);
		publicKey = SrpCalculation.generateClientPublic(Group.G_3072_BIT, privateKey);
		pw = hash(username, ":", setupCode);
	}
	
	// <premaster secret> = (B - (k * g^x)) ^ (a + (u * x)) % N
	//                  S = ((B - k (g^x)) ^ (a + ux)) % N
	//                  S = (B - kg^x) ^ (a + ux)
	
	//SC = (B − k(g^x))^(a + ux) // lost formating see on https://habr.com/ru/post/579704/
	
	public byte[] respond(SrpPublicKey externalKey, Salt salt) {
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
		//M1 = hash(publicKey, externalPubKey, secret);
		
		M1 = calculateM();
				
		return M1;
	}
	

	private byte[] calculateM() {
		byte[] hN = hash(GROUP.getNAsArr());
		byte[] hG = hash(GROUP.getGAsArr());
		byte[] xorResult = xor(hN, hG);
		byte[] K = hash(secret);
		byte[] hU = hash(username);
		
		byte[] M = hash(xorResult, hU, salt, publicKey, externalPubKey, K);
		return M;
		
	}

	public void verify(byte[] data) {
		byte[] M2 = hash(publicKey, M1, secret);
		if (data != M2) {
			log.error("Not valid result evidance message");
		}
	}
	
	 private byte[] xor(byte[] b1, byte[] b2) {
		    byte[] result = new byte[b1.length];
		    for (int i = 0; i < b1.length; i++) {
		      result[i] = (byte) (b1[i] ^ b2[i]);
		    }
		    return result;
		  }
	
}
