package com.myteammanager.ui.phone;

import android.content.Context;
import org.holoeverywhere.app.Fragment;

import com.myteammanager.R;
import com.myteammanager.beans.ConvocationBean;
import com.myteammanager.ui.fragments.ChoosePlayerFromContactsFragment;
import com.myteammanager.ui.fragments.ChooseSinglePlayerFromContactsFragment;
import com.myteammanager.ui.fragments.EditConvocationFragment;
import com.myteammanager.ui.fragments.RosterFragment;

public class ChooseSinglePlayerFromContactsActivity extends BaseSinglePaneActivity {

	private final static String LOG_TAG = ChooseSinglePlayerFromContactsActivity.class.getName();

	private ChooseSinglePlayerFromContactsFragment m_choosePlayersFromContact;

	@Override
	protected Fragment onCreatePane() {
		setTitle(R.string.title_import_from_contacts);
		m_choosePlayersFromContact = new ChooseSinglePlayerFromContactsFragment();
		return m_choosePlayersFromContact;
	}

	@Override
	protected void init() {

	}
	
	

}
