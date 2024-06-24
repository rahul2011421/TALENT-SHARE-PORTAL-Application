package com.tsp.mentorshipService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MentorshipServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MentorshipServiceApplication.class, args);
	}

}
