package com.qiren.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiren.common.response.CommonResponse;
import com.qiren.common.tools.CommonUtils;
import com.qiren.common.tools.Constants;
import com.qiren.common.tools.Logger;
import com.qiren.common.tools.Role;

@Controller
public class WelcomeController {
	
	@GetMapping(path = "/")
	public String welcomeMain() {
		Logger.log(this, "welcomeMain");
		return "index.html";
	}

	@GetMapping(path = "/perspective/{role}")
	public String welcomeMainForRoles(@PathVariable String role) {
		Logger.log(this, "welcomeMainForRoles");
		if (Role.validRole(role)) {
			return "index.html";
		}
		return CommonUtils.errorPage();
	}
	
	@GetMapping(path = "/welcome")
	public String welcome() {
		Logger.log(this, "getWelcome");
		return "welcome/welcome";
	}
	
	@PostMapping(path = "/welcome")
	public @ResponseBody CommonResponse postWelcome() {
		Logger.log(this, "postWelcome");
		CommonResponse response = new CommonResponse();
		response.setStatusCode(0);
		response.setErrorMessage("");
		return response;
	}
}
