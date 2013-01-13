package com.myteammanager.events;

public class TeamLineupSelectionEvent {

	private boolean m_lineupHasBeenDefined = false;
	
	public TeamLineupSelectionEvent() {
		super();
	}

	public TeamLineupSelectionEvent(boolean m_lineupHasBeenDefined) {
		super();
		this.m_lineupHasBeenDefined = m_lineupHasBeenDefined;
	}

	public boolean isLineupHasBeenDefined() {
		return m_lineupHasBeenDefined;
	}

	public void setLineupHasBeenDefined(boolean m_lineupHasBeenDefined) {
		this.m_lineupHasBeenDefined = m_lineupHasBeenDefined;
	}
	
}
