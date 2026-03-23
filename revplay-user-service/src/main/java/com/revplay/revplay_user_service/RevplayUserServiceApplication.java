package com.revplay.revplay_user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RevplayUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RevplayUserServiceApplication.class, args);
    }

}
