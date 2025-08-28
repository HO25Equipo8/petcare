package com.petcare.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);

        // Added lines for testing
        System.out.println("PETCARE_GOOGLE_CLIENT_ID: " + System.getenv("PETCARE_GOOGLE_CLIENT_ID"));
        System.out.println("PETCARE_GOOGLE_CLIENT_SECRET: " + (System.getenv("PETCARE_GOOGLE_CLIENT_SECRET") != null ? "***PRESENT***" : "***MISSING***"));
	}

}
