package com.qiren.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.qiren.common.response.CommonResponse;
import com.qiren.common.tools.CommonUtils;
import com.qiren.common.tools.Logger;
import com.qiren.common.tools.Role;
import com.qiren.portal.request.UserRegistrationRequest;
import com.qiren.portal.service.LoginService;

import jakarta.validation.Valid;

@RestController()
@RequestMapping("/api/login")
public class LoginApiController {

	@Autowired
	private LoginService loginService;
	
	@PostMapping("/userLogin/{role}")
	public @ResponseBody CommonResponse userLogin(@PathVariable String role) {
		Logger.log(this, "userLogin");
		if (null == role) {
			return CommonUtils.fail("Missing path variable!");
		} else {
			return CommonUtils.success();
		}
	}

	@PostMapping("/userRegister/{role}")
	public @ResponseBody CommonResponse userRegister(
			@Valid @RequestBody UserRegistrationRequest userRequest, 
			BindingResult bindingResult,
			@PathVariable String role) {
		Logger.log(this, "userRegister");
		if (null == role || !Role.validRole(role)) {
			return CommonUtils.fail("Invalid Role!");
		} else {
			boolean isSuccess = !bindingResult.hasErrors();
			if (isSuccess) {
				// create the user
				String messageString = loginService.registerUser(userRequest, role);
				if (null != messageString && !messageString.isBlank()) {
					return CommonUtils.fail(messageString);
				}
				return CommonUtils.success();
			}
			return CommonUtils.bindingError(bindingResult);
		}
	}
}
