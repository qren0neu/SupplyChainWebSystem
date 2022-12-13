package com.qiren.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EnableJpaRepositories("com.qiren.portal.repository")
@SpringBootApplication
@EnableRedisRepositories
public class SupplyChainPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupplyChainPortalApplication.class, args);
	}
}
