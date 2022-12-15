package com.qiren.supplier.controller;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.qiren.common.response.CommonResponse;

@RestController
@RequestMapping("/api/welcome")
public class WelcomeController {

	@GetMapping("/test")
	public @ResponseBody CommonResponse welcome() {
		CommonResponse response = new CommonResponse();
		
		HashMap<String, String> welcomeHashMap = new HashMap<>();
		welcomeHashMap.put("welcome", "Very good controller, love from supplier");
		
		response.setData(new Gson().toJson(welcomeHashMap));
		response.setStatusCode(0);
		
		return response;
	}
}
