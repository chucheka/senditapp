package com.neulogics.senditapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;



@SpringBootApplication
@EnableJpaAuditing
public class SenditappApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SenditappApplication.class, args);
	}

}

