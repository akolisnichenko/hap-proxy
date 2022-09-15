package com.kain.hap.proxy.tlv.serialize;

import java.nio.ByteBuffer;
import java.util.Map;

import com.google.common.collect.Maps;
import com.kain.hap.proxy.tlv.Type;
import com.kain.hap.proxy.tlv.packet.BasePacket;
import com.kain.hap.proxy.tlv.packet.MethodPacket;
import com.kain.hap.proxy.tools.Salt;

public class TypeSerializer {

	private Map<Class<?>, GenericSerializer<?>> serilizers = Maps.newHashMap();

	public byte[] serialize(Object obj) {
		// TODO: set default serializer
		GenericSerializer<?> ser = serilizers.get(obj.getClass());
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
		register(new BasePacketSerilizer());
		register(new MethodPacketSerilizer());
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

	class BasePacketSerilizer implements GenericSerializer<BasePacket> {
		public byte[] serialize(BasePacket base) {
			ByteBuffer buf = ByteBuffer.allocate(3);
			buf.put(Type.STATE.getValue());
			buf.put((byte) 0x01);
			buf.put(base.getState().getValue());
			return buf.array();
		}

		@Override
		public Class<BasePacket> getTypeClass() {
			return BasePacket.class;
		}
	}

	class MethodPacketSerilizer implements GenericSerializer<MethodPacket> {
		public byte[] serialize(MethodPacket method) {
			ByteBuffer buf = ByteBuffer.allocate(3);
			buf.put(Type.METHOD.getValue());
			buf.put((byte) 0x01);
			buf.put(method.getMethod().getValue());
			return buf.array();
		}

		@Override
		public Class<BasePacket> getTypeClass() {
			return BasePacket.class;
		}
	}
}
