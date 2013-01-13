package com.myteammanager.ui.phone;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.myteammanager.R;
import com.myteammanager.specializedStorage.MyTeamManagerDBManager;
import com.myteammanager.ui.fragments.PlayerListForMatchSubstitutions;
import com.myteammanager.ui.fragments.PlayerListForScorerChoice;
import com.myteammanager.ui.fragments.RosterFragment;

public class MatchSubstitutionsActivity extends BaseSinglePaneActivity {

	private final static String LOG_TAG = MatchSubstitutionsActivity.class.getName();

	private PlayerListForMatchSubstitutions m_playerList;

	@Override
	protected Fragment onCreatePane() {
		setTitle(R.string.title_substitutions);
		m_playerList = new PlayerListForMatchSubstitutions();
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
