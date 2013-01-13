package com.myteammanager.events;

import java.util.ArrayList;

import com.myteammanager.beans.PageBean;
import com.myteammanager.listener.FacebookResponseListener;

public class FacebookPageResponseEvent extends FacebookResponseEvent {

	private ArrayList<PageBean> m_pages;

	public FacebookPageResponseEvent(FacebookResponseListener m_listener) {
		super(m_listener);
	}

	public ArrayList<PageBean> getPages() {
		return m_pages;
	}

	public void setPages(ArrayList<PageBean> m_pages) {
		this.m_pages = m_pages;
	}

}
