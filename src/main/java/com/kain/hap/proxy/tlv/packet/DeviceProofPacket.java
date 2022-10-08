package com.kain.hap.proxy.tlv.packet;

import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.type.Proof;
import com.kain.hap.proxy.tlv.type.PublicKey;

import lombok.Getter;

@Getter
public class DeviceProofPacket extends ProofPacket {
	
	private final PublicKey publicKey;
	
	public DeviceProofPacket(State state, Proof proof, PublicKey publicKey) {
		super(state, proof);
		this.publicKey = publicKey;
	}


}
