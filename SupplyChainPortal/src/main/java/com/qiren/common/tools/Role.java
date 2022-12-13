package com.qiren.common.tools;

import java.sql.Connection;

public enum Role {
	CUSTOMER,
	SUPPLIER,
	MANUFACTURER,
	DISTRIBUTOR,
	ROUTER,
	DELIVER;
	
	public static final boolean validRole(String role) {
		try {
			valueOf(role.toUpperCase());
			Connection connection;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
