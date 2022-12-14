package com.qiren.portal.controller;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import com.qiren.common.response.CommonResponse;
import com.qiren.common.tools.CommonUtils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@ControllerAdvice
public class MyErrorController {

	@ExceptionHandler(Exception.class)
	public @ResponseBody CommonResponse errorHandling(Exception ex) {
		return CommonUtils.fail(ex.getMessage());
	}
	
	@GetMapping("/pages/error")
	public String errorPage() {
		return "/error";
	}
}
