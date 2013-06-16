package com.myteammanager.specializedStorage;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.ConvocationBean;
import com.myteammanager.beans.EventBean;
import com.myteammanager.beans.LineupBean;
import com.myteammanager.beans.MatchBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.beans.ScorerBean;
import com.myteammanager.beans.SubstitutionBean;
import com.myteammanager.beans.TeamBean;
import com.myteammanager.storage.DBManager;
import com.myteammanager.storage.exceptions.DBNotInitialized;
import com.myteammanager.util.DateTimeUtil;

public class MyTeamManagerDBManager extends DBManager {
	
	public static final int DB_VERSION = 3;
	
	private static final String SQL_CREATE_VIEW_COMPLETE_PLAYER = "create view players_complete as select players.*, count(MatchSubstitutions.playerIn)+count(lineups.player)  as caps, sum(scorers.scoredgoal) as goals from players  left outer join matches on matches.resultEntered=1 left outer join MatchSubstitutions on MatchSubstitutions.playerIn=players.id and matches.id=MatchSubstitutions.match left outer join lineups on lineups.player=players.id and lineups.onTheBench=0 and lineups.match=matches.id left outer join scorers on scorers.player=players.id and scorers.match=matches.id group by players.id"; 
	private static MyTeamManagerDBManager instance;

	public static final BaseBean[] BEANS = { new PlayerBean(), new TeamBean(), new EventBean(), new MatchBean(),
	new ConvocationBean(), new LineupBean(), new ScorerBean(), new SubstitutionBean() };

	public static final String MYTEAMMANAGER_DB = "myteammanager.db";

	private MyTeamManagerDBManager() {
		super();
		
		m_otherSQLCommandsToExecuteOnCreate.add(SQL_CREATE_VIEW_COMPLETE_PLAYER);
		
		ArrayList<String> updateVersion3 = new ArrayList<String>();
		updateVersion3.add("alter table players add column parseId TEXT");
		m_otherSQLCommandsToExecuteOnUpdate.put(3, updateVersion3);
	}
	
	
	
	public static MyTeamManagerDBManager getInstance() {
		if (instance == null) {
			instance = new MyTeamManagerDBManager();
		}
		else {
			if (m_dbName == null) {
				throw new DBNotInitialized("You must call the init() method at least once before starting using the DBManager.");
			}
		}
		return instance;
	}

	public void updatePlayersTableWithPresencesAndGoals() {
		SQLiteDatabase db = getDatabaseHelper().getWritableDatabase();
		String sql = "update players set gamePlayed=(select caps from players_complete where players.id=id), goalScored=(select goals from players_complete where players.id=id)";
		db.execSQL(sql);
	}
	
}
