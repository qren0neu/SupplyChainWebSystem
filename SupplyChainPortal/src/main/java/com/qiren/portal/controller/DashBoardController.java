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
import com.qiren.portal.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/pages/dashboard")
public class DashBoardController {

	@Autowired
	private LoginService loginService;
	
	/**
	 * Get dashboard page of each roles
	 */
	@GetMapping(path = "/{role}")
	public String getMainPage(HttpServletRequest request, @PathVariable String role) {
		Logger.log(this, "getMainPage " + role);
		
		UserBean userBean = loginService.getLoginUser(request);
		
		if (null == userBean || !Role.validRole(role)) {
			return CommonUtils.errorPage();
		}
		
		return CommonUtils.page("/dashboard/dashboard");
	}
	
	
}
