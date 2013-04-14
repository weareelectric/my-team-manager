package com.myteammanager.ui.phone;

import android.content.Context;
import android.os.Bundle;
import org.holoeverywhere.app.Fragment;

import com.myteammanager.R;
import com.myteammanager.beans.MatchBean;
import com.myteammanager.ui.fragments.PlayerListForScorerChoice;
import com.myteammanager.ui.fragments.PlayerListForTeamLineupFragment;
import com.myteammanager.ui.fragments.RosterFragment;
import com.myteammanager.util.KeyConstants;

public class PlayerListForTeamLineupActivity extends BaseSinglePaneActivity {

	private final static String LOG_TAG = PlayerListForTeamLineupActivity.class.getName();

	private PlayerListForTeamLineupFragment m_playersForLineup;

	@Override
	protected Fragment onCreatePane() {
		Bundle bundle = getIntent().getExtras();

		MatchBean matchBean = (MatchBean) bundle.get(KeyConstants.KEY_MATCH);
		setTitle(matchBean.getTeam1StringToShow(this) + " - " + matchBean.getTeam2StringToShow(this));

		m_playersForLineup = new PlayerListForTeamLineupFragment();
		return m_playersForLineup;
	}

	@Override
	protected void init() {

	}
	

}
