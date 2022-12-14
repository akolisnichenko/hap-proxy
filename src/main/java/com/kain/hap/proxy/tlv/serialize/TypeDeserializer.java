package com.kain.hap.proxy.tlv.serialize;

import java.nio.ByteBuffer;

import com.kain.hap.proxy.tlv.ErrorCode;
import com.kain.hap.proxy.tlv.Method;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.Type;
import com.kain.hap.proxy.tlv.packet.DataPacket;
import com.kain.hap.proxy.tlv.packet.DeviceProofPacket;
import com.kain.hap.proxy.tlv.packet.EncryptedPacket;
import com.kain.hap.proxy.tlv.packet.ErrorPacket;
import com.kain.hap.proxy.tlv.packet.MethodPacket;
import com.kain.hap.proxy.tlv.packet.ProofPacket;
import com.kain.hap.proxy.tlv.packet.SaltPacket;
import com.kain.hap.proxy.tlv.type.Encrypted;
import com.kain.hap.proxy.tlv.type.Identifier;
import com.kain.hap.proxy.tlv.type.Proof;
import com.kain.hap.proxy.tlv.type.Salt;
import com.kain.hap.proxy.tlv.type.Signature;
import com.kain.hap.proxy.tlv.type.PublicKey;

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
		
		if (wrapper.contains(Type.ENCRYPTED_DATA.getValue())) {
			return new EncryptedPacket(State.toState(wrapper.getOne(Type.STATE.getValue())),
					new Encrypted(wrapper.get(Type.ENCRYPTED_DATA.getValue())));
		}
		if (wrapper.contains(Type.SIGNATURE.getValue())) {
			return new DataPacket(new Identifier(wrapper.get(Type.IDENTIFIER.getValue())),
					new PublicKey(wrapper.get(Type.PUBLIC_KEY.getValue())),
					new Signature(wrapper.get(Type.SIGNATURE.getValue())));
		}
		if (wrapper.contains(Type.SALT.getValue())) {
			return new SaltPacket(State.toState(wrapper.getOne(Type.STATE.getValue())),
					new PublicKey(wrapper.get(Type.PUBLIC_KEY.getValue())),
					new Salt(wrapper.get(Type.SALT.getValue())));
		}
		if (wrapper.contains(Type.PUBLIC_KEY.getValue())) {
			return new DeviceProofPacket(State.toState(wrapper.getOne(Type.STATE.getValue())),
					new Proof(wrapper.get(Type.PROOF.getValue())),
					new PublicKey(wrapper.get(Type.PUBLIC_KEY.getValue())));
		}
		if (wrapper.contains(Type.PROOF.getValue())) {
			return new ProofPacket(State.toState(wrapper.getOne(Type.STATE.getValue())),
					new Proof(wrapper.get(Type.PROOF.getValue())));
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
