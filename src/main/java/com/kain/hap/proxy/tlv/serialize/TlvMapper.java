package com.kain.hap.proxy.tlv.serialize;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TlvMapper {
	
	private static final TypeSerializer typeSerializer = new TypeSerializer();
	private static final TypeDeserializer typeDesrializer = new TypeDeserializer();
	
	public static <T> byte[] writeValue(T value) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		addData(value.getClass(), value, stream);
		return stream.toByteArray();
	}
	
	private static void addData(Class<? extends Object> clazz, Object value, ByteArrayOutputStream stream) {
		if (clazz.getSuperclass() != null) {
			addData(clazz.getSuperclass(), value, stream);
		}
		for (Method m : clazz.getDeclaredMethods()) {
			try {
				// use only getters
				if (m.getName().startsWith("get") && m.canAccess(value)) {
					Object fieldValue = m.invoke(value); 
					if (fieldValue != null) {
						stream.write(typeSerializer.serialize(fieldValue));
					}
				}
			}
			catch(Exception ex) {
				log.error("Not available method {}", m.getName(), ex);
			}
		}
	}
	
	public static <T> T readPacket(byte[] data) {
		return typeDesrializer.writeToObject(data);
	}

}
