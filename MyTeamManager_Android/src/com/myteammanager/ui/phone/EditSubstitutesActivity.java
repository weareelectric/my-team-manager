package com.myteammanager.ui.phone;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.myteammanager.R;
import com.myteammanager.beans.ConvocationBean;
import com.myteammanager.ui.fragments.EditConvocationFragment;
import com.myteammanager.ui.fragments.EditSubstitutesFragment;
import com.myteammanager.ui.fragments.RosterFragment;

public class EditSubstitutesActivity extends EditConvocationActivity {

	private final static String LOG_TAG = EditSubstitutesActivity.class.getName();

	private EditSubstitutesFragment m_substitutesFragment;

	@Override
	protected Fragment onCreatePane() {
		setTitle(getString(R.string.title_substitutes));
		m_substitutesFragment = new EditSubstitutesFragment();
		return m_substitutesFragment;
	}

	@Override
	protected void init() {

	}
	

}
