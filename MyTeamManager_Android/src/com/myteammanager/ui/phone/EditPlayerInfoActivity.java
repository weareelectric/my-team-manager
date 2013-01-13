package com.myteammanager.ui.phone;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.myteammanager.ui.fragments.EditPlayerInfoFragment;

public class EditPlayerInfoActivity extends AddPlayerInfoActivity {

	@Override
	protected Fragment onCreatePane() {
		return new EditPlayerInfoFragment();
	}
	
}
