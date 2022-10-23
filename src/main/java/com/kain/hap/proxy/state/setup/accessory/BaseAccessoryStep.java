package com.kain.hap.proxy.state.setup.accessory;

import com.kain.hap.proxy.service.AccessorySessionService;
import com.kain.hap.proxy.service.SessionService;
import com.kain.hap.proxy.state.AccessoryStep;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseAccessoryStep implements AccessoryStep {
	private final AccessorySessionService sessionService;

	@Override
	public SessionService getSessionService() {
		return sessionService;
	}
}
