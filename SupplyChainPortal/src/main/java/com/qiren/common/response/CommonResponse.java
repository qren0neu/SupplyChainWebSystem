package com.qiren.common.response;

import java.io.Serializable;

import lombok.Data;

@Data
public class CommonResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int statusCode;
	
	private String errorMessage;
	
}
