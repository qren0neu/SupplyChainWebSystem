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
		boolean isSuccess = !bindingResult.hasErrors();
		if (isSuccess) {
//			if (!loginService.checkLoginCorrect(username)) {
//				return CommonUtils.fail("login information mismatch");
//			}

			CommonUserEntity entity = loginService.findFullUserInfoByName(username);

			if (null == entity) {
				return CommonUtils.fail("username not found");
			}

			Role role = Role.valueOf(entity.getRole().toUpperCase());

			if (role != Role.COMMON) {
				return CommonUtils.fail("invalid role");
			}

			// create the company
			String errorString = companyService.registerNewCompany(companyRegisRequest);
			if (!errorString.isBlank()) {
				return CommonUtils.fail(errorString);
			}
			// change user type
			String companyRole = companyRegisRequest.getRole();
			Role enumCompanyRole = Role.valueOf(companyRole.toUpperCase());

			String userRoleNew = "";

			switch (enumCompanyRole) {
			case SUPPLIER: {
				userRoleNew = InternalRole.Supplier.COMPANY_MANAGER;
				break;
			}
			case DISTRIBUTOR: {
				userRoleNew = InternalRole.Distributor.COMPANY_MANAGER;
				break;
			}
			case MANUFACTURER: {
				userRoleNew = InternalRole.Manufacturer.COMPANY_MANAGER;
				break;
			}
			case ROUTER: {
				userRoleNew = InternalRole.Router.ROUTE_PLANNER;
				break;
			}
			default:
				return CommonUtils.fail("Invalid input");
			}

			entity.setRole(companyRole);

			boolean resp = loginService.createLogin(entity, userRoleNew);

			if (!resp) {
				return CommonUtils.fail("Create new login failed");
			}

			return CommonUtils.frontEndRedirect("/dashboard/" + companyRole);
		}
		return CommonUtils.bindingError(bindingResult);
	}

	@PostMapping("/viewAll/{role}")
	public @ResponseBody CommonResponse findCompanyByType(@PathVariable String role) {
		Logger.log("findCompanyByType " + role);
		return companyService.findCompanyByType(role);
	}

}
