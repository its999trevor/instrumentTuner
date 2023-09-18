package com.tuner.newtuner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;


@SpringBootApplication(exclude = ThymeleafAutoConfiguration.class)
public class NewtunerApplication {
	

	public static void main(String[] args) {
        SpringApplication.run(NewtunerApplication.class, args);


	}
}
	
