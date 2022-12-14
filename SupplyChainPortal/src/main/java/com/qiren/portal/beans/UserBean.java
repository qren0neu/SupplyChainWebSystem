package com.qiren.portal.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBean extends DefaultBean {

	private String userid;
	private String username;
	private String fname;
	private String role;
	private String type;
	
	public UserBean() {
		
	}
}
