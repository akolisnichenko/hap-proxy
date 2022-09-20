package com.kain.hap.proxy.tlv.packet;

import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.type.Salt;
import com.kain.hap.proxy.tlv.type.SrpPublicKey;

import lombok.Getter;

@Getter
public class SaltPacket extends BasePacket {
	private final Salt salt;
	private final SrpPublicKey publicKey;

	public SaltPacket(State state, SrpPublicKey publicKey, Salt salt) {
		super(state);
		this.salt = salt;
		this.publicKey = publicKey;
	}

}
