package com.kain.hap.proxy.service;

import java.util.Map;

import com.google.common.collect.Maps;

public class SessionRegistrationService {
	private Map<String, DeviceSession> sessions = Maps.newConcurrentMap();
	
	public DeviceSession registerSession(String deviceId) {
		return sessions.getOrDefault(deviceId, newSession());
	}

	private DeviceSession newSession() {
		return new DeviceSession();
	}

}
