package com.myteammanager.events;

import java.util.ArrayList;

import com.myteammanager.beans.ScorerBean;

public class ScorersChangeEvent {

	private ArrayList<ScorerBean> m_scorers;

	public ScorersChangeEvent() {
		super();
	}

	public ScorersChangeEvent(ArrayList<ScorerBean> m_scorers) {
		super();
		this.m_scorers = m_scorers;
	}

	public ArrayList<ScorerBean> getScorers() {
		return m_scorers;
	}

}
