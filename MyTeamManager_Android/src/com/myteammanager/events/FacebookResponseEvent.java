package com.myteammanager.events;

import com.facebook.android.FacebookError;
import com.myteammanager.listener.FacebookResponseListener;

public abstract class FacebookResponseEvent {

	private FacebookResponseListener m_listener;
	private FacebookError m_facebookError;

	public FacebookResponseEvent(FacebookResponseListener m_listener) {
		super();
		this.m_listener = m_listener;
	}

	public FacebookError getFacebookError() {
		return m_facebookError;
	}

	public void setFacebookError(FacebookError m_facebookError) {
		this.m_facebookError = m_facebookError;
	}

	public FacebookResponseListener getLiistener() {
		return m_listener;
	}

}
