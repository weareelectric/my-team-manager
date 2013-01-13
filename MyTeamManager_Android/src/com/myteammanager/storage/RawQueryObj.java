package com.myteammanager.storage;

import java.util.ArrayList;

import com.myteammanager.beans.BaseBean;

public class RawQueryObj {

	private String m_rawQuery = "";

	private ArrayList<BaseBean> m_beansFromJoin = new ArrayList<BaseBean>();

	public String getRawQuery() {
		return m_rawQuery;
	}

	public void setRawQuery(String m_rawQuery) {
		this.m_rawQuery = m_rawQuery;
	}

	public void addJoinBean(BaseBean bean) {
		m_beansFromJoin.add(bean);
	}

	public ArrayList<BaseBean> getBeansFromJoin() {
		return m_beansFromJoin;
	}

}
