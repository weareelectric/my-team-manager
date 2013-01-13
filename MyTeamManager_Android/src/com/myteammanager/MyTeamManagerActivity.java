package com.myteammanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.ConvocationBean;
import com.myteammanager.beans.EventBean;
import com.myteammanager.beans.LineupBean;
import com.myteammanager.beans.MatchBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.beans.ScorerBean;
import com.myteammanager.beans.SubstitutionBean;
import com.myteammanager.beans.TeamBean;
import com.myteammanager.specializedStorage.MyTeamManagerDBManager;
import com.myteammanager.storage.DBManager;
import com.myteammanager.storage.SettingsManager;
import com.myteammanager.ui.phone.AddPlayerInfoActivity;
import com.myteammanager.ui.phone.HomePageActivity;
import com.myteammanager.ui.phone.WizardEnterPlayersInfoActivity;
import com.myteammanager.ui.phone.WizardEnterTeamNameActivity;
import com.squareup.otto.Bus;

public class MyTeamManagerActivity extends BaseActivity {

	public static final int WIZARD_TEAM_NAME_CODE = 1;
	public static final int WIZARD_ENTER_PLAYERS_INFO_CODE = 2;
	public static final int WIZARD_ENTER_PLAYERS_PROCESS_COMPLETED_CODE = 3;

	public static final int RESULT_WIZARD_TEAM_NAME_ENTERED = 3823;
	public static final int RESULT_ENTER_PLAYERS_INFO_START = 3824;
	public static final int RESULT_ENTER_PLAYERS_LIST_DONE = 3825;

	private static final BaseBean[] BEANS = { new PlayerBean(), new TeamBean(), new EventBean(), new MatchBean(),
			new ConvocationBean(), new LineupBean(), new ScorerBean(), new SubstitutionBean() };

	private static Bus m_bus;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyTeamManagerDBManager.getInstance().init(this, BEANS, "myteammanager.db", 2);

		setContentView(R.layout.main);

		if (SettingsManager.getInstance(this).getTeamName() == null) {
			Intent intent = new Intent(MyTeamManagerActivity.this, WizardEnterTeamNameActivity.class);
			startActivityForResult(intent, WIZARD_TEAM_NAME_CODE);
		} else {
			startHomePage();
		}

		m_bus = new Bus();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(MyTeamManagerActivity.class.getName(), "requestCode: " + requestCode + " responseCode: " + resultCode);
		switch (requestCode) {
		case WIZARD_TEAM_NAME_CODE:
			if (resultCode == RESULT_WIZARD_TEAM_NAME_ENTERED) {
				Intent intent = new Intent(MyTeamManagerActivity.this, WizardEnterPlayersInfoActivity.class);
				startActivityForResult(intent, WIZARD_ENTER_PLAYERS_INFO_CODE);
			} else {
				DBManager.getInstance().closeDB();
				finish();
			}
			break;

		case WIZARD_ENTER_PLAYERS_INFO_CODE:
			if (resultCode == RESULT_ENTER_PLAYERS_INFO_START) {
				Intent intent = new Intent(MyTeamManagerActivity.this, AddPlayerInfoActivity.class);
				startActivityForResult(intent, WIZARD_ENTER_PLAYERS_PROCESS_COMPLETED_CODE);
			} else {
				DBManager.getInstance().closeDB();
				finish();
			}
			break;

		case WIZARD_ENTER_PLAYERS_PROCESS_COMPLETED_CODE:
			if (resultCode == RESULT_ENTER_PLAYERS_LIST_DONE) {
				startHomePage();
			} else {
				DBManager.getInstance().closeDB();
				finish();
			}
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
		return m_bus;
	}

}