package com.kain.hap.proxy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.messaging.MessageChannel;

import com.kain.hap.proxy.tlv.integration.ResponseHandler;
import com.kain.hap.proxy.tlv.serialize.client.Tlv8RequestSerializer;
import com.kain.hap.proxy.tlv.serialize.client.Tlv8ResponseDeserializer;

@Configuration
public class ClientConfig {
	@Value("${real.accessory.host:localhost}")
	private String realAccessoryHost;
	@Value("${real.accessory.port}")
	private int realAccessoryPort;
	
	@Autowired
	private TaskExecutor taskExecutor;
	
	@Autowired
	private ResponseHandler responseHandler;

	@Bean
	public MessageChannel outcome() {
		return MessageChannels.executor(taskExecutor).get();
	}
	
	@Bean
	public MessageChannel response() {
		return MessageChannels.direct().get();
	}
/*	
	@Bean
	public TcpNetClientConnectionFactory cf() {
		TcpNetClientConnectionFactory cf = new TcpNetClientConnectionFactory(realAccessoryHost, realAccessoryPort);
		cf.setSingleUse(false);
		cf.setDeserializer(new Tlv8ResponseDeserializer());
		cf.setSerializer(new Tlv8RequestSerializer());
		cf.setSoTimeout(10000);
		return cf;
	}

	
	@Bean
	@ServiceActivator(inputChannel = "device")
	public TcpOutboundGateway deviceGateway() {
		TcpOutboundGateway outGate = new TcpOutboundGateway();
		outGate.setConnectionFactory(cf());
		outGate.setOutputChannel(response());
		outGate.setRemoteTimeout(20000);
		return outGate;
	}

	@Bean
	public IntegrationFlow mainCommunicationFlow() {
		return IntegrationFlows.from(outcome()).channel("device").get();
	}
*/

	
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
				.channel(outcome())
				.get();
	}

}
