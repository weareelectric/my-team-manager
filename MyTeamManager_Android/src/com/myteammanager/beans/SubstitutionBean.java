package com.myteammanager.beans;

import java.util.Comparator;

import com.parse.ParseObject;

public class SubstitutionBean extends BaseBean {
	
	public final static String KEY_PLAYERIN = "playerIn";
	public final static String KEY_PLAYEROUT = "playerOut";
	public final static String KEY_MATCH = "match";
	
	protected int m_key_id;
	private PlayerBean m_playerIn;
	private PlayerBean m_playerOut;
	private MatchBean m_match;
	private String m_parseId;
	
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
	
	public String getParseId() {
		return m_parseId;
	}

	public void setParseId(String parseId) {
		m_parseId = parseId;
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
	
	public ParseObject getSubstitutionsParseObject() {
		ParseObject substitutionsObj = new ParseObject("Substitutions");
		if (m_parseId !=null) {
			substitutionsObj.setObjectId(m_parseId);
		}
		substitutionsObj.put(KEY_PLAYERIN, getPlayerIn().getPlayerParseObject());
		substitutionsObj.put(KEY_PLAYEROUT, getPlayerOut().getPlayerParseObject());
		substitutionsObj.put(KEY_MATCH, getMatch().getMatchParseObject());
		return substitutionsObj;
	}

}
