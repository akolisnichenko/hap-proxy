package com.kain.hap.proxy.tlv.packet;

import com.kain.hap.proxy.tlv.ErrorCode;
import com.kain.hap.proxy.tlv.Method;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.type.Encrypted;
import com.kain.hap.proxy.tlv.type.Identifier;
import com.kain.hap.proxy.tlv.type.Proof;
import com.kain.hap.proxy.tlv.type.PublicKey;
import com.kain.hap.proxy.tlv.type.Salt;
import com.kain.hap.proxy.tlv.type.Signature;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Packet {
	private State state;
	private PublicKey key;
	private Encrypted encrypted;
	private Salt salt;
	private ErrorCode error;
	private Proof proof;
	private Identifier id;
	private Method method; 
	private Signature signature;
	
	//TODO: add flags
	

}
