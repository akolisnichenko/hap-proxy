package com.kain.hap.proxy.service;

import java.util.UUID;

import com.kain.hap.proxy.srp.Session;

public interface SessionService {
	Session registerSession();
	Session getSession();
	void pairKey(UUID id, byte[] publicKey);
	void generateKeyPair();
	byte[] getLTPK();
	byte[] getLTSK();

}
