package com.myteammanager.storage.util;

import com.myteammanager.storage.reflection.ReflectionManagerForDB;




public class SQLStringUtil {
	
	public final static String LOG_TAG = SQLStringUtil.class.getName();

	public static boolean isTableColumn(String variableName) {
		return variableName.startsWith("m_");
	}
	
	public static boolean isPrimaryKey(String variableName) {
		return variableName.startsWith(ReflectionManagerForDB.M_KEY);
	}
	
	public static boolean isID(String variableName) {
		return variableName.endsWith("id");
	}
	
	public static boolean mustBeUnique(String variableName) {
		return variableName.contains("unique");
	}
	
	public static String getStringWithFirstCapitalLetter(String string) {
		return string.substring(0,1).toUpperCase() + string.substring(1);
	}
}
