package com.qiren.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.qiren.portal.entities")
@SpringBootApplication
public class SupplyChainPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupplyChainPortalApplication.class, args);
	}
 
}
