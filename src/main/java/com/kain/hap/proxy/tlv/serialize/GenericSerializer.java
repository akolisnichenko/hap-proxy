package com.kain.hap.proxy.tlv.serialize;

public interface GenericSerializer<T> {
	Class<?> getTypeClass();
	byte[] serialize(T value);
	
	@SuppressWarnings("unchecked")
	default byte[] serializeObject(Object obj){
		return serialize ((T) obj);
	}

}
