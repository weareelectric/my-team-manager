package com.myteammanager.events;

import com.myteammanager.beans.MatchBean;

public class ResultEnteredEvent {

	private MatchBean m_match;
	private boolean m_resultDeleted = true;
	private int m_goalHome = -1;
	private int m_goalAway = -1;

	private int[] m_variationsForStats;

	public ResultEnteredEvent(MatchBean match, boolean resultDeleted, int goalHome, int goalAway) {
		super();
		this.m_match = match;
		this.m_resultDeleted = resultDeleted;
		this.m_goalHome = goalHome;
		this.m_goalAway = goalAway;
	}

	public MatchBean getMatch() {
		return m_match;
	}

	public boolean isResultDeleted() {
		return m_resultDeleted;
	}

	public int getGoalHome() {
		return m_goalHome;
	}

	public int getGoalAway() {
		return m_goalAway;
	}

	public void setVariationsForStats(int[] m_variationsForStats) {
		this.m_variationsForStats = m_variationsForStats;
	}

	public int[] getVariationsForStats() {
		return m_variationsForStats;
	}

}
