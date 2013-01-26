package com.myteammanager.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.myteammanager.R;
import com.myteammanager.beans.ContactBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.ui.phone.ChoosePlayerFromContactsActivity;
import com.myteammanager.ui.phone.ChooseSinglePlayerFromContactsActivity;
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
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.add_player_menu, menu);
		m_menuItem1 = menu.findItem(R.id.menu_item1);
		m_menuItem2 = menu.findItem(R.id.menu_item2);
	}

	@Override
	protected void customizeMenuItem2(View root) {

	}

	@Override
	protected void customizeMenuItem1(View root) {
		super.customizeMenuItem1(root);
		m_menuItem1.setTitle(getSherlockActivity().getResources().getString(
				R.string.save));
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

	@Override
	protected void clickOnMenuItem2() {
		// User wants to add data taking them from a contact in the address book
		Intent intent = new Intent(getSherlockActivity(),
				ChooseSinglePlayerFromContactsActivity.class);
		startActivityForResult(intent, KeyConstants.CODE_CONTACT_CHOSEN);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case KeyConstants.CODE_CONTACT_CHOSEN:
			switch (resultCode) {
			case KeyConstants.RESULT_CONTACT_CHOSEN:
				ContactBean contact = (ContactBean)data.getExtras().get(KeyConstants.KEY_CHOSEN_CONTACT);
				writeContactDataInTheForm(contact);
				break;
			}
			break;
		}
	}

}
