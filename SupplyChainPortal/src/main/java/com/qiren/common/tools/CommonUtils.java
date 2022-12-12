package com.qiren.common.tools;

import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.http.HttpStatusCode;

import com.qiren.common.response.CommonResponse;

public class CommonUtils {
	
	public static final String md5(String path) {
		return MD5Encoder.encode(path.getBytes());
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
}
