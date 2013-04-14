package com.myteammanager.ui.phone;

import android.os.Bundle;
import org.holoeverywhere.app.Fragment;

import com.myteammanager.ui.fragments.WizardEnterTeamNameFragment;

public class WizardEnterTeamNameActivity extends BaseSinglePaneActivity {

	@Override
	protected Fragment onCreatePane() {
		return new WizardEnterTeamNameFragment();
	}
	
	@Override
	protected void init() {
	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	
}
