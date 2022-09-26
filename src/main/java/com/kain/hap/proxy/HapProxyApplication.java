package com.kain.hap.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@IntegrationComponentScan
@EnableWebMvc
public class HapProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(HapProxyApplication.class, args);
	}

}
