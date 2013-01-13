package com.myteammanager.beans;

import java.util.ArrayList;

public class PageBean {

	private String m_idPage;
	private String m_name;

	public PageBean() {
	}

	public PageBean(String m_idPage, String m_name) {
		super();
		this.m_idPage = m_idPage;
		this.m_name = m_name;
	}

	public String getIdPage() {
		return m_idPage;
	}

	public void setIdPage(String m_idPage) {
		this.m_idPage = m_idPage;
	}

	public String getName() {
		return m_name;
	}

	public void setName(String m_name) {
		this.m_name = m_name;
	}

	public static String[] getPages(ArrayList<PageBean> pageBeans) {
		String[] pages = null;
		if (pageBeans != null) {
			int size = pageBeans.size();
			pages = new String[size];
			for (int k = 0; k < size; k++) {
				pages[k] = pageBeans.get(k).getName();
			}
		} else {
			pages = new String[0];
		}
		return pages;
	}

}
