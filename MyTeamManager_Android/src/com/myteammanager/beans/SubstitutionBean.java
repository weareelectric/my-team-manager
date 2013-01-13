package com.myteammanager.beans;

import java.util.Comparator;

public class SubstitutionBean extends BaseBean {
	
	protected int m_key_id;
	private PlayerBean m_playerIn;
	private PlayerBean m_playerOut;
	private MatchBean m_match;
	
	public SubstitutionBean() {
	}
	
	public SubstitutionBean(PlayerBean m_playerIn, PlayerBean m_playerOut) {
		super();
		this.m_playerIn = m_playerIn;
		this.m_playerOut = m_playerOut;
	}
	
	public int getId() {
		return m_key_id;
	}

	public void setId(int m_id) {
		this.m_key_id = m_id;
	}
	public PlayerBean getPlayerIn() {
		return m_playerIn;
	}

	public void setPlayerIn(PlayerBean m_playerIn) {
		this.m_playerIn = m_playerIn;
	}

	public PlayerBean getPlayerOut() {
		return m_playerOut;
	}

	public void setPlayerOut(PlayerBean m_playerOut) {
		this.m_playerOut = m_playerOut;
	}

	public MatchBean getMatch() {
		return m_match;
	}

	public void setMatch(MatchBean m_match) {
		this.m_match = m_match;
	}

	@Override
	public String getDatabaseTableName() {
		return "MatchSubstitutions";
	}

	@Override
	public BaseBean getEmptyNewInstance() {
		return new SubstitutionBean();
	}

	@Override
	public String orderByRule() {
		return null;
	}

	@Override
	public Comparator getComparator() {
		return null;
	}

}
