package com.qiren.common.tools;

import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.qiren.common.response.CommonResponse;

public class RestManager {

	public static RestManager getInstance() {
		return Inner.instance;
	}

	public CommonResponse sendHttpGet(RestTemplate template, String url, Object body) {
		if (null != body) {
			url += "?data=";
			url += new Gson().toJson(body);
		}

		CommonResponse responseBody = template.getForObject(url, CommonResponse.class);
		return responseBody;
	}

	public CommonResponse sendHttpPost(RestTemplate template, String url, Object body) {
		CommonResponse responseBody = template.postForObject(url, body, CommonResponse.class);
		return responseBody;
	}

	private RestManager() {

	}

	static class Inner {

		private static RestManager instance = new RestManager();
	}
}
