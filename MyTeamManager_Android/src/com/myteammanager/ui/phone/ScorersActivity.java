package com.myteammanager.ui.phone;

import android.content.Context;
import org.holoeverywhere.app.Fragment;

import com.myteammanager.R;
import com.myteammanager.specializedStorage.MyTeamManagerDBManager;
import com.myteammanager.ui.fragments.PlayerListForScorerChoice;
import com.myteammanager.ui.fragments.RosterFragment;

public class ScorersActivity extends BaseSinglePaneActivity {

	private final static String LOG_TAG = ScorersActivity.class.getName();

	private PlayerListForScorerChoice m_playerList;

	@Override
	protected Fragment onCreatePane() {
		setTitle(R.string.title_scorers);
		m_playerList = new PlayerListForScorerChoice();
		return m_playerList;
	}

	@Override
	protected void init() {

	}

	@Override
	protected void onDestroy() {
		super.onStop();
		MyTeamManagerDBManager.getInstance().updatePlayersTableWithPresencesAndGoals();
	}
	
	

}
