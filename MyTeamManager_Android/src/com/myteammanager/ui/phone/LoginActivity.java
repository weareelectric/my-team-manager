package com.myteammanager.ui.phone;

import org.holoeverywhere.app.Fragment;

import com.myteammanager.ui.fragments.LoginFragment;

public class LoginActivity extends BaseSinglePaneActivity {

	@Override
	protected Fragment onCreatePane() {
		return new LoginFragment();
	}

	@Override
	protected void init() {

	}
	

}
