package com.kain.hap.proxy.tlv.packet;

import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.type.Proof;

import lombok.Getter;

@Getter
public class ProofPacket extends BasePacket {

	private final Proof proof;
	
	public ProofPacket(State state, Proof proof) {
		super(state);
		this.proof = proof;
	}

}
