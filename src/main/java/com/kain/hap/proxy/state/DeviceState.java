package com.kain.hap.proxy.state;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DeviceState {
	PAIR_SETUP("/pair-setup"),
	PAIR_VERIFY("/pair-verify");

	@Getter
	private final String endpoint;
}
