package com.myteammanager.util;

import java.util.regex.Pattern;

import android.util.Patterns;

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
	
	public static boolean validEmail(String email) {
	    Pattern pattern = Patterns.EMAIL_ADDRESS;
	    return pattern.matcher(email).matches();
	}
	
}
