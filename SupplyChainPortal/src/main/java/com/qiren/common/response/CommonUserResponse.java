package com.qiren.common.response;

import lombok.Data;

/**
 * This is the response entity for subsystem (or customer login table in the
 * main system)
 * 
 * @author renqi
 *
 */
@Data
public class CommonUserResponse {
	private String username;
	private String role;
	private String type;
	private String fname;
}
