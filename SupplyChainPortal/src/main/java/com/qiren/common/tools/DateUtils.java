package com.qiren.common.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");

	public static String getDateTime() {
		return sdf.format(new Date());
	}

}
