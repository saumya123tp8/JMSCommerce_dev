package com.example.JMSCommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JMSCommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JMSCommerceApplication.class, args);
	}

}
