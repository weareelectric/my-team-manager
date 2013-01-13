package com.myteammanager.ui.phone;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.myteammanager.ui.fragments.WizardEnterPlayersInfoFragment;

public class WizardEnterPlayersInfoActivity extends BaseSinglePaneActivity {

	@Override
	protected Fragment onCreatePane() {
		return new WizardEnterPlayersInfoFragment();
	}

	@Override
	protected void init() {
		// setTitle(SettingsManager.getInstance(this).getTeamName());
	}

}
