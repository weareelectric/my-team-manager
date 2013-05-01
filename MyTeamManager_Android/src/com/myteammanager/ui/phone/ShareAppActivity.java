package com.myteammanager.ui.phone;

import com.myteammanager.R;
import com.myteammanager.R.layout;
import com.myteammanager.R.menu;
import com.myteammanager.storage.SettingsManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class ShareAppActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("plain/text");
		
		intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.label_share_app_subject));
		intent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.message_share_the_application));
		startActivity(Intent.createChooser(intent, ""));
		finish();
	}

}
