package com.qiren.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.qiren.common.response.CommonResponse;
import com.qiren.common.tools.CommonUtils;
import com.qiren.portal.beans.UserBean;
import com.qiren.portal.service.CompanyService;
import com.qiren.portal.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/supplier")
public class SupplierApiController {

	@Autowired
	private CompanyService companyService;
	@Autowired
	private LoginService loginService;
	
	@PostMapping("/company/info")
	public @ResponseBody CommonResponse viewSelfCompany(HttpServletRequest request) {
		try {
			return companyService.viewSelfCompany(request, loginService);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail(e.getMessage());
		}
	}
}
