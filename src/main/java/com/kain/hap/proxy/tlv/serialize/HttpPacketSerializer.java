package com.kain.hap.proxy.tlv.serialize;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.core.serializer.Serializer;

import com.google.common.primitives.Bytes;
import com.kain.hap.proxy.tlv.packet.HapResponse;

// replaced by BasePacketSerializer without error codes
@Deprecated
public class HttpPacketSerializer implements Serializer<HapResponse>{
	private static final TlvMapper MAPPER = TlvMapper.INSTANCE;
	private static final byte[] CRLF = "\r\n".getBytes();
	private static final byte[] COLON = ":".getBytes();
	private static final byte[] RESPONSE_PREFIX = "HTTP/1.1 ".getBytes();
	private static final byte[] LENGTH_HEADER = "Content-Length".getBytes();

	@Override
	public void serialize(HapResponse resonse, OutputStream outputStream) throws IOException {
		byte[] body = MAPPER.writeValue(resonse.getBody());
		byte[] headers = resonse.getHeaders()
				.entrySet()
				.stream()
				.map(e -> Bytes.concat(e.getKey().getBytes(), COLON, e.getValue().getBytes(), CRLF))
				.reduce(new byte[0], Bytes::concat);


		byte[] raw = Bytes.concat(RESPONSE_PREFIX, resonse.getCode().getBytes(), CRLF);
		raw = Bytes.concat(raw, headers,  LENGTH_HEADER, COLON, String.valueOf(body.length).getBytes(), CRLF);
		raw = Bytes.concat(raw, CRLF, body);

		outputStream.write(raw);
	}

}
