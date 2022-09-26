package com.kain.hap.proxy.tlv.serialize.client;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.core.serializer.Serializer;

import com.google.common.primitives.Bytes;
import com.kain.hap.proxy.tlv.packet.HapRequest;
import com.kain.hap.proxy.tlv.serialize.TlvMapper;

public class Tlv8RequestSerializer implements Serializer<HapRequest>{
	private static final TlvMapper MAPPER = TlvMapper.INSTANCE;
	private static final byte[] CRLF = "\r\n".getBytes();
	private static final byte[] COLON = ":".getBytes();
	//TODO: replace by correct endpoint from request  
	private static final byte[] REQUEST_HEAD = "POST /pair-setup HTTP/1.1".getBytes();
	private static final byte[] LENGTH_HEADER = "Content-Length".getBytes();

	@Override
	public void serialize(HapRequest request, OutputStream outputStream) throws IOException {
		byte[] body = MAPPER.writeValue(request.getBody());
		byte[] headers =  Stream.of(Map. of("Content-Type", "application/pairing+tlv8"), request.getHeaders())
				.flatMap(m -> m.entrySet().stream())
				.map(e -> Bytes.concat(e.getKey().getBytes(), COLON, e.getValue().getBytes(), CRLF))
				.reduce(new byte[0], Bytes::concat);


		byte[] raw = Bytes.concat(REQUEST_HEAD, CRLF);
		raw = Bytes.concat(raw, headers,  LENGTH_HEADER, COLON, String.valueOf(body.length).getBytes(), CRLF);
		raw = Bytes.concat(raw, CRLF, body);

		outputStream.write(raw);
	}

}
