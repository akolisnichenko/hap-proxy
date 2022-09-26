package com.kain.hap.proxy.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionRegistrationService {
	private Map<String, AccessorySession> sessions = Maps.newConcurrentMap();
	@Value("${accessory.setup.code}")
	private String setupCode;
	
	public AccessorySession registerSession(String deviceId) {
		AccessorySession session = new AccessorySession(setupCode);
		sessions.put(deviceId, session);
		return session;
	}

	//TODO: replace by error if not exist 
	public AccessorySession getSession(String deviceId) {
		return sessions.get(deviceId);
	}

}
