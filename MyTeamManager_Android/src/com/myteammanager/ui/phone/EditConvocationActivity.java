package com.myteammanager.ui.phone;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.myteammanager.R;
import com.myteammanager.beans.ConvocationBean;
import com.myteammanager.ui.fragments.EditConvocationFragment;
import com.myteammanager.ui.fragments.RosterFragment;

public class EditConvocationActivity extends BaseSinglePaneActivity {

	private final static String LOG_TAG = EditConvocationActivity.class.getName();

	private EditConvocationFragment m_convocationFragment;

	@Override
	protected Fragment onCreatePane() {
		m_convocationFragment = new EditConvocationFragment();
		return m_convocationFragment;
	}

	@Override
	protected void init() {

	}

}
