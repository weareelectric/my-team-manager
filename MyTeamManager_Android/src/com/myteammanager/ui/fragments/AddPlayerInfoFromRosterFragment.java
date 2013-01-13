package com.myteammanager.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myteammanager.R;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.util.KeyConstants;

public class AddPlayerInfoFromRosterFragment extends AddPlayerInfoFragment {
 
	private static final String LOG_TAG = AddPlayerInfoFragment.class.getName();

	public AddPlayerInfoFromRosterFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		m_player = new PlayerBean();
		
		
		return m_root;
	}

	@Override
	protected void customizeMenuItem2(View root) {
		Log.d(LOG_TAG, "Disable menuitem2");
		m_menuItem2.setVisible(false);
	}

	@Override
	protected void customizeMenuItem1(View root) {
		super.customizeMenuItem1(root);
		m_menuItem1.setTitle(getSherlockActivity().getResources().getString(R.string.save));
	}
	
	protected void savePlayer() {
		super.savePlayer();
		
		setResultForActivity();
		getActivity().finish();
	}
	

	protected void setResultForActivity() {
		Intent intent = new Intent();
		intent.putExtra(KeyConstants.KEY_BEANDATA, m_player);
		getActivity().setResult(KeyConstants.RESULT_BEAN_ADDED, intent);
		
		
	}

	protected void performActionsAndExit() {
		getActivity().finish();
	}
	
	@Override
	protected void resetObjectAndInterface() {
	}

}
