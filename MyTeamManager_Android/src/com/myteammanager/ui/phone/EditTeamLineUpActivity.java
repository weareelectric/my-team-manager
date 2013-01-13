package com.myteammanager.ui.phone;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.myteammanager.R;
import com.myteammanager.beans.MatchBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.specializedStorage.MyTeamManagerDBManager;
import com.myteammanager.ui.fragments.SoccerFieldFragment;
import com.myteammanager.ui.fragments.SubstitutesFragment;
import com.myteammanager.util.KeyConstants;
import com.myteammanager.util.PlayerUtil;

public class EditTeamLineUpActivity extends SherlockFragmentActivity {

	private final static String LOG_TAG = EditTeamLineUpActivity.class.getName();

	private SoccerFieldFragment m_soccerFieldFragment;
	private SubstitutesFragment m_substitutesFragment;

	private MatchBean m_match;

	/*		@Override
	protected Fragment onCreatePane() {
		Log.d(LOG_TAG, "maremma maiala");
		return new SoccerFieldFragment();
	}*/

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_edit_team_lineup);

		Bundle extra = getIntent().getExtras();
		m_match = (MatchBean) extra.get(KeyConstants.KEY_MATCH);

		setTitle(m_match.getTeam1StringToShow(this) + " - " + m_match.getTeam2StringToShow(this));

		final FragmentManager fm = getSupportFragmentManager();
		m_soccerFieldFragment = new SoccerFieldFragment();
		m_soccerFieldFragment.setMatch(m_match);
		m_soccerFieldFragment.setMainActivity(this);
		m_substitutesFragment = new SubstitutesFragment();
		m_substitutesFragment.setMainActivity(this);

		fm.beginTransaction().add(R.id.root_container1, m_soccerFieldFragment).commit();
		fm.beginTransaction().add(R.id.root_container2, m_substitutesFragment).commit();

		/* Log.d(LOG_TAG, "Aggiungo la lista");
		 fm.beginTransaction().add(R.id.root_container, m_listPlayerFragment).commit();*/
	}

	public void updateNotSelectedTextView(ArrayList<PlayerBean> notSelectedPlayers) {
		m_substitutesFragment.updateNotSelectedTextView(notSelectedPlayers);
	}

	public void updateSubstituteTextView(ArrayList<PlayerBean> players) {
		m_substitutesFragment.updateSubstitutesTextView(players);
	}

	public void startActivityToSelectSubstitutes() {
		m_soccerFieldFragment.startSubstitutesSelection();
	}
	
	@Override
	protected void onDestroy() {
		super.onStop();
		MyTeamManagerDBManager.getInstance().updatePlayersTableWithPresencesAndGoals();
	}

	/*	@Override
		protected void init() {
			// TODO Auto-generated method stub
			
		}*/
	


}
