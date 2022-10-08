package com.kain.hap.proxy.tlv.packet;

import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.type.Encrypted;

import lombok.Getter;


@Getter
public class EncryptedPacket extends BasePacket {
	private final Encrypted data;
	
	public EncryptedPacket(State state, Encrypted data) {
		super(state);
		this.data = data;
	}
}