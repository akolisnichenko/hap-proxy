package com.kain.hap.proxy.state;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.packet.Packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Component
@RequiredArgsConstructor
public class DeviceStateService implements StateService {
	private final DeviceStepMap map;
	@Getter
	@Setter
	private DeviceState currentState = DeviceState.PAIR_SETUP;

	@Override
	public Packet onNext(StateContext context) {
		if (context.getIncome().getState() == State.M6 && currentState == DeviceState.PAIR_SETUP) {
			currentState = DeviceState.PAIR_VERIFY;
			context.setIncome(Packet.builder().state(null).build());
		}
		context.setEndpoint(currentState.getEndpoint());
		
		return Optional.ofNullable(map.getNextStep(context))
		.map(s -> s.handle(context))
		.orElse(null);
	}

}
