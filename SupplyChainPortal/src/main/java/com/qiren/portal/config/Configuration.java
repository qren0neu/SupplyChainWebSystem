package com.qiren.portal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.qiren.portal.beans.UserBean;

@org.springframework.context.annotation.Configuration
public class Configuration {

	@Bean
	public RedisTemplate<String, UserBean> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, UserBean> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

}
