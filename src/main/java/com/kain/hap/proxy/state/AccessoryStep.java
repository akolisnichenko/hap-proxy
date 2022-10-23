package com.kain.hap.proxy.state;

import com.kain.hap.proxy.service.SessionService;

//TODO: decide what to do with class merge with DeviceStep or extend  
public interface AccessoryStep extends BehaviourStep {
	SessionService getSessionService();
}
