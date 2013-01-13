package com.myteammanager.ui.phone;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.myteammanager.R;
import com.myteammanager.ui.fragments.AddEventInfoFragment;
import com.myteammanager.ui.fragments.AddPlayerInfoFragment;
import com.myteammanager.ui.fragments.EventDetailFragment;
import com.myteammanager.ui.fragments.PostMatchFragment;

public class PostMatchDetailActivity extends BaseSinglePaneActivity {

	@Override
	protected Fragment onCreatePane() {
		return new PostMatchFragment();
	}

	@Override
	protected void init() {
		setTitle(R.string.title_post_match_facebook);
	}
	
}
