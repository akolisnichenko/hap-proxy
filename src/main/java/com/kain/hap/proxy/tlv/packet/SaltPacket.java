package com.kain.hap.proxy.tlv.packet;

import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tools.Salt;
import com.kain.hap.proxy.tools.SrpPublicKey;

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
