package com.kain.hap.proxy.service;

import java.util.Deque;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.crypto.tink.subtle.Ed25519Sign.KeyPair;
import com.kain.hap.proxy.exception.MaxPeersException;
import com.kain.hap.proxy.srp.AccessorySession;
import com.kain.hap.proxy.srp.DeviceSession;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Deprecated
//TODO: divide onto two separate classes for accessory and device 
public class SessionRegistrationService {
	//TODO: add time to live for sessions
	private Deque<AccessorySession> sessions = Lists.newLinkedList();
	private Deque<DeviceSession> devices = Lists.newLinkedList();
	@Value("${accessory.setup.code}")
	private String setupCode;
	@Value("${real.accessory.setup.code}")
	private String realSetupCode;
	@Value("${max.allowed.connections}")
	private int maxAllowedConnections;
	@Value("${accessory.identifier}")
	private String accessoryId;
	
	
	@Value("${device.identifier}")
	private String deviceIdentifier;
	
	
	private Map<UUID, byte[]> connectedDeviceKeys = Maps.newHashMap();
	private Map<UUID, KeyPair> connectedAccessoryKeys = Maps.newHashMap();
	
	
	
	public AccessorySession registerSession() {
		AccessorySession session = new AccessorySession(setupCode, accessoryId);
		sessions.push(session);
		return session;
	}
	
	public DeviceSession registerDevice() {
		DeviceSession session = new DeviceSession(realSetupCode, deviceIdentifier);
		devices.push( session);
		return session;
	}

	//TODO: replace by error if not exist 
	public AccessorySession getSession() {
		return sessions.peek();
	}
	
	public DeviceSession getDeviceSession() {
		return devices.peek();
	}

	public void registerDevicePairing(UUID iosDevicePairingUUID, byte[] iosDeviceLTPK) {
		connectedDeviceKeys.put(iosDevicePairingUUID, iosDeviceLTPK);
	}

	public void registerAccessoryPairing(UUID accessoryUUID, KeyPair keyPair) {
		if(connectedDeviceKeys.size() > maxAllowedConnections) {
			throw new MaxPeersException();
		}
		connectedAccessoryKeys.put(accessoryUUID, keyPair);
	}

	public byte[] getLongTermSecretKey(String accessoryIdentifier) {
		UUID sessionAccId = UUID.nameUUIDFromBytes(accessoryIdentifier.getBytes());
		return connectedAccessoryKeys.get(sessionAccId).getPrivateKey();
		
	}
}
