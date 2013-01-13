package com.myteammanager.storage.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.util.Log;

import com.myteammanager.beans.BaseBean;
import com.myteammanager.storage.MethodStructure;
import com.myteammanager.storage.TableBeansMappingManager;
import com.myteammanager.storage.util.SQLStringUtil;
import com.myteammanager.util.DateTimeUtil;
import com.myteammanager.util.StringUtil;

public class ReflectionManagerForDB {

	public static final String M_KEY = "m_key_";

	public static final String LOG_TAG = ReflectionManagerForDB.class.getName();

	public static void setChildBeanIn(BaseBean bean, BaseBean childBean, MethodStructure childBeanMethodStruct) {
		Class c = bean.getClass();
		String type = childBeanMethodStruct.getType();
		String columnName = TableBeansMappingManager.getColumnName(childBeanMethodStruct.getFieldName());
		String methodName = "set" + SQLStringUtil.getStringWithFirstCapitalLetter(columnName);

		// Log.d(LOG_TAG, "Trying to invoke methodName: " + methodName + " for " + bean + " with child " + childBean);

		try {
			Method method = c.getMethod(methodName, new Class[] { childBean.getClass() });
			method.invoke(bean, new Object[] { childBean });
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static void populateObjectFieldWithCursorValue(Object bean, Cursor cursor, MethodStructure methodStruct) {
		Class c = bean.getClass();
		String type = methodStruct.getType();
		String columnName = TableBeansMappingManager.getColumnName(methodStruct.getFieldName());
		String methodName = "set" + SQLStringUtil.getStringWithFirstCapitalLetter(columnName);

		// Log.d(LOG_TAG, "Trying to invoke methodName: " + methodName);

		int indexOfCoulumn = cursor.getColumnIndex(TableBeansMappingManager.getColumnName(methodStruct.getFieldName()));

		try {
			if (type.equals("int")) {
				Method method = c.getMethod(methodName, new Class[] { int.class });
				method.invoke(bean, new Object[] { new Integer(cursor.getInt(indexOfCoulumn)) });
			}
			if (type.equals("long")) {
				Method method = c.getMethod(methodName, new Class[] { long.class });
				method.invoke(bean, new Object[] { new Long(cursor.getLong(indexOfCoulumn)) });
			} else if (type.equals("java.lang.String")) {
				Method method = c.getMethod(methodName, new Class[] { String.class });
				method.invoke(bean, new Object[] { cursor.getString(indexOfCoulumn) });
			} else if (type.equals("java.util.Date")) {
				Method method = c.getMethod(methodName, new Class[] { Date.class });
				method.invoke(bean, new Object[] { DateTimeUtil.getDateFromLong(cursor.getLong(indexOfCoulumn)) });
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static BaseBean getBeanObjectForField(BaseBean parentBean, MethodStructure methodStruct) {

		try {

			BaseBean childBean = (BaseBean) getValueForField(parentBean, methodStruct);

			return childBean;
		} catch (IllegalArgumentException e) {
			Log.e(LOG_TAG, "Error", e);
		} catch (NoSuchMethodException e) {
			Log.e(LOG_TAG, "Error", e);
		} catch (IllegalAccessException e) {
			Log.e(LOG_TAG, "Error", e);
		} catch (Throwable e) {
			Log.e(LOG_TAG, "Error", e);
		}
		return null;
	}

	public static Object getValueForField(BaseBean parentBean, MethodStructure methodStruct)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Class c = parentBean.getClass();
		String type = methodStruct.getType();
		String columnName = TableBeansMappingManager.getColumnName(methodStruct.getFieldName());
		String methodName = "get" + SQLStringUtil.getStringWithFirstCapitalLetter(columnName);

		// Log.d(LOG_TAG, "parentBean " + parentBean);
		Method method = c.getMethod(methodName, null);
		// Log.d(LOG_TAG, "Calling method: " + method.getName() + " on parentBean " + parentBean);
		Object object = method.invoke(parentBean, new Object[] {});

		// Log.d(LOG_TAG, "Returned object " + object);

		return object;
	}

	public static BaseBean getEmptyObjectForBeanField(MethodStructure method) {
		try {
			return (BaseBean) Class.forName(method.getType()).newInstance();
		} catch (InstantiationException e) {
			Log.e(LOG_TAG, "Error", e);
		} catch (IllegalAccessException e) {
			Log.e(LOG_TAG, "Error", e);
		} catch (ClassNotFoundException e) {
			Log.e(LOG_TAG, "Error", e);
		}
		return null;
	}

	public static void putValueForFieldInContentValues(ContentValues initialValues, String columnName,
			MethodStructure methodStruct, Object bean) {
		try {
			Class c = bean.getClass();
			String type = methodStruct.getType();
			String methodName = "get" + SQLStringUtil.getStringWithFirstCapitalLetter(columnName);

			if (SQLStringUtil.isPrimaryKey(methodStruct.getFieldName())) {
				// Primary key is not valorized because autoincrement
				return;
			}

			Method method = c.getMethod(methodName);

			// if the field is a Bean then get the id and store it
			if (TableBeansMappingManager.isABean(methodStruct.getType())) {
				BaseBean childBean = (BaseBean) method.invoke(bean, new Object[] {});
				int childBeanIndex = getIndexValue(childBean, TableBeansMappingManager.getColumnIndex(childBean));
				initialValues.put(columnName, childBeanIndex);
				return;
			}

			// Log.d(LOG_TAG, "Trying to invoke methodName: " + methodName);

			if (type.equals("int")) {
				initialValues.put(columnName, ((Integer) method.invoke(bean, new Object[] {})).intValue());
			} else if (type.equals("long")) {
				initialValues.put(columnName, ((Long) method.invoke(bean, new Object[] {})).longValue());
			} else if (type.equals("java.lang.String")) {
				initialValues.put(columnName, (String) method.invoke(bean, new Object[] {}));
			} else if (type.equals("java.util.Date")) {
				initialValues.put(columnName, DateTimeUtil.getDateLong((Date) method.invoke(bean, new Object[] {})));
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static void putValueForFieldInInsertHelper(InsertHelper helper, String columnName,
			MethodStructure methodStruct, Object bean) {
		try {
			Class c = bean.getClass();
			String type = methodStruct.getType();
			String methodName = "get" + SQLStringUtil.getStringWithFirstCapitalLetter(columnName);

			if (SQLStringUtil.isPrimaryKey(methodStruct.getFieldName())) {
				// Primary key is not valorized because autoincrement
				return;
			}

			Method method = c.getMethod(methodName);

			// if the field is a Bean then get the id and store it
			if (TableBeansMappingManager.isABean(methodStruct.getType())) {
				BaseBean childBean = (BaseBean) method.invoke(bean, new Object[] {});
				int childBeanIndex = getIndexValue(childBean, TableBeansMappingManager.getColumnIndex(childBean));
				helper.bind(methodStruct.getColumnIndex(), childBeanIndex);
				return;
			}

			// Log.d(LOG_TAG, "Trying to invoke methodName: " + methodName);

			if (type.equals("int")) {
				helper.bind(methodStruct.getColumnIndex(), ((Integer) method.invoke(bean, new Object[] {})).intValue());
			} else if (type.equals("long")) {
				helper.bind(methodStruct.getColumnIndex(), ((Long) method.invoke(bean, new Object[] {})).longValue());
			} else if (type.equals("java.lang.String")) {
				helper.bind(methodStruct.getColumnIndex(), (String) method.invoke(bean, new Object[] {}));
			} else if (type.equals("java.util.Date")) {
				helper.bind(methodStruct.getColumnIndex(),
						DateTimeUtil.getDateLong((Date) method.invoke(bean, new Object[] {})));
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static int getIndexValue(BaseBean bean, String columnName) {
		Class c = bean.getClass();
		Method[] methods = c.getMethods();
		Log.d(LOG_TAG, "Get the index value for the column " + columnName);
		String methodName = "get" + SQLStringUtil.getStringWithFirstCapitalLetter(columnName);

		try {
			Method method = c.getMethod(methodName);
			return ((Integer) method.invoke(bean, new Object[] {})).intValue();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return -1;
	}

	public static int setIndexValue(BaseBean bean, String columnName, int index) {
		Class c = bean.getClass();
		String methodName = "set" + SQLStringUtil.getStringWithFirstCapitalLetter(columnName);

		try {
			Method method = c.getMethod(methodName, new Class[] { int.class });
			// Log.d(LOG_TAG, "Set id for object " + bean + " id " + index);
			method.invoke(bean, new Object[] { new Integer(index) });
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return -1;
	}

	public static ArrayList<Field> getSQLFields(Object object) {
		Class c = object.getClass();
		Field[] fields = c.getDeclaredFields();
		ArrayList<Field> sqlfields = new ArrayList<Field>();
		for (int i = 0; i < fields.length; i++) {
			Log.d(LOG_TAG, "exploring field " + fields[i].getName());
			if (SQLStringUtil.isTableColumn(fields[i].getName())) {
				Log.d(LOG_TAG, "field added");
				sqlfields.add(fields[i]);
			}
		}
		return sqlfields;
	}

	public static String[] getSQLColumns(Object object) {
		Class c = object.getClass();
		Field[] fields = c.getDeclaredFields();
		ArrayList<String> columns = new ArrayList<String>();
		for (int i = 0; i < fields.length; i++) {
			if (SQLStringUtil.isTableColumn(fields[i].getName())) {
				Log.d(LOG_TAG, "Getting column name for: " + fields[i].getName());
				columns.add(TableBeansMappingManager.getColumnName(fields[i].getName()));
			}
		}

		int numberOfFoundColoumn = columns.size();
		String[] colArray = new String[numberOfFoundColoumn];

		for (int i = 0; i < numberOfFoundColoumn; i++) {
			colArray[i] = columns.get(i);
		}

		return colArray;
	}

}
