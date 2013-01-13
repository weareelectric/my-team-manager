package com.myteammanager.beans;

import java.util.Comparator;

import com.myteammanager.storage.MethodStructure;

public abstract class BaseBean {

	protected String m_orderByRule;
	protected String m_selectWhereByRule;
	protected String m_tableNameInJoin;
	protected MethodStructure m_methodStructure;
	protected boolean m_lazy = false;

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
	
	public abstract String getDatabaseTableName();

	public abstract BaseBean getEmptyNewInstance();

	public abstract String orderByRule();

	public abstract Comparator getComparator();

}
