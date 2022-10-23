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
	private static final byte[] CRLF = "\r\n".getBytes();
	private static final byte[] COLON = ":".getBytes();
	private static final String REQUEST_HEAD_TEMPLATE = "POST %s HTTP/1.1";
	private static final byte[] LENGTH_HEADER = "Content-Length".getBytes();

	@Override
	public void serialize(HapRequest request, OutputStream outputStream) throws IOException {
		byte[] body = TlvMapper.writeValue(request.getBody());
		byte[] headers =  Stream.of(Map. of("Content-Type", "application/pairing+tlv8"), request.getHeaders())
				.flatMap(m -> m.entrySet().stream())
				.map(e -> Bytes.concat(e.getKey().getBytes(), COLON, e.getValue().getBytes(), CRLF))
				.reduce(new byte[0], Bytes::concat);

		byte[] requestHead = REQUEST_HEAD_TEMPLATE.formatted(request.getEndpoint()).getBytes();
		byte[] raw = Bytes.concat(requestHead, CRLF);
		raw = Bytes.concat(raw, headers,  LENGTH_HEADER, COLON, String.valueOf(body.length).getBytes(), CRLF);
		raw = Bytes.concat(raw, CRLF, body);
		outputStream.write(raw);
	}

}
