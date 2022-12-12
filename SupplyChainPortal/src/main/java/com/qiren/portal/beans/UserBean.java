package com.qiren.portal.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBean extends DefaultBean {

	private String userId;
	private String userName;
	private String firstName;
	
	public UserBean() {
		
	}
}
