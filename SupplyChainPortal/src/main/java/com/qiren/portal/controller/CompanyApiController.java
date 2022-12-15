package com.qiren.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.qiren.common.response.CommonResponse;
import com.qiren.common.tools.CommonUtils;
import com.qiren.common.tools.Constants;
import com.qiren.common.tools.InternalRole;
import com.qiren.common.tools.Logger;
import com.qiren.common.tools.Role;
import com.qiren.portal.entities.CommonUserEntity;
import com.qiren.portal.request.ChooseCompanyRequset;
import com.qiren.portal.request.CompanyRegistrationRequest;
import com.qiren.portal.service.CompanyService;
import com.qiren.portal.service.LoginService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/company")
public class CompanyApiController {

	@Autowired
	private CompanyService companyService;
	@Autowired
	private LoginService loginService;

	@PostMapping("/register/{username}")
	public @ResponseBody CommonResponse registerCompany(@PathVariable String username,
			@RequestBody @Valid CompanyRegistrationRequest companyRegisRequest, BindingResult bindingResult) {

		Logger.log("registerCompany " + username);
		boolean isSuccess = !bindingResult.hasErrors();
		if (isSuccess) {
			return companyService.registerCompanyAndModifyUser(loginService, companyRegisRequest, username);
		}
		return CommonUtils.bindingError(bindingResult);
	}

	@PostMapping("/chooseCompany")
	public @ResponseBody CommonResponse chooseCompany(
			@RequestBody @Valid ChooseCompanyRequset chooseCompanyRequset) {
		Logger.log("chooseCompany ");
		return companyService.chooseCompany(loginService, chooseCompanyRequset);
	}

	@PostMapping("/viewAll/{role}")
	public @ResponseBody CommonResponse findCompanyByType(@PathVariable String role) {
		Logger.log("findCompanyByType " + role);
		return companyService.findCompanyByType(role);
	}

	@PostMapping("/viewAll")
	public @ResponseBody CommonResponse findCompanyAll() {
		Logger.log("findCompanyAll ");
		return companyService.findAllCompany();
	}
}
