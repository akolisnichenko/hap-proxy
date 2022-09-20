package com.kain.hap.proxy.tlv.serialize;

import java.util.Map;

import com.google.common.collect.Maps;
import com.kain.hap.proxy.tlv.Method;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.Type;
import com.kain.hap.proxy.tlv.type.Salt;
import com.kain.hap.proxy.tlv.type.SrpPublicKey;

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
	
	class KeySerilizer extends GenericSerializer<SrpPublicKey> {
		@Override
		public byte[] serialize(SrpPublicKey key) {
			return writeTLV(Type.PUBLIC_KEY.getValue(), key.getKey());
		}

		@Override
		public Class<SrpPublicKey> getTypeClass() {
			return SrpPublicKey.class;
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
	
	
}
