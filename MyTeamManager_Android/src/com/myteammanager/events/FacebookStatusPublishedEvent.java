package com.myteammanager.events;

import com.myteammanager.listener.FacebookResponseListener;

public class FacebookStatusPublishedEvent extends FacebookResponseEvent {

	public FacebookStatusPublishedEvent(FacebookResponseListener m_listener) {
		super(m_listener);
	}

}
