package com.myteammanager.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.myteammanager.R;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.storage.DBManager;
import com.myteammanager.util.KeyConstants;
import com.myteammanager.util.PlayerAndroidUtil;

public class EditPlayerInfoFragment extends AddPlayerInfoFromRosterFragment {

	private static final String LOG_TAG = EditPlayerInfoFragment.class.getName();

	public EditPlayerInfoFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Bundle extra = getSherlockActivity().getIntent().getExtras();
		m_player = (PlayerBean) extra.get(KeyConstants.KEY_BEANDATA);
		m_playerName.setText(m_player.getName());
		m_playerLastName.setText(m_player.getLastName());

		m_shirtNumberSpinner.setSelection(m_player.getShirtNumber());
		m_emailEditText.setText(m_player.getEmail());
		m_phoneEditText.setText(m_player.getPhone());
		m_birthDateCompositeField.setDateValue(m_player.getBirthDate());

		m_positionSpinner.setSelection(m_player.getRole());

		Log.d(LOG_TAG, "m_playerLastName: " + m_playerLastName);

		setHasOptionsMenu(true);
		return m_root;
	}

	@Override
	protected void storePlayerInfo() {
		DBManager.getInstance().updateBean(m_player);
		Log.d(LOG_TAG, "Updated player: " + PlayerAndroidUtil.toString(getActivity(), m_player));
		setResultForActivity();
	}

	protected void setResultForActivity() {
		Intent intent = new Intent();
		intent.putExtra(KeyConstants.KEY_BEANDATA, m_player);
		getActivity().setResult(KeyConstants.RESULT_BEAN_EDITED, intent);
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.player_detail, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_delete_player:
			showDeleteConfirmation();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void customizeMenuItem2(View root) {
		m_menuItem2.setVisible(false);
	}

	@Override
	public void button1Pressed(int alertId) {
		DBManager.getInstance().deleteBean(m_player);
		Intent intent = new Intent();
		intent.putExtra(KeyConstants.KEY_BEANDATA, m_player);
		getActivity().setResult(KeyConstants.RESULT_BEAN_DELETED, intent);

		getActivity().finish();
	}

}
