package com.qiren.common.tools;

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
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
