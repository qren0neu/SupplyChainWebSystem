package com.qiren.manufacture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.qiren.manufacture.repository")
@SpringBootApplication
public class ManufactureApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManufactureApplication.class, args);
	}

}
