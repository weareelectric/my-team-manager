package com.myteammanager.ui.phone;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.myteammanager.R;
import com.myteammanager.ui.fragments.EventsListFragment;
import com.myteammanager.ui.fragments.MatchesListFragment;
import com.myteammanager.ui.fragments.RosterFragment;

public class MatchesListActivity extends BaseSinglePaneActivity {

	private final static String LOG_TAG = MatchesListActivity.class.getName();

	private MatchesListFragment m_matchFragment;

	@Override
	protected Fragment onCreatePane() {
		setTitle(R.string.title_matches);
		m_matchFragment = new MatchesListFragment();
		return m_matchFragment;
	}

	@Override
	protected void init() {

	}
	
}
