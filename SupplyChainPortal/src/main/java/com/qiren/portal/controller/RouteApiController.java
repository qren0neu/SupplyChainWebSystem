package com.qiren.portal.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qiren.common.response.CommonResponse;
import com.qiren.common.tools.CommonUtils;
import com.qiren.portal.service.LoginService;
import com.qiren.portal.service.RouteService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/route")
public class RouteApiController {

	@Autowired
	private RouteService routeService;
	@Autowired
	private LoginService loginService;

	@PostMapping("/viewAllService")
	public CommonResponse getAllServices(HttpServletRequest request) {
		if (!loginService.isLogin(request)) {
			return CommonUtils.fail("Not login");
		}
		return CommonUtils.successOrFail(routeService.getAllServices());
	}

	@PostMapping("/buildRoute")
	public CommonResponse buildRoute(HttpServletRequest servletRequest, @RequestBody HashMap<String, Object> buildRouteRequest) {
		if (!loginService.isLogin(servletRequest)) {
			return CommonUtils.fail("Not login");
		}
		return CommonUtils.successOrFail(routeService.buildRoute(servletRequest, buildRouteRequest));
	}
}
