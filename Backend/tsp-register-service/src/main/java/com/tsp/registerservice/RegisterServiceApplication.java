package com.tsp.registerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RegisterServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(RegisterServiceApplication.class, args);
	}
}
