package com.myteammanager.ui.phone;

import android.content.Context;
import org.holoeverywhere.app.Fragment;

import com.myteammanager.ui.fragments.AddPlayerInfoFromRosterFragment;

public class AddPlayerInfoFromRosterActivity extends AddPlayerInfoActivity {

	@Override
	protected Fragment onCreatePane() {
		super.onCreatePane();
		return new AddPlayerInfoFromRosterFragment();
	}

	@Override
	protected void init() {

	}
	

}
