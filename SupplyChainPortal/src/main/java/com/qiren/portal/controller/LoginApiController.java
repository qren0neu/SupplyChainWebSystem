package com.qiren.portal.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.qiren.common.response.CommonResponse;
import com.qiren.common.tools.CommonUtils;

@RestController()
@RequestMapping("/api/login")
public class LoginApiController {
	
	@PostMapping("/userLogin/{role}")
	public @ResponseBody CommonResponse userLogin(@PathVariable String role) {
		if (null == role) {
			return CommonUtils.fail("Missing path variable!");
		} else {
			return CommonUtils.success();
		}
	}
}
