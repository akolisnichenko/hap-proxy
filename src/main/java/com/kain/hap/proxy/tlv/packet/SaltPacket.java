package com.kain.hap.proxy.tlv.packet;

import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.type.Salt;
import com.kain.hap.proxy.tlv.type.PublicKey;

import lombok.Getter;

@Getter
public class SaltPacket extends BasePacket {
	private final Salt salt;
	private final PublicKey publicKey;

	public SaltPacket(State state, PublicKey publicKey, Salt salt) {
		super(state);
		this.salt = salt;
		this.publicKey = publicKey;
	}

}
