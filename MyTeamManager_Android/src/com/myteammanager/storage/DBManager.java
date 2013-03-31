package com.myteammanager.storage;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.myteammanager.beans.BaseBean;
import com.myteammanager.storage.exceptions.DBNotInitialized;
import com.myteammanager.storage.reflection.ReflectionManagerForDB;
import com.myteammanager.storage.util.SQLStringUtil;
import com.myteammanager.ui.fragments.BaseFragment;
import com.myteammanager.util.StringUtil;

public class DBManager {

	private static String LOG_TAG = DBManager.class.getName();

	private static String MAPPING_TABLE_NAME = "beansmapping";

	private static String MAPPING_TABLE_COLUMN_BEANS_NAME = "beanName";
	private static String MAPPING_TABLE_COLUMN_BEANS_FIELD = "beanField";
	private static String MAPPING_TABLE_COLUMN_BEANS_METHOD_TYPE = "beanMethodType";
	private static String MAPPING_TABLE_COLUMN_BEANS_UNIQUE = "beanUnique";
	private static String MAPPING_TABLE_COLUMN_BEANS_COLINDEX = "colIndex";

	private static String[] MAPPING_TABLE_COLUMNS = { MAPPING_TABLE_COLUMN_BEANS_NAME, MAPPING_TABLE_COLUMN_BEANS_FIELD,
			MAPPING_TABLE_COLUMN_BEANS_METHOD_TYPE, MAPPING_TABLE_COLUMN_BEANS_UNIQUE, MAPPING_TABLE_COLUMN_BEANS_COLINDEX };

	private static DBManager instance;
	private static DatabaseHelper m_dbHelper;
	private static SQLiteDatabase m_db;

	private static BaseBean[] m_beans;
	private static int m_dbVersion;
	
	protected static String m_dbName;
	protected static Hashtable<Integer, ArrayList<String>> m_otherSQLCommandsToExecuteOnUpdate = new Hashtable<Integer, ArrayList<String>>();
	protected static ArrayList<String> m_otherSQLCommandsToExecuteOnCreate = new ArrayList<String>();
	

	private static ArrayList<MethodStructure> m_mappingToStore;

	public void init(Context context, BaseBean[] beans, String dbName, int version) {
		m_dbName = dbName;
		m_dbVersion = version;
		m_beans = beans;

		m_dbHelper = new DatabaseHelper(context);

		m_db = m_dbHelper.getWritableDatabase();

		TableBeansMappingManager.init(context);

	}

