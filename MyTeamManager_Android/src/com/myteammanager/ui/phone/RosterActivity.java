package com.myteammanager.ui.phone;

import android.content.Context;
import org.holoeverywhere.app.Fragment;

import com.myteammanager.R;
import com.myteammanager.ui.fragments.RosterFragment;

public class RosterActivity extends BaseSinglePaneActivity {

	private final static String LOG_TAG = RosterActivity.class.getName();

	private RosterFragment m_rosterFragment;

	@Override
	protected Fragment onCreatePane() {
		setTitle(R.string.title_team_roster);
		m_rosterFragment = new RosterFragment();
		return m_rosterFragment;
	}

	@Override
	protected void init() {

	}

    

}
