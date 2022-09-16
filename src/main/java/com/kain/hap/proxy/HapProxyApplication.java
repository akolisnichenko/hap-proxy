package com.kain.hap.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
@IntegrationComponentScan
public class HapProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(HapProxyApplication.class, args);
	}

}
