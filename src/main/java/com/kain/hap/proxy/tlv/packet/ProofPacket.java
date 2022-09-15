package com.kain.hap.proxy.tlv.packet;

import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tools.SrpPublicKey;

import lombok.Getter;

@Getter
public class ProofPacket extends BasePacket {
	private final SrpPublicKey publicKey;
	private final byte[] proof; //TODO: replace by Object
	
	public ProofPacket(State state, SrpPublicKey key, byte[] proof) {
		super(state);
		this.proof = proof;
		this.publicKey = key;
	}

}
