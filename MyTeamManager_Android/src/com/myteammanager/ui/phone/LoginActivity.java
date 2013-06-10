package com.myteammanager.ui.phone;

import org.holoeverywhere.app.Fragment;

import android.content.Intent;
import android.util.Log;

import com.myteammanager.ui.fragments.LoginFragment;
import com.parse.ParseFacebookUtils;

public class LoginActivity extends BaseSinglePaneActivity {

	@Override
	protected Fragment onCreatePane() {
		return new LoginFragment();
	}

	@Override
	protected void init() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}
	
	

}
