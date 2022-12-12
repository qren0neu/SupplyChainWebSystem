package com.qiren.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.RedisData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.qiren.common.tools.CommonUtils;
import com.qiren.common.tools.Role;
import com.qiren.portal.beans.UserBean;

@Controller()
@RequestMapping("/pages/login")
public class LoginController {

	@Autowired
	private RedisTemplate<String, UserBean> sessionRedisTemplate;

	@PostMapping(path = "/welcome/{name}")
	public @ResponseBody String welcome(@PathVariable String name) {
		return "Welcome, " + name;
	}

	@GetMapping(path = "/{role}/userLogin")
	public String login(@PathVariable String role) {
		if (Role.validRole(role)) {
			return CommonUtils.page("/login/login");
		}
		return CommonUtils.errorPage();
	}

}
