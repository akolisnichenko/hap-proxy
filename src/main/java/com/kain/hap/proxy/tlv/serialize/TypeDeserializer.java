package com.kain.hap.proxy.tlv.serialize;

import java.nio.ByteBuffer;

import com.kain.hap.proxy.tlv.ErrorCode;
import com.kain.hap.proxy.tlv.Method;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.Type;
import com.kain.hap.proxy.tlv.packet.Packet;
import com.kain.hap.proxy.tlv.type.Encrypted;
import com.kain.hap.proxy.tlv.type.Identifier;
import com.kain.hap.proxy.tlv.type.Proof;
import com.kain.hap.proxy.tlv.type.PublicKey;
import com.kain.hap.proxy.tlv.type.Salt;
import com.kain.hap.proxy.tlv.type.Signature;

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
			int length = Byte.toUnsignedInt(buf.get());
			
			byte[] value = new byte[length]; 
			buf.get(value);
			wrapper.add(type, value);
		}
		return convertToObject(wrapper);
	}

	private Object convertToObject(TlvWrapper wrapper) {
		Packet.PacketBuilder builder = Packet.builder(); 
		
		if (wrapper.contains(Type.ENCRYPTED_DATA.getValue())) {
			builder.encrypted(new Encrypted(wrapper.get(Type.ENCRYPTED_DATA.getValue())));
		}
		if (wrapper.contains(Type.STATE.getValue())) {
			builder.state(State.toState(wrapper.getOne(Type.STATE.getValue())));
		}
		if (wrapper.contains(Type.SIGNATURE.getValue())) {
			builder.signature(new Signature(wrapper.get(Type.SIGNATURE.getValue())));
		}
		if (wrapper.contains(Type.IDENTIFIER.getValue())){
			builder.id(new Identifier(wrapper.get(Type.IDENTIFIER.getValue())));
		}
		if (wrapper.contains(Type.PUBLIC_KEY.getValue())) {
			builder.key(new PublicKey(wrapper.get(Type.PUBLIC_KEY.getValue())));
		}
		if (wrapper.contains(Type.SALT.getValue())) {
			builder.salt(new Salt(wrapper.get(Type.SALT.getValue())));
		}
		if (wrapper.contains(Type.METHOD.getValue())) {
			builder.method(Method.toMethod(wrapper.getOne(Type.METHOD.getValue())));
		}
		if (wrapper.contains(Type.ERROR.getValue())) {
			builder.error(ErrorCode.toError(wrapper.getOne(Type.ERROR.getValue())));
		}
		if (wrapper.contains(Type.PROOF.getValue())) {
			builder.proof(new Proof(wrapper.get(Type.PROOF.getValue())));
		}
		return builder.build();
	}

}
