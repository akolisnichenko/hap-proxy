package com.kain.hap.proxy.tlv.packet;

import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.type.Proof;
import com.kain.hap.proxy.tlv.type.SrpPublicKey;

import lombok.Getter;

@Getter
public class DeviceProofPacket extends ProofPacket {
	
	private final SrpPublicKey publicKey;
	
	public DeviceProofPacket(State state, Proof proof, SrpPublicKey publicKey) {
		super(state, proof);
		this.publicKey = publicKey;
	}


}
