package com.qiren.common.response;

import lombok.Data;

@Data
public class CommonUserResponse {
	private String userid;
	private String username;
	private String password;
	private String role;
	private String type;
}
