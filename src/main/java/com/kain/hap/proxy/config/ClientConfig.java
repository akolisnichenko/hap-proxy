package com.kain.hap.proxy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.messaging.MessageChannel;

import com.kain.hap.proxy.tlv.integration.RequestHandler;
import com.kain.hap.proxy.tlv.integration.ResponseHandler;
import com.kain.hap.proxy.tlv.serialize.BasePacketSerializer;
import com.kain.hap.proxy.tlv.serialize.HttpPacketDeserializer;
import com.kain.hap.proxy.tlv.serialize.client.Tlv8RequestSerializer;
import com.kain.hap.proxy.tlv.serialize.client.Tlv8ResponseDeserializer;

@Configuration
public class ClientConfig {
	@Value("${real.accesssory.host:localhost}")
	private String realAccessoryHost;
	@Value("${real.accesssory.port}")
	private int realAccessoryPort;
	
	
	@Autowired
	private ResponseHandler responseHandler;

	@Bean
	public MessageChannel outcome() {
		return MessageChannels.direct().get();
	}
	
	@Bean
	public MessageChannel response() {
		return MessageChannels.direct().get();
	}

	@Bean
	public IntegrationFlow device() {
		return IntegrationFlows.from(outcome())
				.handle(Tcp.outboundGateway(
						Tcp.netClient(realAccessoryHost, realAccessoryPort)
							.soKeepAlive(true)
							.leaveOpen(true)
							.deserializer(new Tlv8ResponseDeserializer())
							.serializer(new Tlv8RequestSerializer())
							.singleUseConnections(false)
							)
						)
				.channel(response())
				.get();
	}

	@Bean
	public IntegrationFlow accessoryResResponseHandler() {
		return IntegrationFlows.from(response())
				.handle(responseHandler)
				.get();
	}

}
