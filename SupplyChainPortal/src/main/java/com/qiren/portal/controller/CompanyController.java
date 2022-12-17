package com.qiren.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qiren.common.tools.CommonUtils;
import com.qiren.common.tools.Logger;
import com.qiren.common.tools.Role;
import com.qiren.portal.beans.UserBean;
import com.qiren.portal.entities.CommonUserEntity;
import com.qiren.portal.service.CompanyService;
import com.qiren.portal.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/pages/company")
public class CompanyController {

	@Autowired
	private LoginService loginService;
	@Autowired
	private CompanyService companyService;

	@GetMapping("/getRegister/{username}")
	public String getCompanyRegistration(@PathVariable String username, HttpServletRequest request) {
		Logger.log(this, "getCompanyRegistration");
		CommonUserEntity userBean = loginService.findUserInfoByName(username);

		if (null == userBean || Role.valueOf(userBean.getRole().toUpperCase()) != Role.COMMON) {
			// only common user can go
			return CommonUtils.errorPage();
		}

		return CommonUtils.page("/company/companyRegistration");
	}

	@GetMapping("/selectCompany/{username}")
	public String getCompanySelection(@PathVariable String username, HttpServletRequest request) {
		Logger.log(this, "getCompanySelection");
		CommonUserEntity userBean = loginService.findUserInfoByName(username);

		if (null == userBean || Role.valueOf(userBean.getRole().toUpperCase()) != Role.COMMON) {
			// only common user can go
			return CommonUtils.errorPage();
		}

		return CommonUtils.page("/company/companySelection");
	}

	@GetMapping("/viewStaff/{username}")
	public String viewCompanyStaff(HttpServletRequest request, @PathVariable String username) {
		if (!companyService.isManager(request)) {
			return CommonUtils.errorPage();
		}
		return CommonUtils.page("/company/editUser");
	}

//	public String viewCompany() {
//		
//	}
}
