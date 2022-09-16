package com.kain.hap.proxy.tlv.integration;

import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.stereotype.Component;

import com.kain.hap.proxy.tlv.packet.BasePacket;
import com.kain.hap.proxy.tlv.serialize.TlvMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class PacketTransformer implements GenericTransformer<byte[], BasePacket>{
	private final TlvMapper mapper = TlvMapper.INSTANCE; 

	@Override
	public BasePacket transform(byte[] source) {
		BasePacket packet = mapper.readPacket(source);
		log.debug("here is converted packet: {}", packet);
		return packet;
	}

}
