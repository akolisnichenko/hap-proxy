package com.kain.hap.proxy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.messaging.MessageChannel;

import com.kain.hap.proxy.tlv.serialize.BasePacketSerializer;
import com.kain.hap.proxy.tlv.serialize.HttpPacketDeserializer;

@Configuration
public class ClientConfig {
	@Value("${real.accesssory.host:localhost}")
	private String realAccessoryHost;
	@Value("${real.accesssory.port}")
	private int realAccessoryPort;
	
	
	@Bean
	public MessageChannel outcome() {
		return MessageChannels.direct().get();
	}

	
	@Bean
	public IntegrationFlow device() {
		return IntegrationFlows.from(outcome())
				.handle(Tcp.outboundGateway(Tcp.netClient(realAccessoryHost, realAccessoryPort)
								.deserializer(new HttpPacketDeserializer())
								.serializer(new BasePacketSerializer()))
						)
				.get();
	}


}
