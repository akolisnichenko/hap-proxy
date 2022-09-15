package com.kain.hap.proxy.tlv.serialize;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.serializer.Deserializer;


public class TlvDeserializer implements Deserializer<Object>{
	
	private final TlvMapper mapper;
	
	public TlvDeserializer() {
		mapper = new TlvMapper();
	}

	public TlvDeserializer(TlvMapper mapper) {
		this.mapper = mapper;
	}
	
	@Override
	public Object deserialize(InputStream inputStream) throws IOException {
		//inputStream.
		
		return mapper.writeValue(null);
	}

}
