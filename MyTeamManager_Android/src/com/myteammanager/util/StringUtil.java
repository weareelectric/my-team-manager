package com.myteammanager.util;

public class StringUtil {

	public static boolean isNotEmpty(CharSequence string) {
		return string != null && string.length() > 0;
	}

	public static String getValue(String string) {
		if (string == null)
			return "";
		else
			return string;
	}

	public static String getStringAfterUnderscore(String name) {
		int indexOfLastUnderscore = name.lastIndexOf("_");
		return name.substring(indexOfLastUnderscore + 1);
	}
}
