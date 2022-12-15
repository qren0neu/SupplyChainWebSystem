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

	public static final String md5(String ori) {
		return DigestUtils.md5Hex(ori.getBytes());
	}

	public static String page(String pageSub) {
		return "pages" + pageSub;
	}

	public static RedirectView redirect(String pageSub) {
		return new RedirectView("/portal/pages" + pageSub);
	}

	public static CommonResponse frontEndRedirect(String pageSub) {
		CommonResponse response = new CommonResponse();
		response.setStatusCode(Constants.STATUS_CODE_REDIRECT);
		response.setRedirect(page(pageSub));
		return response;
	}

	public static String errorPage() {
//		return page("/error/error");
		Logger.error("Redirect to error page due to violation.");
		return "/error";
	}

	public static final CommonResponse success() {
		CommonResponse response = new CommonResponse();
		response.setStatusCode(Constants.STATUS_CODE_SUCCESS);
		return response;
	}

	public static final CommonResponse success(Object data) {

		GsonBuilder builder = new GsonBuilder();

		Gson gson = builder.create();

		CommonResponse response = new CommonResponse();
		response.setStatusCode(Constants.STATUS_CODE_SUCCESS);
		response.setData(gson.toJson(data));
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
