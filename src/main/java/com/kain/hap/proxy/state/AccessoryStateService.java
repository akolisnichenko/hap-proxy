package com.kain.hap.proxy.state;

import org.springframework.stereotype.Component;

import com.kain.hap.proxy.tlv.packet.BasePacket;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccessoryStateService implements StateService{
	
	private final BehaviourMap map;
	

	@Override
	public BasePacket onNext(StateContext context) {
		return map.getNextStep(context.getState()).handle(context);
	}

}
