package com.qiren.common.tools;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.http.HttpStatusCode;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.qiren.common.response.CommonResponse;

public class CommonUtils {
	
	public static final String md5(String ori) {
		return DigestUtils.md5Hex(ori.getBytes());
	}
	
	public static String page(String pageSub) {
		return "pages" + pageSub;
	}
	
	public static String errorPage() {
//		return page("/error/error");
		return "/error";
	}
	
	public static final CommonResponse success() {
		CommonResponse response = new CommonResponse();
		response.setStatusCode(Constants.STATUS_CODE_SUCCESS);
		return response;
	}
	
	public static final CommonResponse fail(String errorMessage) {
		CommonResponse response = new CommonResponse();
		response.setStatusCode(Constants.STATUS_CODE_FAILED);
		response.setErrorMessage(errorMessage);
		return response;
	}
	
	public static final CommonResponse bindingError(BindingResult bindingResult) {
		StringBuilder sb = new StringBuilder();

		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			System.out.println(fieldError.getField() + "::" + fieldError.getDefaultMessage());
			sb.append(fieldError.getField()).append(" : ").append(fieldError.getDefaultMessage()).append("<br/>");
		}

		String error = sb.toString();
		return fail(error);
	}
}
