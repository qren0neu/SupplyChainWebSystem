package com.qiren.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EnableJpaRepositories("com.qiren.portal.entities")
@SpringBootApplication
@EnableRedisRepositories
public class SupplyChainPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupplyChainPortalApplication.class, args);
	}
}
