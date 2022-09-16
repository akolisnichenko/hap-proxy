package com.kain.hap.proxy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.integration.ip.tcp.serializer.TcpCodecs;
import org.springframework.messaging.MessageChannel;

import com.kain.hap.proxy.tlv.serialize.PacketSerializer;
import com.kain.hap.proxy.tlv.serialize.TlvDeserializer;
import com.kain.hap.proxy.tlv.serialize.TlvSerializer;

@Configuration
public class ServerConfig {
	
	
	@Bean
	public MessageChannel errorChannel() {
		return MessageChannels.direct().get();
	}
	
	@Bean
	public IntegrationFlow server() {
	    return IntegrationFlows.from(Tcp.inboundAdapter(Tcp.netServer(12345)
	                            //.deserializer(new TlvDeserializer())
	    						//.deserializer(TcpCodecs.crlf())
	    						.deserializer(new PacketSerializer())
	                            .serializer(new TlvSerializer())
	                            .backlog(30))
	                        .errorChannel(errorChannel())
	                        .id("tcpIn"))
	            .transform(Transformers.objectToString())
	            .channel("tcpInbound")
	            .get();
	}
}
