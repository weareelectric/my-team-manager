package com.myteammanager.beans;

import java.util.Comparator;

import android.content.ContentValues;
import android.database.Cursor;

public class MenuBean extends BaseBean {

	public String m_label;
	
	public MenuBean(String m_label) {
		super();
		this.m_label = m_label;
	}

	public String getLabel() {
		return m_label;
	}

	public void setLabel(String label) {
		this.m_label = label;
	}

	@Override
	public String getDatabaseTableName() {
		return null;
	}

	@Override
	public BaseBean getEmptyNewInstance() {
		return null;
	}

	@Override
	public String orderByRule() {
		return null;
	}

	@Override
	public Comparator getComparator() {
		// TODO Auto-generated method stub
		return null;
	}

}
