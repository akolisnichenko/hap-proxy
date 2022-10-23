package com.kain.hap.proxy.state;

import com.kain.hap.proxy.service.SessionService;

public interface DeviceStep extends BehaviourStep{
	SessionService getSessionService();
}
