package com.myteammanager.ui.phone;

import org.holoeverywhere.app.Fragment;

import com.myteammanager.ui.fragments.SignupFragment;

public class SignupActivity extends BaseSinglePaneActivity {
	
	public static final String EXTRA_SHOW_MESSAGE_FOR_OLD_USERS = "showOldUserMsg";

	@Override
	protected Fragment onCreatePane() {
		return new SignupFragment();
	}

	@Override
	protected void init() {

	}
	

}
