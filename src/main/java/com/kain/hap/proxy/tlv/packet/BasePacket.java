package com.kain.hap.proxy.tlv.packet;

import com.kain.hap.proxy.tlv.State;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class BasePacket extends Packet{
	private final State state;
}
