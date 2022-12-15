package com.qiren.manufacture.request;

import lombok.Data;

@Data
public class UserAuthRequest {
	
	private String userid;

	private String type;

	private String identifier;

	private String credential;

	private String role;
	
}
