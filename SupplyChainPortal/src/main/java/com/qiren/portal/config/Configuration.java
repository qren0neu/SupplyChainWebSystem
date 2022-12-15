package com.qiren.portal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

import com.qiren.portal.beans.UserBean;

@org.springframework.context.annotation.Configuration
public class Configuration {

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate template = new RestTemplate();
		return template;
	}
}
