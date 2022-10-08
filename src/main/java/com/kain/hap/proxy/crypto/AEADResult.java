package com.kain.hap.proxy.crypto;

import java.util.Arrays;

import com.google.common.primitives.Bytes;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AEADResult {
	private byte[] encrypted;
	private byte[] tag;
	
	public AEADResult(byte[] rawData) {
		int borderIndex = rawData.length - 16;
		encrypted = Arrays.copyOfRange(rawData, 0, borderIndex);
		tag = Arrays.copyOfRange(rawData, borderIndex, rawData.length);
	}
	
	public byte[] getConcatenated() {
		return Bytes.concat(encrypted, tag);
	}
}
