package com.myteammanager.ui.phone;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.myteammanager.R;
import com.myteammanager.ui.fragments.AddEventInfoFragment;
import com.myteammanager.ui.fragments.AddPlayerInfoFragment;
import com.myteammanager.ui.fragments.EventDetailFragment;
import com.myteammanager.ui.fragments.MatchDetailFragment;

public class MatchDetailActivity extends BaseSinglePaneActivity {

	@Override
	protected Fragment onCreatePane() {
		return new MatchDetailFragment();
	}

	@Override
	protected void init() {

	}

}
