package com.qiren.common.tools;

public class RoleUtils {
	
	public static boolean checkPermissionViewingOtherUser(String positionType) {
		if (null == positionType || !positionType.contains("_")) {
			return false;
		}
		String prefixString = positionType.split("_")[0];
		return "companymanager".equals(prefixString.toLowerCase());
	}
	
}
