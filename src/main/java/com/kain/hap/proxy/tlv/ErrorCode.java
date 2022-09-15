package com.kain.hap.proxy.tlv;


public enum ErrorCode {
	NA,
	UNKNOWN,
	AUTHENTICATION,
	BACKOFF,
	MAX_PEERS,
	MAX_TRIES,
	UNAVAILABLE,
	BUSY,
	RESERVED;   //0x08-0xFF

}
