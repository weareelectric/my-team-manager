package com.myteammanager.ui.phone;

import android.content.Context;
import org.holoeverywhere.app.Fragment;

import com.myteammanager.R;
import com.myteammanager.ui.fragments.AddEventInfoFragment;
import com.myteammanager.ui.fragments.AddPlayerInfoFragment;
import com.myteammanager.ui.fragments.EventDetailFragment;
import com.myteammanager.ui.fragments.HelpFragment;
import com.myteammanager.ui.fragments.MatchDetailFragment;

public class HelpActivity extends BaseSinglePaneActivity {

	@Override
	protected Fragment onCreatePane() {
		return new HelpFragment();
	}

	@Override
	protected void init() {

	}

}
