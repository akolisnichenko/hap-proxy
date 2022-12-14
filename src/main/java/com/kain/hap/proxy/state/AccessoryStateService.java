package com.kain.hap.proxy.state;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.kain.hap.proxy.tlv.packet.BasePacket;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccessoryStateService implements StateService{
	
	private final BehaviourMap map;
	

	@Override
	public BasePacket onNext(StateContext context) {
		return Optional.ofNullable(map.getNextStep(context))
		.map(s -> s.handle(context))
		.orElse(null);
	}

}
