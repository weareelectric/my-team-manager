package com.myteammanager.beans;

import java.util.Comparator;

import android.content.Context;

import com.myteammanager.storage.MethodStructure;
import com.parse.ParseObject;

public abstract class BaseBean {

	protected String m_orderByRule;
	protected String m_selectWhereByRule;
	protected String m_tableNameInJoin;
	protected MethodStructure m_methodStructure;
	protected boolean m_lazy = false;
	protected String m_parseId;

	public BaseBean() {

	}

	public String getTableNameInJoin() {
		return m_tableNameInJoin;
	}

	public void setTableNameInJoin(String tableNameInJoin) {
		this.m_tableNameInJoin = tableNameInJoin;
	}

	public MethodStructure getMethodStructure() {
		return m_methodStructure;
	}

	public void setMethodStructure(MethodStructure methodStructure) {
		this.m_methodStructure = methodStructure;
	}

	public boolean isLazy() {
		return m_lazy;
	}

	public void setLazy(boolean lazy) {
		this.m_lazy = lazy;
	}
	
	public String getParseId() {
		return m_parseId;
	}

	public void setParseId(String parseId) {
		m_parseId = parseId;
	}

	public abstract String getDatabaseTableName();

	public abstract BaseBean getEmptyNewInstance();

	public abstract String orderByRule();

	public abstract Comparator getComparator();
	
	public abstract ParseObject getParseObject(Context context);
	

}
