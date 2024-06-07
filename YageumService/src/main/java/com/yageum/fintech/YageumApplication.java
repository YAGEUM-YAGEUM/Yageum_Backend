package com.yageum.fintech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class YageumApplication {
	public static void main(String[] args) {
		SpringApplication.run(YageumApplication.class, args);
	}
}
