package com.qiren.common.tools;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qiren.common.response.CommonResponse;

public class CommonUtils {

	/**
	 * Simple md5 hash
	 */
	public static final String md5(String ori) {
		return DigestUtils.md5Hex(ori.getBytes());
	}

	/**
	 * Get server html pages (thymeleaf), cannot return api here!
	 * Cannot be used for redirect!
	 */
	public static String page(String pageSub) {
		Logger.log("Getting Page: " + "pages" + pageSub);
		return "pages" + pageSub;
	}

	/**
	 * Generate a {@code RedirectView}
	 */
	public static RedirectView redirect(String pageSub) {
		return new RedirectView("/portal/pages" + pageSub);
	}

	/**
	 * Send back a {@code CommonResponse} and let frontend itself to redirect
	 * Using window.location
	 */
	public static CommonResponse frontEndRedirect(String pageSub) {
		CommonResponse response = new CommonResponse();
		response.setStatusCode(Constants.STATUS_CODE_REDIRECT);
		response.setRedirect("/portal/pages" + pageSub);
		return response;
	}

	/**
	 * 404 or violation of role
	 */
	public static String errorPage() {
		Logger.error("Redirect to error page due to violation.");
		return "/error";
	}
	
	public static String errorPage(String errorMessage) {
		Logger.error("Redirect to error page due to violation." + errorMessage);
		
		return "/errorLogin";
	}

	/**
	 * Common success response
	 */
	public static final CommonResponse success() {
		CommonResponse response = new CommonResponse();
		response.setStatusCode(Constants.STATUS_CODE_SUCCESS);
		return response;
	}

	/**
	 * Common success response with extra data
	 * 
	 * @param data the true data body of the response
	 */
	public static final CommonResponse success(Object data) {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		CommonResponse response = new CommonResponse();
		response.setStatusCode(Constants.STATUS_CODE_SUCCESS);
		response.setData(gson.toJson(data));
		return response;
	}

	/**
	 * Common failure response
	 */
	public static final CommonResponse fail(String errorMessage) {
		CommonResponse response = new CommonResponse();
		response.setStatusCode(Constants.STATUS_CODE_FAILED);
		response.setErrorMessage(errorMessage);
		return response;
	}

	/**
	 * Simply returning binding error
	 */
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
