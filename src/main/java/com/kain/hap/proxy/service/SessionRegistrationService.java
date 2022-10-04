package com.kain.hap.proxy.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.kain.hap.proxy.srp.AccessorySession;
import com.kain.hap.proxy.srp.DeviceSession;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionRegistrationService {
	private Map<String, AccessorySession> sessions = Maps.newConcurrentMap();
	private Map<String, DeviceSession> devices = Maps.newConcurrentMap();
	@Value("${real.accessory.setup.code}")
	private String setupCode;
	
	public AccessorySession registerSession(String deviceId) {
		AccessorySession session = new AccessorySession(setupCode);
		sessions.put(deviceId, session);
		return session;
	}
	
	public DeviceSession registerDevice(String deviceId) {
		DeviceSession session = new DeviceSession(setupCode);
		devices.put(deviceId, session);
		return session;
	}

	//TODO: replace by error if not exist 
	public AccessorySession getSession(String deviceId) {
		return sessions.get(deviceId);
	}
	
	public DeviceSession getDeviceSession(String deviceId) {
		return devices.get(deviceId);
	}

}
