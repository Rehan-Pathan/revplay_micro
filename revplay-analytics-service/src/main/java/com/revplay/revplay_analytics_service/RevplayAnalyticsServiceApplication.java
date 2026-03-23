package com.revplay.revplay_analytics_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {
		"com.revplay.revplay_analytics_service",
		"com.revplay.revplay_security"
})
public class RevplayAnalyticsServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(RevplayAnalyticsServiceApplication.class, args);
	}

}
