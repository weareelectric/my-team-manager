package com.myteammanager.ui.phone;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.myteammanager.R;
import com.ubikod.capptain.android.sdk.CapptainAgent;
import com.ubikod.capptain.android.sdk.CapptainAgentUtils;

public class PreferenceActvity extends SherlockPreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		 String activityNameOnCapptain = CapptainAgentUtils.buildCapptainActivityName(getClass()); // Uses short class name and removes "Activity" at the end.
		 CapptainAgent.getInstance(this).startActivity(this, activityNameOnCapptain, null);
	}

	@Override
	protected void onPause() {
	    super.onPause();
	    CapptainAgent.getInstance(this).endActivity();
	}

}
