package com.myteammanager.ui.fragments;

import java.util.ArrayList;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.myteammanager.R;
import com.myteammanager.adapter.ContactListAdapterWithCheckbox;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.ContactBean;
import com.myteammanager.contacts.PhonebookManager;
import com.myteammanager.exceptions.NoDataException;
import com.myteammanager.ui.CheckboxListener;

public class ChoosePlayerFromContactsFragment extends BaseListFragment implements CheckboxListener {

	@Override
	protected void init() {
	}

	@Override
	protected ArrayList<? extends BaseBean> getData() {
		try {
			return PhonebookManager.getContacts(getSherlockActivity());
		} catch (NoDataException e) {
			Log.e(LOG_TAG, "Error reading from phone address book");
			return new ArrayList<ContactBean>();
		}
	}

	@Override
	protected ArrayAdapter<? extends BaseBean> initAdapter() {
		m_itemsList.clear();
		return new ContactListAdapterWithCheckbox(getSherlockActivity(), R.layout.list_with_checkbox, m_itemsList, this);
	}

	@Override
	public void button1Pressed(int alertId) {
		
	}

	@Override
	public void button2Pressed(int alertId) {
		
	}

	@Override
	public void button3Pressed(int alertId) {
		
	}

	@Override
	public void convocationChanged() {
		
	}

	
}
