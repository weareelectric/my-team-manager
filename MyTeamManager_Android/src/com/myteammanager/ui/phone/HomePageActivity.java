package com.myteammanager.ui.phone;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONException;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.Fragment;
import com.myteammanager.util.Log;
import org.holoeverywhere.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.myteammanager.R;
import com.myteammanager.beans.PageBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.data.FacebookManager;
import com.myteammanager.data.FacebookParser;
import com.myteammanager.events.FacebookPageResponseEvent;
import com.myteammanager.events.FacebookResponseEvent;
import com.myteammanager.listener.DialogListListener;
import com.myteammanager.listener.FacebookResponseListener;
import com.myteammanager.storage.DBManager;
import com.myteammanager.storage.SettingsManager;
import com.myteammanager.ui.ButtonsAlertDialogListener;
import com.myteammanager.ui.ListDialogFragment;
import com.myteammanager.ui.fragments.ButtonsAlertDialogFragment;
import com.myteammanager.ui.fragments.NewHomeFragment;
import com.myteammanager.util.KeyConstants;
import com.myteammanager.util.StringUtil;

public class HomePageActivity extends BaseSinglePaneActivity implements FacebookResponseListener,
		ButtonsAlertDialogListener, DialogInterface.OnClickListener {
	
	public static final String SHARED_PREFERENCES_NAME = "myTeamManagerPref";

	private static Facebook m_facebook = new Facebook(KeyConstants.FACEBOOK_APP_ID);
	private SharedPreferences m_prefs;
	private ListDialogFragment m_listDialogFragment;
	private ArrayList<PageBean> m_fbPageList;
	private NewHomeFragment m_homePageFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!SettingsManager.getInstance(this).hasFacebookActivationBeenRequested()) {
			SettingsManager.getInstance(this).setFacebookActivationRequested(true);
			showFacebookDialogActivation();
			
		} else if ( SettingsManager.getInstance(this).isFacebookActivated() ){
			facebookAuth();
		}

	}

	private void showFacebookDialogActivation() {
		ButtonsAlertDialogFragment newFragment = new ButtonsAlertDialogFragment(null,
				getString(R.string.dialog_facebook_activation), this, R.string.label_yes, R.string.label_no);
		newFragment.show(this.getSupportFragmentManager(), "");
	}

	public void facebookAuth() {
		m_prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
		String access_token = m_prefs.getString("access_token", null);
		long expires = m_prefs.getLong("access_expires", 0);
		if (access_token != null) {
			m_facebook.setAccessToken(access_token);
		}
		if (expires != 0) {
			m_facebook.setAccessExpires(expires);
		}

		/*
		 * Only call authorize if the access_token has expired.
		 */
		if (!m_facebook.isSessionValid()) {

			m_facebook.authorize(this, new String[] { "publish_stream, manage_pages" }, new DialogListener() {
				@Override
				public void onComplete(Bundle values) {
					SharedPreferences.Editor editor = m_prefs.edit();
					editor.putString("access_token", m_facebook.getAccessToken());
					editor.putLong("access_expires", m_facebook.getAccessExpires());
					editor.commit();

					// Now get the list of the pages to allow user to choose which page he wants to use to publish info
					showProgressDialog(getResources().getString(R.string.dialog_please_wait_loading_data));
					FacebookManager.getInstance().getPages(new FacebookPageResponseEvent(HomePageActivity.this));
				}

				@Override
				public void onFacebookError(FacebookError error) {
				}

				@Override
				public void onError(DialogError e) {
				}

				@Override
				public void onCancel() {
				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("HomePageActivity", "onActivityResult");
		Log.d("HomePageActivity", "onActivityResult.requestCode: " + requestCode);
		Log.d("HomePageActivity", "onActivityResult.resultCode: " + resultCode);
		if (requestCode == KeyConstants.CODE_SETTINGS_ACTIVIY) {
			// If facebook is active and there is no tokenID start the Oauth process
			m_prefs = getPreferences(MODE_PRIVATE);
			Log.d("HomePageActivity", "SettingsManager.getInstance(this).isFacebookActivation(): "
					+ SettingsManager.getInstance(this).isFacebookActivated());
			Log.d(
					"HomePageActivity",
					"StringUtil.isNotEmpty(m_prefs.getString(\"access_token\", null): "
							+ StringUtil.isNotEmpty(m_prefs.getString("access_token", null)));
			if (SettingsManager.getInstance(this).isFacebookActivated()
					&& !StringUtil.isNotEmpty(m_prefs.getString("access_token", null))) {
				facebookAuth();
			}
		}
		else {
			m_facebook.authorizeCallback(requestCode, resultCode, data);
		}

	}

	public void onResume() {
		super.onResume();
		m_facebook.extendAccessTokenIfNeeded(this, null);
	}

	@Override
	protected Fragment onCreatePane() {
		if ( m_homePageFragment == null ) {
			m_homePageFragment = new NewHomeFragment();
		}
		return m_homePageFragment;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		DBManager.getInstance().closeDB();
		super.onDestroy();
	}

	public static Facebook getFacebook() {
		return m_facebook;
	}

	@Override
	public void button1Pressed(int alertId) {

		SettingsManager.getInstance(this).setFacebookActivation(true);
		facebookAuth();

	}

	@Override
	public void button2Pressed(int alertId) {

	}

	@Override
	public void button3Pressed(int alertId) {
		SettingsManager.getInstance(this).setFacebookActivation(false);

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.home_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_settings:
			Intent intent = new Intent(this, PreferenceActvity.class);
			startActivityForResult(intent, KeyConstants.CODE_SETTINGS_ACTIVIY);
			break;
			
		case R.id.menu_help:
			intent = new Intent(this,
					HelpActivity.class);
			startActivity(intent);
			break;
		}
		return true;
	}

	@Override
	public void postFBResponse(FacebookResponseEvent event) {
		if (event.getFacebookError() != null) {

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(HomePageActivity.this, R.string.toast_facebook_pages_error, Toast.LENGTH_LONG).show();
				}
			});
		} else {
			FacebookPageResponseEvent pagesResponse = (FacebookPageResponseEvent) event;
			m_fbPageList = pagesResponse.getPages();
			cancelProgressDialog();
/*
			m_listDialogFragment = new ListDialogFragment(this, getResources().getString(
					R.string.dialog_facebook_choose_your_page_title), "", PageBean.getPages(m_fbPageList));
			m_listDialogFragment.setListener(this);
			m_listDialogFragment.show(getSupportFragmentManager(), "");
			*/
	
			runOnUiThread(new Runnable() {

				private AlertDialog m_alert;

				@Override
				public void run() {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							HomePageActivity.this);
					builder.setItems(PageBean.getPages(m_fbPageList), HomePageActivity.this);
					m_alert = builder.create();
					m_alert.setTitle(getResources().getString(R.string.title_dialog_facebook_pages));
					
					m_alert.show();
				}
			});
			

		}

	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		Log.d("HomePageActivity", "Selected index in the dialog: " + which);
		dialog.dismiss();

		PageBean fbPageChosen = m_fbPageList.get(which);
		Log.d("HomePageActivity", "IdPage: " + fbPageChosen.getIdPage());
		Log.d("HomePageActivity", "Page name: " + fbPageChosen.getName());
		SettingsManager.getInstance(this).setFacebookPageId(fbPageChosen.getIdPage());
		SettingsManager.getInstance(this).setFacebookPageName(fbPageChosen.getName());
		
	}
	
}
