package com.revplay.revplay_catalog_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {
		"com.revplay.revplay_catalog_service",
		"com.revplay.revplay_security"
})
public class RevplayCatalogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RevplayCatalogServiceApplication.class, args);
	}

}
