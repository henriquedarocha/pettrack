package com.pettrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PettrackApplication {

	public static void main(String[] args) {

		SpringApplication.run(PettrackApplication.class, args);

	}

}