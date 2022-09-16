package com.kain.hap.proxy.tlv.serialize;

import java.nio.ByteBuffer;
import java.util.Map;

import com.google.common.collect.Maps;
import com.kain.hap.proxy.tlv.Method;
import com.kain.hap.proxy.tlv.State;
import com.kain.hap.proxy.tlv.Type;
import com.kain.hap.proxy.tools.Salt;

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
	}

	class SaltSerilizer implements GenericSerializer<Salt> {
		@Override
		public byte[] serialize(Salt salt) {
			ByteBuffer buf = ByteBuffer.allocate(salt.getSalt().length + 2);
			buf.put(Type.SALT.getValue());
			buf.put((byte) salt.getSalt().length);
			buf.put(salt.getSalt());
			return buf.array();
		}

		@Override
		public Class<Salt> getTypeClass() {
			return Salt.class;
		}
	}

	class StateSerilizer implements GenericSerializer<State> {
		public byte[] serialize(State state) {
			ByteBuffer buf = ByteBuffer.allocate(3);
			buf.put(Type.STATE.getValue());
			buf.put((byte) 0x01);
			buf.put(state.getValue());
			return buf.array();
		}

		@Override
		public Class<State> getTypeClass() {
			return State.class;
		}
	}

	class MethodSerilizer implements GenericSerializer<Method> {
		public byte[] serialize(Method method) {
			ByteBuffer buf = ByteBuffer.allocate(3);
			buf.put(Type.METHOD.getValue());
			buf.put((byte) 0x01);
			buf.put(method.getValue());
			return buf.array();
		}

		@Override
		public Class<Method> getTypeClass() {
			return Method.class;
		}
	}
}
