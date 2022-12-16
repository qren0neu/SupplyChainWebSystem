package com.qiren.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
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
import com.qiren.common.tools.RoleUtils;
import com.qiren.portal.beans.UserBean;
import com.qiren.portal.entities.CommonUserEntity;
import com.qiren.portal.request.UserLoginRequest;
import com.qiren.portal.request.UserRegistrationRequest;
import com.qiren.portal.service.CompanyService;
import com.qiren.portal.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController()
@RequestMapping("/api/login")
public class LoginApiController {

	@Autowired
	private LoginService loginService;
	@Autowired
	private CompanyService companyService;

	/**
	 * Check login info
	 */
	@PostMapping("/userLogin")
	public @ResponseBody CommonResponse userLogin(@RequestBody UserLoginRequest userLoginRequest,
			HttpServletRequest httpServletRequest) {
		Logger.log(this, "userLogin ");
		try {
			return loginService.login(userLoginRequest.getUsername(), userLoginRequest.getPassword(),
					httpServletRequest);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail("Login failed");
		}
	}

	/**
	 * Do registration, role is just a check here.
	 */
	@PostMapping("/userRegister/{role}")
	public @ResponseBody CommonResponse userRegister(@Valid @RequestBody UserRegistrationRequest userRequest,
			BindingResult bindingResult, @PathVariable String role) {
		Logger.log(this, "userRegister " + role);
		if (null == role || !Role.validRole(role)) {
			return CommonUtils.fail("Invalid Role!");
		} else {
			boolean isSuccess = !bindingResult.hasErrors();
			if (isSuccess) {
				// create the user
				String messageString = loginService.registerUser(userRequest, "common");
				if (null != messageString && !messageString.isBlank()) {
					return CommonUtils.fail(messageString);
				}
				return CommonUtils.success();
			}
			return CommonUtils.bindingError(bindingResult);
		}
	}

	/**
	 * Register customer info into "login" table
	 */
	@PostMapping("/loginRegister/{role}/{username}")
	public @ResponseBody CommonResponse loginRegister(@PathVariable String role, @PathVariable String username) {
		Logger.log(this, "loginRegister " + role);
		return loginService.customerRegisterLogin(username, role);
	}

	/**
	 * view registered user info
	 * 
	 * @param username user name in md5 hash
	 * @return user full info
	 */
	@PostMapping("/viewUser/{username}")
	public @ResponseBody CommonResponse viewUser(
			HttpServletRequest servletRequest,
			@PathVariable String username) {
		Logger.log(this, "viewUser " + username);
		
		UserBean userBean = loginService.getLoginUser(servletRequest);
		
		if (null == userBean) {
			return CommonUtils.fail("Not logged in");
		}
		
		String positionType = userBean.getType();
		if (RoleUtils.checkPermissionViewingOtherUser(positionType)) {
			return CommonUtils.fail("no permission");
		}
		
		CommonUserEntity userEntity = loginService.findUserInfoByName(username);
		if (null == userEntity) {
			return CommonUtils.fail("Failed to view userinfo");
		} else {
			
			if (companyService.isInSameCompany(
					userBean.getCommonInfo().getPkUser() + "", 
					userEntity.getPkUser() + "")) {
				userEntity.setPassword("");
				return CommonUtils.success(userEntity);
			}

			return CommonUtils.fail("no permission");
		}
	}

	@PostMapping("/viewUser/self")
	public CommonResponse getSelfInfoLogin(HttpServletRequest servletRequest) {

		UserBean userBean = loginService.getLoginUser(servletRequest);
		
		if (null == userBean) {
			return CommonUtils.fail("Not logged in");
		}
		
		CommonUserEntity commonUserEntity = userBean.getCommonInfo();
		commonUserEntity.setPassword(null);
		
		return CommonUtils.success(commonUserEntity);
	}
}
