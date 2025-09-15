package com.petcare.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.petcare.back.domain.entity")
@EnableJpaRepositories("com.petcare.back.repository")
public class BackApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);}

}
