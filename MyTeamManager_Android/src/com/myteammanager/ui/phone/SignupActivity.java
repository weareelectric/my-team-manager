package com.myteammanager.ui.phone;

import org.holoeverywhere.app.Fragment;

import com.myteammanager.ui.fragments.SignupFragment;

public class SignupActivity extends BaseSinglePaneActivity {

	@Override
	protected Fragment onCreatePane() {
		return new SignupFragment();
	}

	@Override
	protected void init() {

	}
	

}
