package com.myteammanager.ui.phone;

import com.myteammanager.R;
import com.myteammanager.R.layout;
import com.myteammanager.R.menu;
import com.myteammanager.storage.SettingsManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class ContactUsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("plain/text");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"myteammanagerservice@gmail.com"});
		intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.title_subject_contactus_email));
		startActivity(Intent.createChooser(intent, ""));
		finish();
	}

}
