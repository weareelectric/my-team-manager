package com.myteammanager.storage;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import com.myteammanager.beans.BaseBean;
import com.myteammanager.storage.reflection.ReflectionManagerForDB;
import com.myteammanager.storage.util.SQLStringUtil;
import com.myteammanager.util.StringUtil;

import android.content.Context;
import android.location.GpsStatus.NmeaListener;
import com.myteammanager.util.Log;

public class TableBeansMappingManager {

	public static final String LOG_TAG = TableBeansMappingManager.class.getName();

	private static Hashtable<String, Vector<MethodStructure>> m_beansFieldsTable = null;

	private static TableBeansMappingManager m_instance = null;

	public static void init(Context context) {
		if (m_instance == null) {
			m_instance = new TableBeansMappingManager();
			m_beansFieldsTable = new Hashtable<String, Vector<MethodStructure>>();
		}

		ArrayList<MethodStructure> mapping = DBManager.getInstance().getBeansMapping();

		for (MethodStructure method : mapping) {
			insertMapping(method);
		}

	}

	private static void insertMapping(MethodStructure method) {
		String key = method.getClassName();
		if (m_beansFieldsTable.containsKey(key)) {
			Vector<MethodStructure> methodsStructs = (Vector<MethodStructure>) m_beansFieldsTable.get(key);
			methodsStructs.add(method);
		} else {
			Vector<MethodStructure> methodsStructs = new Vector<MethodStructure>();
			methodsStructs.add(method);
			m_beansFieldsTable.put(key, methodsStructs);
		}

	}

	public static String getColumnIndex(Object bean) {
		String beanName = bean.getClass().getSimpleName();
		Vector<MethodStructure> methods = m_beansFieldsTable.get(beanName);
		for (MethodStructure method : methods) {

			if (SQLStringUtil.isID(method.getFieldName())) {
				return StringUtil.getStringAfterUnderscore(method.getFieldName());
			}
		}

		return null;
	}

	public static ArrayList<MethodStructure> getSQLFields(Object object) {
		ArrayList<MethodStructure> sqlfields = new ArrayList<MethodStructure>();
		Vector<MethodStructure> methods = m_beansFieldsTable.get(object.getClass().getSimpleName());
		for (MethodStructure method : methods) {
			if (SQLStringUtil.isTableColumn(method.getFieldName())) {
				sqlfields.add(method);
			}
		}
		return sqlfields;
	}

	/**
	 * Returns the fields associated with variables that are not other bean object that are stored in other teable
	 * 
	 * @param object
	 * @return
	 */
	public static ArrayList<MethodStructure> getSQLFieldsNoBean(Object object) {

		ArrayList<MethodStructure> sqlfields = new ArrayList<MethodStructure>();
		Vector<MethodStructure> methods = m_beansFieldsTable.get(object.getClass().getSimpleName());
		for (MethodStructure method : methods) {
			if (!isABean(method.getType()) && SQLStringUtil.isTableColumn(method.getFieldName())) {
				sqlfields.add(method);
			}
		}
		return sqlfields;
	}

	/**
	 * Returns only the fields that are related to other bean objects (child of the bean object passed as argument)
	 * 
	 * @param object
	 * @return
	 */
	public static ArrayList<MethodStructure> getSQLFieldsForBeans(Object object) {
		ArrayList<MethodStructure> sqlfields = new ArrayList<MethodStructure>();
		Vector<MethodStructure> methods = m_beansFieldsTable.get(object.getClass().getSimpleName());
		for (MethodStructure method : methods) {
			if (isABean(method.getType()) && SQLStringUtil.isTableColumn(method.getFieldName())) {
				sqlfields.add(method);
			}
		}
		return sqlfields;

	}

	public static String[] getSQLColumns(Object object) {
		Vector<MethodStructure> methods = m_beansFieldsTable.get(object.getClass().getSimpleName());
		ArrayList<String> columns = new ArrayList<String>();
		for (MethodStructure method : methods) {
			if (SQLStringUtil.isTableColumn(method.getFieldName())) {
				columns.add(TableBeansMappingManager.getColumnName(method.getFieldName()));
			}
		}

		int numberOfFoundColoumn = columns.size();
		String[] colArray = new String[numberOfFoundColoumn];

		for (int i = 0; i < numberOfFoundColoumn; i++) {
			colArray[i] = columns.get(i);
		}

		return colArray;
	}

	public static String getColumnName(String fieldName) {
		return StringUtil.getStringAfterUnderscore(fieldName);

	}

	public static boolean isABean(String type) {
		return type.endsWith("Bean");
	}

	public static boolean isUniqueColumn(MethodStructure methodStruct) {
		return methodStruct.getUnique() == MethodStructure.UNIQUE;
	}

	public static boolean isUniqueColumn(String fieldName) {
		return SQLStringUtil.mustBeUnique(fieldName);
	}

	public static String getWhereClauseForFields(ArrayList<MethodStructure> fields, BaseBean bean) {
		try {
			StringBuffer sb = new StringBuffer();

			int size = fields.size();
			for (MethodStructure method : fields) {
				sb.append(bean.getDatabaseTableName() + "." + getColumnName(method.getFieldName()));
				sb.append("=");
				sb.append("'");
				sb.append(ReflectionManagerForDB.getValueForField(bean, method).toString());
				sb.append("'");
			}
			sb.append(" ");
			return sb.toString();
		} catch (Exception e) {
			return "";
		}
	}

	public static ArrayList<MethodStructure> getFieldUnique(BaseBean bean) {
		ArrayList<MethodStructure> uniqueFields = new ArrayList<MethodStructure>();
		ArrayList<MethodStructure> fields = getSQLFieldsNoBean(bean);

		for (MethodStructure method : fields) {

			if (isUniqueColumn(method)) {
				uniqueFields.add(method);

			}
		}

		return uniqueFields;
	}

	public static String getSQLForColumn(Field field) {
		StringBuffer sb = new StringBuffer();
		String columnName = getColumnName(field.getName());
		sb.append(" ");
		sb.append(columnName);
		sb.append(" ");
		sb.append(TableBeansMappingManager.getSQLTypeForField(field));
		if (SQLStringUtil.isPrimaryKey(field.getName())) {
			sb.append(" ");
			sb.append("PRIMARY KEY AUTOINCREMENT");
		}
		Log.d(LOG_TAG, "getSQLForColumn: " + sb.toString());
		return sb.toString();

	}

	public static String getSQLTypeForField(Field field) {
		String type = field.getType().toString();

		Log.d(SQLStringUtil.LOG_TAG, "type for field: " + field.getType());
		if (type.equals("int")) {
			return "INTEGER";
		} else if (type.equals("long")) {
			return "BIGINT";
		} else if (type.equals("java.lang.String") || type.equals("class java.lang.String")) {
			return "TEXT";
		} else if (type.equals("java.util.Date") || type.equals("class java.util.Date")) {
			return "TIMESTAMP";
		} else if (type.endsWith("Bean")) {
			return "INTEGER"; // in this case the column will contain the id that link to the object in the other table
		}

		return null;
	}
}
