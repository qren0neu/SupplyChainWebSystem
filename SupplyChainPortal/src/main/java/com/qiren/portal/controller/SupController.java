package com.qiren.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qiren.common.tools.CommonUtils;
import com.qiren.portal.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/pages/supplier")
public class SupController {
	
	@Autowired
	private LoginService loginService;

	@GetMapping("/viewProduct")
	public String viewProduct(HttpServletRequest request) {
		
		if (!loginService.isLogin(request)) {
			return CommonUtils.errorPage(null);
		}
		
//		return CommonUtils.page("/supplier/enterPrice");
		return CommonUtils.page("/supplier/viewProduct");
	}
	
	@GetMapping("/enterPrice")
	public String enterPrice(HttpServletRequest request) {
		
		if (!loginService.isLogin(request)) {
			return CommonUtils.errorPage(null);
		}
		
		return CommonUtils.page("/supplier/enterPrice");
//		return CommonUtils.page("/supplier/viewProduct");
	}
	
	@GetMapping("/viewOrder")
	public String viewOrder(HttpServletRequest request) {
		
		if (!loginService.isLogin(request)) {
			return CommonUtils.errorPage(null);
		}
		
		return CommonUtils.page("/supplier/viewOrder");
	}
}
