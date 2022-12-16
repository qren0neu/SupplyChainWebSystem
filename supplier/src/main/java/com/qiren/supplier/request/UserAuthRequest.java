package com.qiren.supplier.request;

import lombok.Data;

@Data
public class UserAuthRequest {
	
	private String userid;

	private String type;

	private String identifier;

	private String credential;

	private String role;
	
	private int company;
	
}
