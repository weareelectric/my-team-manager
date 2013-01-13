package com.myteammanager.ui.phone;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.myteammanager.R;

public class PreferenceActvity extends SherlockPreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

}
