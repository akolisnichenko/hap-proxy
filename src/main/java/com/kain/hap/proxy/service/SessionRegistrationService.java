package com.kain.hap.proxy.service;

import java.util.Map;

import com.google.common.collect.Maps;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SessionRegistrationService {
	public static final SessionRegistrationService INSTANCE = new SessionRegistrationService();
	
	private Map<String, DeviceSession> sessions = Maps.newConcurrentMap();
	
	public DeviceSession registerSession(String deviceId) {
		DeviceSession session = new DeviceSession();
		sessions.put(deviceId, session);
		return session;
	}

	//TODO: replace by error if not exist 
	public DeviceSession getSession(String deviceId) {
		return sessions.get(deviceId);
	}

}
