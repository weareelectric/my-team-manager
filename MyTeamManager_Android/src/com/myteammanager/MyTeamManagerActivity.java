package com.myteammanager;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;

import com.myteammanager.beans.ContactBean;
import com.myteammanager.specializedStorage.MyTeamManagerDBManager;
import com.myteammanager.storage.DBManager;
import com.myteammanager.storage.SettingsManager;
import com.myteammanager.ui.phone.AddPlayerInfoActivity;
import com.myteammanager.ui.phone.ChoosePlayerFromContactsActivity;
import com.myteammanager.ui.phone.HomePageActivity;
import com.myteammanager.ui.phone.LoginActivity;
import com.myteammanager.ui.phone.SignupActivity;
import com.myteammanager.ui.phone.WizardEnterPlayersInfoActivity;
import com.myteammanager.ui.phone.WizardEnterTeamNameActivity;
import com.myteammanager.util.KeyConstants;
import com.myteammanager.util.Log;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.squareup.otto.Bus;

public class MyTeamManagerActivity extends BaseActivity {

	public static final int RESULT_LOGIN_DONE = 3821;
	public static final int RESULT_WIZARD_TEAM_NAME_ENTERED = 3822;
	public static final int RESULT_ENTER_PLAYERS_INFO_START = 3823;
	public static final int RESULT_ENTER_PLAYERS_LIST_DONE = 3824;

	private static Bus m_bus;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyTeamManagerDBManager.getInstance().init(this, MyTeamManagerDBManager.BEANS, MyTeamManagerDBManager.MYTEAMMANAGER_DB, MyTeamManagerDBManager.DB_VERSION);

		setContentView(R.layout.main);
		
		Parse.initialize(this, "MXUjHyEvzPLiBGg7GZEIXWOH9eHSqaPFcpU6WVVP", "4zJtdWSkSnpDMuyLIRUJ0jIaLnwLKFRMtdrMKq7g");
		ParseFacebookUtils.initialize(KeyConstants.FACEBOOK_APP_ID);
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		if ( currentUser == null ) {
				Intent intent = new Intent(MyTeamManagerActivity.this, LoginActivity.class);
				startActivityForResult(intent, KeyConstants.CODE_LOGIN_ACTIVITY);
		}
		else {
			if (SettingsManager.getInstance(this).getTeamName() == null) {
				Intent intent = new Intent(MyTeamManagerActivity.this, WizardEnterTeamNameActivity.class);
				startActivityForResult(intent, KeyConstants.WIZARD_TEAM_NAME_CODE);
			} else {
				startHomePage();
			}
		}



		

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(MyTeamManagerActivity.class.getName(), "requestCode: " + requestCode + " responseCode: " + resultCode);
		switch (requestCode) {
		
		case KeyConstants.CODE_LOGIN_ACTIVITY:
			if ( resultCode == RESULT_LOGIN_DONE ) {
				if (SettingsManager.getInstance(this).getTeamName() == null) {
					Intent intent = new Intent(MyTeamManagerActivity.this, WizardEnterTeamNameActivity.class);
					startActivityForResult(intent, KeyConstants.WIZARD_TEAM_NAME_CODE);
				}
				else {
					startHomePage();
				}
			}
			else {
				DBManager.getInstance().closeDB();
				finish();
			}
			break;
		
		
		
		case KeyConstants.WIZARD_TEAM_NAME_CODE:
			if (resultCode == RESULT_WIZARD_TEAM_NAME_ENTERED) {
				Intent intent = new Intent(MyTeamManagerActivity.this, WizardEnterPlayersInfoActivity.class);
				startActivityForResult(intent, KeyConstants.WIZARD_ENTER_PLAYERS_INFO_CODE);
			} else {
				DBManager.getInstance().closeDB();
				finish();
			}
			break;

		case KeyConstants.WIZARD_ENTER_PLAYERS_INFO_CODE:
			if (resultCode == RESULT_ENTER_PLAYERS_INFO_START) {
				Intent intent = new Intent(MyTeamManagerActivity.this, AddPlayerInfoActivity.class);
				startActivityForResult(intent, KeyConstants.WIZARD_ENTER_PLAYERS_PROCESS_COMPLETED_CODE);
			}
			else if ( resultCode == KeyConstants.RESULT_START_CONTACTS_SELECTION ) {
				Intent intent = new Intent(this, ChoosePlayerFromContactsActivity.class);
				startActivityForResult(intent, KeyConstants.CODE_CONTACTS_CHOSEN);
			}
			else {
				DBManager.getInstance().closeDB();
				finish();
			}
			break;
			

			
		case KeyConstants.CODE_CONTACTS_CHOSEN:
			switch (resultCode) {
			case KeyConstants.RESULT_CONTACTS_CHOSEN:
				Intent intent = new Intent(MyTeamManagerActivity.this, AddPlayerInfoActivity.class);
				if ( data != null ) {
					Bundle bundle = data.getExtras();
					if ( bundle != null ) {
						ArrayList<ContactBean> contacts = (ArrayList<ContactBean>) data.getExtras().get(KeyConstants.KEY_CHOSEN_CONTACTS);
						intent.putExtra(KeyConstants.KEY_CHOSEN_CONTACTS, contacts);
					}
				}
				startActivityForResult(intent, KeyConstants.WIZARD_ENTER_PLAYERS_PROCESS_COMPLETED_CODE);
				break;
				
				default:
					intent = new Intent(MyTeamManagerActivity.this, WizardEnterPlayersInfoActivity.class);
					startActivityForResult(intent, KeyConstants.WIZARD_ENTER_PLAYERS_INFO_CODE);
					break;

			}
			break;

		case KeyConstants.WIZARD_ENTER_PLAYERS_PROCESS_COMPLETED_CODE:
			startHomePage();
			break;

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void startHomePage() {
		Intent intent = new Intent(MyTeamManagerActivity.this, HomePageActivity.class);
		startActivity(intent);
		finish();
	}

	public static Bus getBus() {
		if ( m_bus == null ) {
			m_bus = new Bus();
		}
		return m_bus;
	}

}