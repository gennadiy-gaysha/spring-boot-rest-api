package com.rest.api.crud;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrudApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

		System.setProperty("SECURITY_USERNAME", dotenv.get("SECURITY_USERNAME"));
		System.setProperty("SECURITY_PASSWORD", dotenv.get("SECURITY_PASSWORD"));

		SpringApplication.run(CrudApplication.class, args);
	}
}
