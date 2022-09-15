package com.kain.hap.proxy.tlv.serialize;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TlvMapper {
	private final TypeSerializer typeSerializer = new TypeSerializer();
	
	public <T> byte[] writeValue(T value) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		addData(value.getClass(), value, stream);
		return stream.toByteArray();
	}
	
	private void addData(Class<? extends Object> clazz, Object value, ByteArrayOutputStream stream) {
		addData(clazz.getSuperclass(), value, stream);
		for (Field f : clazz.getDeclaredFields()) {
			try {
				stream.write(typeSerializer.serialize(f.get(value)));
			}
			catch(Exception ex) {
				log.error("Not available property {}", f.getName(), ex);
			}
		}
	}
	
	public <T> T readPacket(byte[] data) {
		return null;
	}

}
