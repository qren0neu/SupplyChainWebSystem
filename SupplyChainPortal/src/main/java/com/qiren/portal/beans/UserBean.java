package com.qiren.portal.beans;

import java.util.HashMap;

import com.qiren.portal.entities.CommonUserEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBean extends DefaultBean {

	private static final long serialVersionUID = 1L;
	private String userid;
	private String username;
	private String fname;
	private String role;
	private String type;
	
	private CommonUserEntity commonInfo;
	private HashMap<String, Object> extraInfo;
	
	public UserBean() {
		
	}
}
