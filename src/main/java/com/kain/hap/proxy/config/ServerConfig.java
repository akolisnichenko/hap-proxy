package com.kain.hap.proxy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.kain.hap.proxy.tlv.integration.PacketHandler;
import com.kain.hap.proxy.tlv.serialize.BasePacketSerializer;
import com.kain.hap.proxy.tlv.serialize.HttpPacketDeserializer;

@Configuration
public class ServerConfig {
	@Autowired
	private PacketHandler packetHandler;
	
	@Bean
	public MessageChannel errorChannel() {
		return MessageChannels.direct().get();
	}

	@Bean
	public MessageChannel income() {
		return MessageChannels.direct().get();
	}
	
	@Bean
	public MessageChannel outcome() {
		return MessageChannels.direct().get();
	}

	@Bean
	public IntegrationFlow accessory() {
		return IntegrationFlows
				.from(Tcp
						.inboundGateway(Tcp.netServer(12345)
								.deserializer(new HttpPacketDeserializer())
								.serializer(new BasePacketSerializer())
								.backlog(30))
						.errorChannel(errorChannel()).id("tcpIn"))
				.channel(income())
				.get();
	}

	@Bean
	public IntegrationFlow incomePacketHandler() {
		return IntegrationFlows.from(income())
				.handle(packetHandler)
				.get();
	}

	@Bean
	@ServiceActivator(inputChannel = "errorChannel")
	public MessageHandler loggingHandler() {
		LoggingHandler loggingHandler = new LoggingHandler("ERROR");
		loggingHandler.setLogExpressionString("payload.message");
		return loggingHandler;
	}
}
