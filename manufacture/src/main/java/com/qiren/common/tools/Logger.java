package com.qiren.common.tools;

import lombok.NonNull;

public class Logger {
	
	private static final String TAG_DEFALT = "Default";
	
	public static void log(String tag, String message) {
		System.out.println(DateUtils.getDateTime() + " " + tag + " : " + message);
	}
	
	public static void log(@NonNull Object obj, String message) {
		log(obj.getClass().getSimpleName(), message);
	}
	
	public static void log(String message) {
		log(TAG_DEFALT, message);
	}
	
	public static void error(String message) {
		error(TAG_DEFALT, message);
	}

	public static void error(@NonNull Object obj, String message) {
		error(obj.getClass().getSimpleName(), message);
	}
	
	public static void error(String tag, String message) {
		System.err.println(DateUtils.getDateTime() + " " + tag + " : " + message);
	}
}
