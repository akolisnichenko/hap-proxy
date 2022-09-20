package com.kain.hap.proxy.tlv.serialize;

import java.nio.ByteBuffer;

import com.kain.hap.proxy.tlv.ErrorCode;
import com.kain.hap.proxy.tlv.Method;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.Type;
import com.kain.hap.proxy.tlv.packet.ErrorPacket;
import com.kain.hap.proxy.tlv.packet.MethodPacket;
import com.kain.hap.proxy.tlv.packet.ProofPacket;
import com.kain.hap.proxy.tlv.type.SrpPublicKey;

public class TypeDeserializer {

	@SuppressWarnings("unchecked")
	public <T> T writeToObject(byte[] data) {
		return (T) parseToType(data);
	}

	private Object parseToType(byte[] data) {
		ByteBuffer buf = ByteBuffer.wrap(data);
		TlvWrapper wrapper = new TlvWrapper();
		while (buf.hasRemaining()) {
			byte type = buf.get();
			byte length = buf.get();
			
			byte[] value = new byte[length]; 
			buf.get(value);
			wrapper.add(type, value);
		}
		
		return convertToObject(wrapper);
		
	}

	private Object convertToObject(TlvWrapper wrapper) {
		if (wrapper.contains(Type.PROOF.getValue())) {
			return new ProofPacket(State.toState(wrapper.getOne(Type.STATE.getValue())),
					new SrpPublicKey(wrapper.get(Type.PUBLIC_KEY.getValue())),
					wrapper.get(Type.PROOF.getValue()));
		}

		if (wrapper.contains(Type.ERROR.getValue())) {
			return new ErrorPacket(State.toState(wrapper.getOne(Type.STATE.getValue())),
					ErrorCode.toError(wrapper.getOne(Type.ERROR.getValue())));
		}
		if (wrapper.contains(Type.METHOD.getValue())) {
			return new MethodPacket(State.toState(wrapper.getOne(Type.STATE.getValue())),
					Method.toMethod(wrapper.getOne(Type.METHOD.getValue())));
		}
		
		return null;
	}

}
