package com.qiren.common.tools;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.qiren.common.response.CommonResponse;

/**
 * 
 * Sending HTTP
 * 
 * @author renqi
 *
 */
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

	public CommonResponse sendHttpGetWithHeader(RestTemplate template, String url, Map<String, Object> header,
			Object body) {
		CommonResponse commonResponse = new CommonResponse();
		return commonResponse;
	}

	public CommonResponse sendHttpPostWithHeader(RestTemplate template, String url, Map<String, Object> header,
			Object body) {
		CommonResponse commonResponse = new CommonResponse();

		HttpHeaders headers = new HttpHeaders();

		for (String key : header.keySet()) {
			Logger.log("add header" + key + header.get(key));
			headers.add(key, header.get(key).toString());
		}
		
		headers.add("Content-Type", "application/json");

		HttpEntity<?> requestEntity = new HttpEntity<>(body, headers);

		ResponseEntity<String> response = template.exchange(url, HttpMethod.POST, requestEntity, String.class);

		String result = response.getBody();
		CommonResponse remote = new Gson().fromJson(result, CommonResponse.class);

		return remote;
	}

	private RestManager() {

	}

	static class Inner {

		private static RestManager instance = new RestManager();
	}
}
