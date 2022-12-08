package com.qiren.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiren.common.response.CommonResponse;
import com.qiren.common.tools.Logger;

@Controller
public class WelcomeController {
	
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
