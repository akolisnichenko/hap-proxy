package com.kain.hap.proxy.srp;

import com.kain.hap.proxy.tlv.type.PublicKey;

public interface Session {
	PublicKey getPublicKey();
	byte[] getSharedSessionKey();
	byte[] getIdentifier();
	byte[] getSalt();
	
	// salt or proof in case of type
	byte[] respond(PublicKey key, byte[] data);
	boolean verify(byte[] data);
	

}
