package com.myteammanager.ui.phone;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.myteammanager.R;
import com.myteammanager.ui.fragments.AddEventInfoFragment;
import com.myteammanager.ui.fragments.AddPlayerInfoFragment;
import com.myteammanager.ui.fragments.EventDetailFragment;

public class EventDetailActivity extends BaseSinglePaneActivity {

	@Override
	protected Fragment onCreatePane() {
		return new EventDetailFragment();
	}

	@Override
	protected void init() {

	}
	

}
