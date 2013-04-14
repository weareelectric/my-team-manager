package com.myteammanager.ui.phone;

import android.content.Context;
import org.holoeverywhere.app.Fragment;

import com.myteammanager.R;
import com.myteammanager.ui.fragments.AddEventInfoFragment;
import com.myteammanager.ui.fragments.AddPlayerInfoFragment;
import com.myteammanager.ui.fragments.EventDetailFragment;
import com.myteammanager.ui.fragments.MatchDetailFragment;
import com.myteammanager.ui.fragments.SendMessageFacebookFragment;
import com.myteammanager.ui.fragments.SendMessageFragment;

public class SendMessageFacebookActivity extends BaseSinglePaneActivity {

	@Override
	protected Fragment onCreatePane() {
		setTitle(R.string.title_send_free_text_facebook);
		return new SendMessageFacebookFragment();
	}

	@Override
	protected void init() {

	}

}
