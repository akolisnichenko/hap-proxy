package com.kain.hap.proxy.service;

import java.security.GeneralSecurityException;
import java.util.Deque;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.crypto.tink.subtle.Ed25519Sign.KeyPair;
import com.kain.hap.proxy.srp.DeviceSession;
import com.kain.hap.proxy.srp.Session;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DeviceSessionService implements SessionService {
	@Value("${real.accessory.setup.code}")
	private String realSetupCode;  // setup code will be sent to accessory as part of login (code on QR)
	@Value("${device.identifier}")
	private String deviceIdentifier; // randomly generated UUID to present emulator as real device 
	
	//TODO: check number of allowed sessions  
	private Deque<DeviceSession> sessions = Lists.newLinkedList();
	//TODO: decide what is key pair storage 
	//private Map<UUID, KeyPair> keys = Maps.newHashMap();
	
	private Map<UUID, byte[]> paired = Maps.newHashMap();
	private KeyPair generatedKeyPair;
	
	@PostConstruct
	private void loadPairedKeys() {
		//TODO: implement loading of existing keys
		log.error("Not implemented yet");
	}
	
	@PreDestroy
	private void savePairedKeys() {
		//TODO: implement save  of existing keys
		log.error("Not implemented yet");
	}

	@Override
	public Session registerSession() {
		DeviceSession session = new DeviceSession(realSetupCode, deviceIdentifier);
		sessions.push( session);
		return session;
	}

	@Override
	public Session getSession() {
		return sessions.peek();
	}

	@Override
	public void pairKey(UUID id, byte[] publicKey) {
		paired.put(id, publicKey);
	}

	@Override
	public void generateKeyPair() {
		try {
			generatedKeyPair = KeyPair.newKeyPair();
		} catch (GeneralSecurityException e) {
			log.error("Key pair was not generated:", e);
		}
	}
	
	public byte[] getLTPK() {
		return generatedKeyPair.getPublicKey();
	}
	
	@Override
	public byte[] getLTSK() {
		return generatedKeyPair.getPrivateKey();
	}


}
