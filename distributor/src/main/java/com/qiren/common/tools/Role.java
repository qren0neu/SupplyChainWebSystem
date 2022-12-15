package com.qiren.common.tools;

public enum Role {
	COMMON,
	CUSTOMER,
	SUPPLIER,
	MANUFACTURER,
	DISTRIBUTOR,
	ROUTER,
	DELIVER;
	
	public static final boolean validRole(String role) {
		try {
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
