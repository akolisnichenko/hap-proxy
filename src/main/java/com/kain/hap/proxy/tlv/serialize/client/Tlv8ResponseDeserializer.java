package com.kain.hap.proxy.tlv.serialize.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.core.serializer.Deserializer;
import org.springframework.integration.ip.tcp.serializer.SoftEndOfStreamException;
import org.springframework.integration.ip.tcp.serializer.TcpDeserializationExceptionEvent;

import com.kain.hap.proxy.tlv.packet.HapResponse;
import com.kain.hap.proxy.tlv.serialize.TlvMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Tlv8ResponseDeserializer implements Deserializer<HapResponse>, ApplicationEventPublisherAware {

	private static final String LENGTH_HEADER = "Content-Length:";
	private static final String RESPONSE_PREFIX = "HTTP/1.1";

	private static final int DEFAULT_MAX_MESSAGE_SIZE = 2048;

	private ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	//TODO: refactor to decrease complexity
	@Override
	public HapResponse deserialize(InputStream inputStream) throws IOException {
		// TODO: check max size of request;
		byte[] buffer = new byte[DEFAULT_MAX_MESSAGE_SIZE];
		int n = 0;
		int bite;
		int available = inputStream.available();
		int bodyLength = 0;
		log.debug("Available to read: {}", available);
		boolean duplicatedLf = false;

		HapResponse response = new HapResponse();

		try {
			while (true) {
				bite = inputStream.read();
				if (bite < 0 && n == 0) {
					throw new SoftEndOfStreamException("Stream closed between payloads");
				}
				checkClosure(bite);
				if (n > 0 && bite == '\n' && buffer[n - 1] == '\r') {
					if (duplicatedLf) {
						// start read body
						byte[] body = new byte[bodyLength];
						inputStream.read(body, 0, bodyLength);
						response.setBody(TlvMapper.readPacket(body));
						break;
					}
					duplicatedLf = true;
					String header = new String(buffer).trim();
					if (header.startsWith(LENGTH_HEADER)) {
						bodyLength = Integer.valueOf(header.substring(LENGTH_HEADER.length()).trim());
					}
					// get request method and endpoint
					if (header.startsWith(RESPONSE_PREFIX)) {
						String[] splited = header.split(" ", 2);
						response.setCode(splited[1]);
					} else {
						response.addHeader(header);
					}

					Arrays.fill(buffer, (byte) 0);
					n = 0;
					continue;
				} else {
					if (bite != '\r') {
						duplicatedLf = false;
					}
				}

				buffer[n++] = (byte) bite;
				if (n >= DEFAULT_MAX_MESSAGE_SIZE) {
					throw new IOException(
							"Terminator CRLF not found before max message length: " + DEFAULT_MAX_MESSAGE_SIZE);
				}
			}
		} catch (SoftEndOfStreamException e) { // NOSONAR catch and throw
			throw e; // it's an IO exception and we don't want an event for this
		} catch (IOException | RuntimeException ex) {
			publishEvent(ex, buffer, n);
			throw ex;
		}
		return response;
	}

	protected void checkClosure(int bite) throws IOException {
		if (bite < 0) {
			log.debug("Socket closed during message assembly");
			throw new IOException("Socket closed during message assembly");
		}
	}

	protected void publishEvent(Exception cause, byte[] buffer, int offset) {
		TcpDeserializationExceptionEvent event = new TcpDeserializationExceptionEvent(this, cause, buffer, offset);
		if (this.applicationEventPublisher != null) {
			this.applicationEventPublisher.publishEvent(event);
		} else {
			log.trace("No event publisher for {}", event);
		}
	}
}
