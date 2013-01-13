package com.myteammanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.flurry.android.FlurryAgent;

public abstract class BaseActivity extends SherlockFragmentActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT > 13) {
			// new HomeButtonEnabledWrapper().setHomeButtonEnabled(true);
		}
	}

	/**
	 * Converts an intent into a {@link Bundle} suitable for use as fragment arguments.
	 */
	public static Bundle intentToFragmentArguments(Intent intent) {
		Bundle arguments = new Bundle();
		if (intent == null) {
			return arguments;
		}

		final Uri data = intent.getData();
		if (data != null) {
			arguments.putParcelable("_uri", data);
		}

		final Bundle extras = intent.getExtras();
		if (extras != null) {
			arguments.putAll(intent.getExtras());
		}

		return arguments;
	}

	/**
	 * Converts a fragment arguments bundle into an intent.
	 */
	public static Intent fragmentArgumentsToIntent(Bundle arguments) {
		Intent intent = new Intent();
		if (arguments == null) {
			return intent;
		}

		final Uri data = arguments.getParcelable("_uri");
		if (data != null) {
			intent.setData(data);
		}

		intent.putExtras(arguments);
		intent.removeExtra("_uri");
		return intent;
	}

	@Override
	public void onAttachedToWindow() {
		getWindow().setFormat(PixelFormat.RGBA_8888);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d("BaseActivity",this + " onStop");
		FlurryAgent.onStartSession(this, "DQ2HS8S62WK8Q7YZ5TX6");
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		Log.d("BaseActivity",this + " onSTart");
		FlurryAgent.onStartSession(this, "DQ2HS8S62WK8Q7YZ5TX6");
		FlurryAgent.onPageView();
	}

	
	
}