	public static DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();
		}
		else {
			if (m_dbName == null) {
				throw new DBNotInitialized("You must call the init() method at least once before starting using the DBManager.");
			}
		}
		// Log.d(LOG_TAG, "DB version: " + m_dbVersion);
		return instance;
	}

	public void closeDB() {
		m_db = m_dbHelper.getWritableDatabase();
		m_db.close();
		m_db = null;
	}

	public void deleteAllBeansFor(BaseBean emptyBean) {
		m_db = m_dbHelper.getWritableDatabase();
		m_db.delete(emptyBean.getDatabaseTableName(), null, null);

	}

	public void deleteBean(BaseBean baseBean) {

		m_db = m_dbHelper.getWritableDatabase();
		String where = getWhereClauseFor(baseBean);
		m_db.delete(baseBean.getDatabaseTableName(), where, null);

	}

	public void deleteBeanWithWhere(BaseBean baseBean, String whereClause) {

		Log.d(LOG_TAG, "where clause: " + whereClause);
		m_db = m_dbHelper.getWritableDatabase();
		m_db.delete(baseBean.getDatabaseTableName(), whereClause, null);

	}

	public String getWhereClauseFor(BaseBean baseBean) {
		String columnNameForIndex = TableBeansMappingManager.getColumnIndex(baseBean);
		String where = columnNameForIndex + "=" + ReflectionManagerForDB.getIndexValue(baseBean, columnNameForIndex);
		return where;
	}

	public void updateBean(BaseBean baseBean) {
		m_db = m_dbHelper.getWritableDatabase();
		ContentValues initialValues = createContentValuesFor(baseBean);
		String where = getWhereClauseFor(baseBean);
		m_db.update(baseBean.getDatabaseTableName(), initialValues, where, null);

	}

	public long storeBean(BaseBean bean) {
		m_db = m_dbHelper.getWritableDatabase();
		long id = insertBean(bean, m_db);//

		ReflectionManagerForDB.setIndexValue(bean, TableBeansMappingManager.getColumnIndex(bean), (int) id);

		return id;
	}

	private long insertBean(BaseBean bean, SQLiteDatabase db) {
		ContentValues initialValues = createContentValuesFor(bean);

		long returnedId = -1;

		String databaseTableName = bean.getDatabaseTableName();

		try {

			returnedId = db.insertOrThrow(databaseTableName, null, initialValues);
		} catch (SQLiteConstraintException e) {
			// Check if the bean has a unique column and if yes, try to select the record using the column unique
			ArrayList<MethodStructure> uniqueFields = TableBeansMappingManager.getFieldUnique(bean);

			String whereForUniqueFields = TableBeansMappingManager.getWhereClauseForFields(uniqueFields, bean);
			Log.d(LOG_TAG, "Where clause is '" + whereForUniqueFields + "'");

			Cursor cursor = db.query(databaseTableName, TableBeansMappingManager.getSQLColumns(bean), whereForUniqueFields,
					null, null, null, null);
			if (cursor.moveToFirst()) {
				populateWithCursor(cursor, bean, false);
			}
			return ReflectionManagerForDB.getIndexValue(bean, TableBeansMappingManager.getColumnIndex(bean));

		}

		return returnedId;
	}

	public ContentValues createContentValuesFor(BaseBean bean) {
		ContentValues initialValues = new ContentValues();

		// First get the fields associated to other beans object that needs to be stored
		ArrayList<MethodStructure> beanFields = TableBeansMappingManager.getSQLFieldsForBeans(bean);
		int beanFieldSize = beanFields.size();

		// Store all the child beans if their id is not present: that will update the index of the child bean object
		for (MethodStructure method : beanFields) {
			BaseBean beanObjectForField = ReflectionManagerForDB.getBeanObjectForField(bean, method);
			if (beanObjectForField != null) {
				storeChildBeanIfNecessaryAndCreateContentValues(bean, initialValues, method, beanObjectForField);

			} else {
				// no reference for this bean -> set index to -1
				initialValues.put(TableBeansMappingManager.getColumnName(method.getFieldName()), -1);
			}

		}

		// Now we store other fields that don't need reference with other table
		ArrayList<MethodStructure> fields = TableBeansMappingManager.getSQLFieldsNoBean(bean);
		int size = fields.size();

		for (MethodStructure method : fields) {
			ReflectionManagerForDB.putValueForFieldInContentValues(initialValues,
					TableBeansMappingManager.getColumnName(method.getFieldName()), method, bean);
		}
		return initialValues;
	}

	private void storeChildBeanIfNecessaryAndCreateContentValues(BaseBean bean, ContentValues initialValues,
			MethodStructure method, BaseBean beanObjectForField) {
		long existentId = ReflectionManagerForDB.getIndexValue(beanObjectForField,
				TableBeansMappingManager.getColumnIndex(bean));

		if (existentId <= 0) {
			long idStored = storeBean(beanObjectForField);

			// Immediate update of index of the child bean stored 
			ReflectionManagerForDB.setIndexValue(beanObjectForField, TableBeansMappingManager.getColumnIndex(bean),
					(int) idStored);
			ReflectionManagerForDB.setChildBeanIn(bean, beanObjectForField, method);

			Log.d(LOG_TAG, "Set id for child bean object stored in the db: " + idStored);
			ReflectionManagerForDB.putValueForFieldInContentValues(initialValues,
					TableBeansMappingManager.getColumnName(method.getFieldName()), method, bean);
		} else {
			Log.d(LOG_TAG, "Set id for child bean object already stored in the db: " + existentId);
			ReflectionManagerForDB.putValueForFieldInContentValues(initialValues,
					TableBeansMappingManager.getColumnName(method.getFieldName()), method, bean);
		}
	}

	private void storeChildBeanIfNecessaryAndPopulateHelper(BaseBean bean, InsertHelper helper, MethodStructure method,
			BaseBean beanObjectForField) {
		long existentId = ReflectionManagerForDB.getIndexValue(beanObjectForField,
				TableBeansMappingManager.getColumnIndex(bean));

		if (existentId <= 0) {
			long idStored = storeBean(beanObjectForField);

			// Immediate update of index of the child bean stored 
			ReflectionManagerForDB.setIndexValue(beanObjectForField, TableBeansMappingManager.getColumnIndex(bean),
					(int) idStored);
			ReflectionManagerForDB.setChildBeanIn(bean, beanObjectForField, method);

			Log.d(LOG_TAG, "Set id for child bean object stored in the db: " + idStored);
			ReflectionManagerForDB.putValueForFieldInInsertHelper(helper,
					TableBeansMappingManager.getColumnName(method.getFieldName()), method, bean);
		} else {
			Log.d(LOG_TAG, "Set id for child bean object already stored in the db: " + existentId);
			ReflectionManagerForDB.putValueForFieldInInsertHelper(helper,
					TableBeansMappingManager.getColumnName(method.getFieldName()), method, bean);
		}
	}


	public void updateBeans(final ArrayList<? extends BaseBean> beans) {
		insertBeans(beans, true);
	}

	public void insertBeans(final ArrayList<? extends BaseBean> beans) {
		insertBeans(beans, false);
	}

	private void insertBeans(final ArrayList<? extends BaseBean> beans, final boolean isUpdate) {

		if (beans == null || beans.size() == 0)
			return;

		String databaseTableName = beans.get(0).getDatabaseTableName();

		InsertHelper helper = new InsertHelper(m_db, databaseTableName);

		try {
			for (BaseBean bean : beans) {
				if (isUpdate) {
					helper.prepareForReplace();
				} else {
					helper.prepareForInsert();
				}

				// First get the fields associated to other beans object that needs to be stored
				ArrayList<MethodStructure> beanFields = TableBeansMappingManager.getSQLFieldsForBeans(bean);

				// Store all the child beans if their id is not present: that will update the index of the child bean object
				for (MethodStructure method : beanFields) {
					BaseBean beanObjectForField = ReflectionManagerForDB.getBeanObjectForField(bean, method);
					if (beanObjectForField != null) {
						storeChildBeanIfNecessaryAndPopulateHelper(bean, helper, method, beanObjectForField);

					} else {
						// no reference for this bean -> set index to -1
						helper.bind(method.getColumnIndex(), -1);
					}

				}

				// Now we store other fields that don't need reference with other table
				ArrayList<MethodStructure> fields = TableBeansMappingManager.getSQLFieldsNoBean(bean);
				int size = fields.size();

				for (MethodStructure method : fields) {
					ReflectionManagerForDB.putValueForFieldInInsertHelper(helper,
							TableBeansMappingManager.getColumnName(method.getFieldName()), method, bean);
				}

				helper.execute();
			}
		} finally {
			helper.close();
		}

	}

	protected SQLiteDatabase getWritableDatabase() {
		return m_dbHelper.getWritableDatabase();
	}

	protected DatabaseHelper getDatabaseHelper() {
		return m_dbHelper;
	}

	public ArrayList<? extends BaseBean> getListOfBeansWhere(BaseBean bean, String where, boolean lazy, String limit) {

		long timeStart = System.currentTimeMillis();
		ArrayList<BaseBean> list = new ArrayList<BaseBean>();

		m_db = m_dbHelper.getWritableDatabase();

		Cursor cursor = m_db.query(bean.getDatabaseTableName(), TableBeansMappingManager.getSQLColumns(bean), where, null,
				null, null, bean.orderByRule(), limit);

		if (cursor != null && cursor.moveToFirst()) {

			do {
				BaseBean beanNewObj = bean.getEmptyNewInstance();
				//				beanNewObj.populateWithCursor(cursor);
				populateWithCursor(cursor, beanNewObj, lazy);
				list.add(beanNewObj);
			} while (cursor.moveToNext());

		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		Log.d(LOG_TAG, "Tempo totale: " + (System.currentTimeMillis() - timeStart));

		return list;

	}

	public ArrayList<? extends BaseBean> getListOfBeansWhere(BaseBean bean, String where, boolean lazy) {
		return getListOfBeansWhere(bean, where, lazy, null);
	}

	public ArrayList<? extends BaseBean> getListOfBeans(BaseBean bean, boolean lazy, String limit) {
		return getListOfBeansWhere(bean, null, lazy, limit);

	}

	public ArrayList<? extends BaseBean> getListOfBeans(BaseBean bean, boolean lazy) {
		return getListOfBeansWhere(bean, null, lazy, null);

	}

	private void populateWithCursor(Cursor cursor, BaseBean baseBean, boolean lazy) {

		ArrayList<MethodStructure> fields = TableBeansMappingManager.getSQLFields(baseBean);

		int k = 0;
		for (MethodStructure method : fields) {
			if (TableBeansMappingManager.isABean(method.getType())) {
				int valueIdForChildBean = cursor.getInt(k);
				// Log.d(LOG_TAG, "Searching bean for field: " + method.getFieldName() + " with index " + valueIdForChildBean);

				if (lazy) {
					// Load a child object only with the index
					BaseBean lazyChild = ReflectionManagerForDB.getEmptyObjectForBeanField(method);
					lazyChild.setLazy(true);
					ReflectionManagerForDB.setIndexValue(lazyChild, TableBeansMappingManager.getColumnIndex(lazyChild),
							valueIdForChildBean);
					ReflectionManagerForDB.setChildBeanIn(baseBean, lazyChild, method);

				} else {
					BaseBean beanObjectForField = ReflectionManagerForDB.getBeanObjectForField(baseBean, method);
					if (beanObjectForField != null) {
						// Log.d(LOG_TAG, "Bean found: " + beanObjectForField);
						BaseBean childBean = getBean(beanObjectForField, valueIdForChildBean);
						if (childBean != null) {
							ReflectionManagerForDB.setChildBeanIn(baseBean, childBean, method);
						}
					} else {
						// Get field loading from db using the id

						beanObjectForField = ReflectionManagerForDB.getEmptyObjectForBeanField(method);
						// Log.d(LOG_TAG, "Bean found: " + beanObjectForField);
						BaseBean childBean = getBean(beanObjectForField, valueIdForChildBean);
						if (childBean != null) {
							ReflectionManagerForDB.setChildBeanIn(baseBean, childBean, method);
						}

					}
				}

			} else {
				ReflectionManagerForDB.populateObjectFieldWithCursorValue(baseBean, cursor, method);
			}

			k++;
		}

	}

	public BaseBean getBean(BaseBean emptyBean, int id) {

		BaseBean bean = null;

		m_db = m_dbHelper.getWritableDatabase();

		String indexColumn = TableBeansMappingManager.getColumnIndex(emptyBean);

		Cursor cursor = m_db.query(emptyBean.getDatabaseTableName(), TableBeansMappingManager.getSQLColumns(emptyBean),
				indexColumn + "=?", new String[] { new Long(id).toString() }, null, null, null);

		if (cursor != null && cursor.moveToFirst()) {
			bean = emptyBean.getEmptyNewInstance();
			populateWithCursor(cursor, bean, false);
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return bean;

	}

	public ArrayList<MethodStructure> getBeansMapping() {
		ArrayList<MethodStructure> list = new ArrayList<MethodStructure>();

		m_db = m_dbHelper.getWritableDatabase();

		Cursor cursor = m_db.query(MAPPING_TABLE_NAME, MAPPING_TABLE_COLUMNS, null, null, null, null, null);

		if (cursor != null && cursor.moveToFirst()) {

			do {
				MethodStructure method = new MethodStructure(cursor.getString(0), cursor.getString(1), cursor.getString(2),
						cursor.getInt(4));
				method.setUnique(cursor.getInt(3));
				list.add(method);
			} while (cursor.moveToNext());

		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return list;
	}

	public static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, m_dbName, null, m_dbVersion);
		}

		private void insertBeanMapping(ArrayList<MethodStructure> methods, SQLiteDatabase db) {

			String databaseTableName = MAPPING_TABLE_NAME;

			InsertHelper helper = new InsertHelper(db, databaseTableName);

			try {
				for (MethodStructure method : methods) {
					helper.prepareForInsert();
					helper.bind(1, method.getClassName());
					helper.bind(2, method.getFieldName());
					helper.bind(3, method.getType());
					helper.bind(4, method.getUnique());
					helper.bind(5, method.getColumnIndex());
					helper.execute();
				}
			} finally {
				helper.close();
			}

		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			Log.d(LOG_TAG, "Creating database");

			int numberOfTables = m_beans.length;

			// Create table to stare mapping between database and beans
			db.execSQL("CREATE TABLE " + MAPPING_TABLE_NAME + "( " + MAPPING_TABLE_COLUMN_BEANS_NAME + " TEXT, "
					+ MAPPING_TABLE_COLUMN_BEANS_FIELD + " TEXT, " + MAPPING_TABLE_COLUMN_BEANS_METHOD_TYPE + " TEXT, "
					+ MAPPING_TABLE_COLUMN_BEANS_UNIQUE + " INTEGER, " + MAPPING_TABLE_COLUMN_BEANS_COLINDEX + " INTEGER );");

			for (int i = 0; i < numberOfTables; i++) {
				db.execSQL(createSQLCreateCommandFor(m_beans[i]));
			}

			insertBeanMapping(m_mappingToStore, db);
			
			for ( String sqlCommand : m_otherSQLCommandsToExecuteOnCreate ) {
				Log.d(LOG_TAG, "Other command executed in onCreate(): " + sqlCommand);
				db.execSQL(sqlCommand);
			}

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			Log.d(LOG_TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
			for ( int k = oldVersion + 1; k <= newVersion; k++ ) {
				ArrayList<String> sqlCommands = m_otherSQLCommandsToExecuteOnUpdate.get(k);
				for ( String sqlCommand : sqlCommands ) {
					db.execSQL(sqlCommand);
				}
			}
			
		}

	}
	
	private static String createSQLCreateCommandFor(BaseBean bean) {
		ArrayList<Field> fields = ReflectionManagerForDB.getSQLFields(bean);
		if (m_mappingToStore == null) {
			m_mappingToStore = new ArrayList<MethodStructure>();
		}

		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE ");
		sb.append(bean.getDatabaseTableName());
		sb.append("( ");
		int size = fields.size();

		Vector<String> uniqueColumn = new Vector<String>();

		for (int k = 0; k < size; k++) {
			Field field = fields.get(k);
			sb.append(TableBeansMappingManager.getSQLForColumn(field));

			String sqlTypeForField = field.getType().getName();
			String name = bean.getClass().getSimpleName();
			MethodStructure methodStruct = new MethodStructure(name, field.getName(), sqlTypeForField, k + 1);

			if (TableBeansMappingManager.isUniqueColumn(field.getName())) {
				uniqueColumn.add(TableBeansMappingManager.getColumnName(field.getName()));
				methodStruct.setUnique(MethodStructure.UNIQUE);
			}

			m_mappingToStore.add(methodStruct);

			if (k != size - 1) {
				sb.append(",");
			}
		}

		int vectorSize = uniqueColumn.size();
		if (vectorSize > 0) {
			sb.append(", UNIQUE(");

			for (int k = 0; k < vectorSize; k++) {
				sb.append(uniqueColumn.get(k));

				if (k != vectorSize - 1) {
					sb.append(",");
				}
			}

			// sb.append(") ON CONFLICT IGNORE");
			sb.append(")");
		}

		sb.append(" );");

		String sqlCmd = sb.toString();
		Log.d(LOG_TAG, "command to create the table: " + sqlCmd);

		return sb.toString();
	}
}