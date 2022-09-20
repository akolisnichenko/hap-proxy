package com.kain.hap.proxy.tlv.serialize;

import java.nio.ByteBuffer;
import java.util.Arrays;

public abstract class GenericSerializer<T> {
	private static final int MAX_SIZE = 0xFF;
	public abstract Class<?> getTypeClass();
	public abstract byte[] serialize(T value);
	
	@SuppressWarnings("unchecked")
	public byte[] serializeObject(Object obj){
		return serialize ((T) obj);
	}
	
	protected byte[] writeTLV(byte type, byte[] value) {
		int repeatBlockNum = value.length / MAX_SIZE;
		int additonalBytes = 2 + repeatBlockNum * 2;
		int totalBytes = value.length + additonalBytes;
		ByteBuffer buf = ByteBuffer.allocate(totalBytes);
		int currentBlockIndex = 0;
		do {
			buf.put(type);
			int blockLength = repeatBlockNum > currentBlockIndex ? MAX_SIZE : value.length % MAX_SIZE;
			buf.put((byte)blockLength);
			int startIndex = currentBlockIndex * MAX_SIZE;
			buf.put(Arrays.copyOfRange(value, startIndex, startIndex + blockLength));
			currentBlockIndex++;
		}while (currentBlockIndex <= repeatBlockNum);
		return buf.array();
	} 
}
