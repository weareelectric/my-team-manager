package com.myteammanager.events;

import com.myteammanager.beans.MatchBean;

public class EventOrMatchChanged {

	private MatchBean m_match;
	
	public EventOrMatchChanged() {
		
	}

	public EventOrMatchChanged(MatchBean match) {
		super();
		m_match = match;
	}

	public MatchBean getMatch() {
		return m_match;
	}

	public void setMatch(MatchBean match) {
		m_match = match;
	}
}
