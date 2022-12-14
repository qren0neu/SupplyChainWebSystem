package com.qiren.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.qiren.common.tools.CommonUtils;
import com.qiren.common.tools.Constants;
import com.qiren.common.tools.Role;
import com.qiren.portal.beans.UserBean;
import com.qiren.portal.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;

@Controller()
@RequestMapping("/pages/login")
public class LoginController {

	@Autowired
	private LoginService loginService;

	@PostMapping(path = "/welcome/{name}")
	public @ResponseBody String welcome(@PathVariable String name) {
		return "Welcome, " + name;
	}

	@GetMapping(path = "/userLogin/{role}")
	public RedirectView login(@PathVariable String role, HttpServletRequest request) {
		Object sesssionObject = request.getSession().getAttribute(Constants.SESSION_KEY);
		if (null != sesssionObject) {
			UserBean userBean = loginService.getLoginUser(sesssionObject.toString());
			if (null != userBean) {
				return CommonUtils.redirect("/dashboard/" + role);
			}
		}

		if (Role.validRole(role)) {
			return CommonUtils.redirect("/login/getLogin/" + role);
		}
		return CommonUtils.redirect("/error");
	}

	@GetMapping(path = "/getLogin/{role}")
	public String getLogin(@PathVariable String role) {
		if (Role.validRole(role)) {
			return CommonUtils.page("/login/login");
		}
		return CommonUtils.errorPage();
	}
	
	@GetMapping(path = "/userRegister/{role}")
	public String register(@PathVariable String role) {
		if (Role.validRole(role)) {
			return CommonUtils.page("/login/registration");
		}
		return CommonUtils.errorPage();
	}
	
	@GetMapping(path = "/userLogout")
	public RedirectView logout(HttpServletRequest request) {
		
		UserBean userBean = loginService.getLoginUser(request);
		
		if (null != userBean) {
			loginService.clearLoginUser(CommonUtils.md5(userBean.getUsername()));
			request.getSession().setAttribute(Constants.SESSION_KEY, "");
			request.getSession().invalidate();
			return new RedirectView("/portal/perspective/" + userBean.getRole());
		}
		
		return new RedirectView("/portal");
	}
}
