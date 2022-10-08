package com.kain.hap.proxy.tlv.serialize;

import java.util.Map;

import com.google.common.collect.Maps;
import com.kain.hap.proxy.tlv.ErrorCode;
import com.kain.hap.proxy.tlv.Method;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.Type;
import com.kain.hap.proxy.tlv.type.Encrypted;
import com.kain.hap.proxy.tlv.type.Identifier;
import com.kain.hap.proxy.tlv.type.Proof;
import com.kain.hap.proxy.tlv.type.PublicKey;
import com.kain.hap.proxy.tlv.type.Salt;
import com.kain.hap.proxy.tlv.type.Signature;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TypeSerializer {

	private Map<Class<?>, GenericSerializer<?>> serilizers = Maps.newHashMap();

	public byte[] serialize(Object obj) {
		GenericSerializer<?> ser = serilizers.get(obj.getClass());
		if (ser == null) {
			log.warn("No serializer for type {}", obj.getClass());
			return new byte[0];
		}
		return ser.serializeObject(obj);
	}

	public void register(GenericSerializer<?> serializer) {
		serilizers.put(serializer.getTypeClass(), serializer);
	}

	public void unregister(Class<?> typeClass) {
		serilizers.remove(typeClass);
	}

	public TypeSerializer() {
		register(new SaltSerilizer());
		register(new StateSerilizer());
		register(new MethodSerilizer());
		register(new KeySerilizer());
		register(new ProofSerilizer());
		register(new EncryptSerializer());
		register(new ErrorCodeSerilizer());
		register(new IdentifierSerializer());
		register(new SignatureSerializer());
	}
	
	class SaltSerilizer extends GenericSerializer<Salt> {
		@Override
		public byte[] serialize(Salt salt) {
			return writeTLV(Type.SALT.getValue(), salt.getSalt());
		}

		@Override
		public Class<Salt> getTypeClass() {
			return Salt.class;
		}
	}
	
	class KeySerilizer extends GenericSerializer<PublicKey> {
		@Override
		public byte[] serialize(PublicKey key) {
			return writeTLV(Type.PUBLIC_KEY.getValue(), key.getKey());
		}

		@Override
		public Class<PublicKey> getTypeClass() {
			return PublicKey.class;
		}
	}

	class StateSerilizer extends GenericSerializer<State> {
		public byte[] serialize(State state) {
			return writeTLV(Type.STATE.getValue(), new byte[] {state.getValue()}) ;
		}

		@Override
		public Class<State> getTypeClass() {
			return State.class;
		}
	}

	class MethodSerilizer extends GenericSerializer<Method> {
		public byte[] serialize(Method method) {
			return writeTLV(Type.METHOD.getValue(), new byte[] {method.getValue()}) ;
		}

		@Override
		public Class<Method> getTypeClass() {
			return Method.class;
		}
	}
	
	class ErrorCodeSerilizer extends GenericSerializer<ErrorCode> {
		public byte[] serialize(ErrorCode code) {
			return writeTLV(Type.ERROR.getValue(), new byte[] {(byte)code.ordinal()}) ;
		}

		@Override
		public Class<ErrorCode> getTypeClass() {
			return ErrorCode.class;
		}
	}
	
	class ProofSerilizer extends GenericSerializer<Proof> {
		public byte[] serialize(Proof proof) {
			return writeTLV(Type.PROOF.getValue(), proof.getProof()) ;
		}

		@Override
		public Class<Proof> getTypeClass() {
			return Proof.class;
		}
	}
	
	class EncryptSerializer extends GenericSerializer<Encrypted> {
		public byte[] serialize(Encrypted val) {
			return writeTLV(Type.ENCRYPTED_DATA.getValue(), val.getArray()) ;
		}

		@Override
		public Class<Encrypted> getTypeClass() {
			return Encrypted.class;
		}
	}
	
	class IdentifierSerializer extends GenericSerializer<Identifier> {
		public byte[] serialize(Identifier val) {
			return writeTLV(Type.IDENTIFIER.getValue(), val.getId()) ;
		}

		@Override
		public Class<Identifier> getTypeClass() {
			return Identifier.class;
		}
	}
	
	class SignatureSerializer extends GenericSerializer<Signature> {
		public byte[] serialize(Signature val) {
			return writeTLV(Type.SIGNATURE.getValue(), val.getValue()) ;
		}

		@Override
		public Class<Signature> getTypeClass() {
			return Signature.class;
		}
	}
	
}
