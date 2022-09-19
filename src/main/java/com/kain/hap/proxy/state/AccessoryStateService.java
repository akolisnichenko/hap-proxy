package com.kain.hap.proxy.state;

import com.kain.hap.proxy.tlv.packet.BasePacket;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccessoryStateService implements StateService{
	
	private final BehaviourMap map;
	

	@Override
	public BasePacket onNext(BasePacket income) {
		// TODO: wrap into context
		
		return map.getNextStep(income.getState()).handle(null);
		
	}

}
