package com.kain.hap.proxy.tlv.packet;

import com.kain.hap.proxy.tlv.type.Identifier;
import com.kain.hap.proxy.tlv.type.PublicKey;
import com.kain.hap.proxy.tlv.type.Signature;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DataPacket {
	private final Identifier identifier;
	private final PublicKey key;
	private final Signature signature;

}
