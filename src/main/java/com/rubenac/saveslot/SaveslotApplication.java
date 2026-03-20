package com.rubenac.saveslot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SaveslotApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaveslotApplication.class, args);
	}
}
