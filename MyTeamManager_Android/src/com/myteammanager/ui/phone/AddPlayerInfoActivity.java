package com.myteammanager.ui.phone;

import android.content.Context;
import org.holoeverywhere.app.Fragment;

import com.myteammanager.R;
import com.myteammanager.ui.fragments.AddPlayerInfoFragment;

public class AddPlayerInfoActivity extends BaseSinglePaneActivity {

	@Override
	protected Fragment onCreatePane() {
		setTitle(R.string.title_add_player);
		return new AddPlayerInfoFragment();
	}

	@Override
	protected void init() {

	}
	

}
