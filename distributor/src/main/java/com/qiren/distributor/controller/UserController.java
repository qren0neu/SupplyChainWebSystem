package com.qiren.distributor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.qiren.common.response.CommonResponse;
import com.qiren.distributor.request.UserAuthRequest;
import com.qiren.distributor.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * Store some of the user info for future authorization
	 */
	@PostMapping("/createAuth")
	public @ResponseBody CommonResponse createUserAuth(@RequestBody UserAuthRequest request) {
		return userService.createAuth(request);
	}
	
	@PostMapping("/getAuth")
	public @ResponseBody CommonResponse authUser(@RequestBody UserAuthRequest userAuthRequest) {
		return userService.getAuth(userAuthRequest);
	}

	@PostMapping("/updateAuth")
	public @ResponseBody CommonResponse updateUserAuth(@RequestBody UserAuthRequest request) {
		return userService.updateAuth(request);
	}
}
