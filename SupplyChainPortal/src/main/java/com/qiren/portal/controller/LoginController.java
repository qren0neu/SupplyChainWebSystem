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
import com.qiren.common.tools.Logger;
import com.qiren.common.tools.Role;
import com.qiren.portal.beans.UserBean;
import com.qiren.portal.entities.CommonUserEntity;
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

	/**
	 * Get login page, if logged in, go to dashboard
	 */
	@GetMapping(path = "/userLogin")
	public RedirectView login(HttpServletRequest request) {
		Logger.log("login");
		Object sesssionObject = request.getSession().getAttribute(Constants.SESSION_KEY);
		if (null != sesssionObject) {
			UserBean userBean = loginService.getLoginUser(sesssionObject.toString());
			if (null != userBean) {
				return CommonUtils.redirect("/dashboard/" + userBean.getRole());
			}
		}

		return CommonUtils.redirect("/login/getLogin");
	}

	/**
	 * get login page only
	 */
	@GetMapping(path = "/getLogin")
	public String getLogin() {
		return CommonUtils.page("/login/login");
	}
	
	/**
	 * get register page only
	 */
	@GetMapping(path = "/userRegister")
	public String register() {
		return CommonUtils.page("/login/registration");
	}

	/**
	 * Get "selectRole" page
	 */
	@GetMapping(path = "/selectRole/{username}")
	public String selectRole(@PathVariable String username) {
		// username here need to be md5
		CommonUserEntity entity = loginService.findUserInfoByName(username);
		
		if (null == entity || 
				Role.valueOf(entity.getRole().toUpperCase()) != Role.COMMON) {
			// not valid
			return CommonUtils.errorPage();
		}
		
		return CommonUtils.page("/login/selectRole");
	}
	
	@GetMapping(path = "/userLogout")
	public RedirectView logout(HttpServletRequest request) {
		
		UserBean userBean = loginService.getLoginUser(request);
		
		if (null != userBean) {
			loginService.clearLoginUserCache(CommonUtils.md5(userBean.getUsername()));
			request.getSession().setAttribute(Constants.SESSION_KEY, "");
			request.getSession().invalidate();
			return new RedirectView("/portal/perspective/" + userBean.getRole());
		}
		
		return new RedirectView("/portal");
	}
}
