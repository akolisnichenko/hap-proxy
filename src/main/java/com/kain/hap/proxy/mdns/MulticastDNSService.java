package com.kain.hap.proxy.mdns;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MulticastDNSService {
	private JmDNS jmdns;

	// TODO: here will be settings to override original mDNS data

	public void start() {
		try {
			jmdns = JmDNS.create(InetAddress.getLocalHost());
//			jmdns.addServiceListener("_hap._tcp.local.", new MulticastDNSDiscoveryService());

			Map<String, String> params = Map.of("id", "AA:AA:AA:AA:AA:AA");
			ServiceInfo serviceInfo = ServiceInfo.create("_http._tcp.local.", "happroxy", 12345, 0, 0, true, params);
			jmdns.registerService(serviceInfo);
		} catch (IOException e) {
			log.error("mDNS wasn't created! ", e);
		}
	}
}
