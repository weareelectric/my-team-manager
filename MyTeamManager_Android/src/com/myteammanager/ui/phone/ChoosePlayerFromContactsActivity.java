package com.myteammanager.ui.phone;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.myteammanager.R;
import com.myteammanager.beans.ConvocationBean;
import com.myteammanager.ui.fragments.ChoosePlayerFromContactsFragment;
import com.myteammanager.ui.fragments.EditConvocationFragment;
import com.myteammanager.ui.fragments.RosterFragment;

public class ChoosePlayerFromContactsActivity extends BaseSinglePaneActivity {

	private final static String LOG_TAG = ChoosePlayerFromContactsActivity.class.getName();

	private ChoosePlayerFromContactsFragment m_choosePlayersFromContact;

	@Override
	protected Fragment onCreatePane() {
		m_choosePlayersFromContact = new ChoosePlayerFromContactsFragment();
		return m_choosePlayersFromContact;
	}

	@Override
	protected void init() {

	}
	
	

}
