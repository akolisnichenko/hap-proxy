package com.kain.hap.proxy.state.setup.device;

import com.kain.hap.proxy.service.DeviceSessionService;
import com.kain.hap.proxy.service.SessionService;
import com.kain.hap.proxy.state.DeviceStep;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseDeviceStep implements DeviceStep {
	private final DeviceSessionService sessionService;

	@Override
	public SessionService getSessionService() {
		return sessionService;
	}
}
