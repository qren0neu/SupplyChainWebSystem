package com.qiren.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/pages/dashboard")
public class DashBoardController {

	@Autowired
	private LoginService loginService;
	@Autowired
	private CompanyService companyService;

	/**
	 * Get dashboard page of each roles
	 */
	@GetMapping(path = "/{role}")
	public String getMainPage(HttpServletRequest request, @PathVariable String role) {
		Logger.log(this, "getMainPage " + role);

		UserBean userBean = loginService.getLoginUser(request);

		if (null == userBean || !Role.validRole(role)) {
			Logger.error("invalid role" + role);
			return CommonUtils.errorPage(null);
		}

		Role myRole = Role.valueOf(role.toUpperCase());

		switch (myRole) {
		case DISTRIBUTOR: {
			return CommonUtils.page("/dashboard/dashboardDis");
		}
		case MANUFACTURER: {
			return CommonUtils.page("/dashboard/dashboardMau");
		}
		case SUPPLIER: {
			return CommonUtils.page("/dashboard/dashboardSup");
		}
		default:
			break;
		}

		return CommonUtils.page("/dashboard/dashboard");
	}

	@GetMapping(path = "/viewSelf")
	public String getSelfInfoPage(HttpServletRequest request) {
		Logger.log(this, "getSelfInfoPage ");

		if (!loginService.isLogin(request)) {
			return CommonUtils.errorPage(null);
		}

		return CommonUtils.page("/user/viewSelf");
	}

	@GetMapping(path = "/editSelf")
	public String editSelfInfoPage(HttpServletRequest request) {
		Logger.log(this, "editSelfInfoPage ");

		if (!loginService.isLogin(request)) {
			return CommonUtils.errorPage(null);
		}

		return CommonUtils.page("/user/editUser");
	}

	@GetMapping(path = "/viewCompanyUser")
	public String viewCompanyUser(HttpServletRequest request) {

		if (!loginService.isLogin(request)) {
			return CommonUtils.errorPage(null);
		}
		if (!companyService.isManager(request)) {
			return CommonUtils.errorPage();
		}

		return CommonUtils.page("/company/companyUser");
	}

	@GetMapping(path = "/viewRoutes")
	public String viewRoutes(HttpServletRequest request) {

		if (!loginService.isLogin(request)) {
			return CommonUtils.errorPage(null);
		}
		if (!companyService.isRouter(request)) {
			return CommonUtils.errorPage();
		}

		return CommonUtils.page("/dashboard/routeService");
	}

	@GetMapping(path = "/viewManufactureRoutes")
	public String viewManufactureRoutes(HttpServletRequest request) {

		if (!loginService.isLogin(request)) {
			return CommonUtils.errorPage(null);
		}

		return CommonUtils.page("/manufacturer/viewRoute");
	}
	
//	@PostMapping("/viewRoutes/next/{service}")
//	public String viewRoutesNext(HttpServletRequest request) {
//
//		if (!loginService.isLogin(request)) {
//			return CommonUtils.errorPage(null);
//		}
//		if (!companyService.isRouter(request)) {
//			return CommonUtils.errorPage();
//		}
//
//		return CommonUtils.page("/dashboard/buildRoute");
//	}
}
