package com.myteammanager.data;

import java.util.ArrayList;
import java.util.List;

import com.myteammanager.beans.PlayerBean;
import com.myteammanager.beans.TeamBean;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ParseObjectManager {

	private static ParseObjectManager m_manager;
	
	public static ParseObjectManager getInstance() {
		if ( m_manager == null ) {
			m_manager = new ParseObjectManager();
		}
		
		return m_manager;
	}
	
	public ArrayList<PlayerBean> getParseRosterList(ParseObject teamObject) throws ParseException {
		ParseQuery<ParseObject> query = ParseQuery
				.getQuery(PlayerBean.PARSE_PLAYER_OBJECT);
		query.whereEqualTo(PlayerBean.KEY_IS_DELETED, 0);
		query.whereEqualTo(PlayerBean.KEY_TEAM, teamObject);
		return getPlayers(query.find());
	}
	
	private ArrayList<PlayerBean> getPlayers(List<ParseObject> objects) {
		ArrayList<PlayerBean> players = new ArrayList<PlayerBean>();
		
		for ( ParseObject object : objects  ) {
			players.add(PlayerBean.getBeanFrom(object));
		}
		
		return players;
	}
}
