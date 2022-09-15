package com.kain.hap.proxy.tlv;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Type {
	METHOD((byte)0x00),
	IDENTIFIER((byte)0x01),
	SALT((byte)0x02),
	PUBLIC_KEY((byte)0x03),
	PROOF((byte)0x04),
	ENCRYPTED_DATA((byte)0x05),
	STATE((byte)0x06),
	ERROR((byte)0x07),
	RETRY_DELAY((byte)0x08),
	CERTIFICATE((byte)0x09),
	SIGNATURE((byte)0x0a),
	PERMISSIONS((byte)0x0b),
	FRAGMENT_DATA((byte)0x0c),
	FRAGMENT_LAST((byte)0x0d),
	FLAGS((byte)0x13),
	SEPARATOR((byte)0xff);
	

	private final byte value; 
	
}
