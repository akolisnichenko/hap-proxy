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
import com.kain.hap.proxy.tlv.integration.PacketTransformer;
import com.kain.hap.proxy.tlv.serialize.PacketSerializer;
import com.kain.hap.proxy.tlv.serialize.TlvSerializer;

@Configuration
public class ServerConfig {
	@Autowired
	private PacketHandler packetHandler;
	@Autowired
	private PacketTransformer packetTransformer;
	
	@Bean
	public MessageChannel errorChannel() {
		return MessageChannels.direct().get();
	}
	
	@Bean
	public MessageChannel income() {
		return MessageChannels.direct().get();
	}
	
	@Bean
	public IntegrationFlow server() {
	    return IntegrationFlows.from(Tcp.inboundGateway(Tcp.netServer(12345)
	                            //.deserializer(new TlvDeserializer())
	    						//.deserializer(TcpCodecs.crlf())
	    						.deserializer(new PacketSerializer())
	                            .serializer(new TlvSerializer())
	                            .backlog(30)
	                            )
	                        .errorChannel(errorChannel())
	                        .replyTimeout(0)
	                        .id("tcpIn"))
	    		.transform(packetTransformer)
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
	@ServiceActivator(inputChannel="errorChannel")
	public MessageHandler loggingHandler() {
	    LoggingHandler loggingHandler = new LoggingHandler("ERROR");
	    loggingHandler.setLogExpressionString("payload.message");
	    return loggingHandler;
	}
}
