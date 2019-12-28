package com.daichi703n.capanda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableScheduling
// @EnableWebSecurity
public class CapandaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapandaApplication.class, args);
	}

}
