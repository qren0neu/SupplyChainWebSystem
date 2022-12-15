package com.qiren.distributor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.qiren.distributor.repository")
@SpringBootApplication
public class DistributorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistributorApplication.class, args);
	}

}
