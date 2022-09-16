package com.kain.hap.proxy.tlv.serialize;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.core.serializer.Serializer;

public class TlvSerializer implements Serializer<Object>{
	private final TlvMapper mapper = TlvMapper.INSTANCE;

	@Override
	public void serialize(Object object, OutputStream outputStream) throws IOException {
		outputStream.write(mapper.writeValue(object));
	}

}
