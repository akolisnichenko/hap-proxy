package com.kain.hap.proxy.tlv.serialize;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TlvMapper {
	
	private final TypeSerializer typeSerializer = new TypeSerializer();
	private final TypeDeserializer typeDesrializer = new TypeDeserializer();
	
	public static final TlvMapper INSTANCE = new TlvMapper();
	
	public <T> byte[] writeValue(T value) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		addData(value.getClass(), value, stream);
		return stream.toByteArray();
	}
	
	private void addData(Class<? extends Object> clazz, Object value, ByteArrayOutputStream stream) {
		if (clazz.getSuperclass() != null) {
			addData(clazz.getSuperclass(), value, stream);
		}
		for (Method m : clazz.getDeclaredMethods()) {
			try {
				if (m.canAccess(value) && m.getName().startsWith("get")) {
					stream.write(typeSerializer.serialize(m.invoke(value)));
				}
			}
			catch(Exception ex) {
				log.error("Not available method {}", m.getName(), ex);
			}
		}
	}
	
	public <T> T readPacket(byte[] data) {
		return typeDesrializer.writeToObject(data);
	}

}
