package com.myteammanager.ui.phone;

import android.content.Context;
import org.holoeverywhere.app.Fragment;

import com.myteammanager.R;
import com.myteammanager.ui.fragments.AddEventInfoFragment;
import com.myteammanager.ui.fragments.AddPlayerInfoFragment;
import com.myteammanager.ui.fragments.EventDetailFragment;
import com.myteammanager.ui.fragments.MatchDetailFragment;
import com.myteammanager.ui.fragments.SendMessageFragment;

public class SendMessageActivity extends BaseSinglePaneActivity {

	@Override
	protected Fragment onCreatePane() {
		setTitle(R.string.title_send_message);
		return new SendMessageFragment();
	}

	@Override
	protected void init() {

	}

}
