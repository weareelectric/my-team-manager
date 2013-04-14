package com.myteammanager.ui.phone;

import org.holoeverywhere.app.Fragment;

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
