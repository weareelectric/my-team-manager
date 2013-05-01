package com.myteammanager.ui.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.content.res.Resources;
import com.myteammanager.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.myteammanager.R;
import com.myteammanager.adapter.ContactListAdapterWithCheckbox;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.ContactBean;
import com.myteammanager.beans.ConvocationBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.contacts.PhonebookManager;
import com.myteammanager.exceptions.NoDataException;
import com.myteammanager.storage.SettingsManager;
import com.myteammanager.ui.CheckboxListener;
import com.myteammanager.util.KeyConstants;

public class ChoosePlayerFromContactsFragment extends BaseListFragment implements CheckboxListener {
	
	private ArrayList<ContactBean> m_chosenContacts;

	@Override
	protected void init() {
	}

	@Override
	protected ArrayList<? extends BaseBean> getData() {
		try {
			return PhonebookManager.getContacts(getSupportActivity());
		} catch (NoDataException e) {
			Log.e(LOG_TAG, "Error reading from phone address book");
			return new ArrayList<ContactBean>();
		}
	}

	@Override
	protected ArrayAdapter<? extends BaseBean> initAdapter() {
		m_itemsList.clear();
		return new ContactListAdapterWithCheckbox(getActivity(), R.layout.list_with_checkbox, m_itemsList, this);
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.edit_from_contacts, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_save_player:
			m_chosenContacts = new ArrayList<ContactBean>();
			
			ContactBean contact = null;
			Object obj = null;
			int size = m_itemsList.size();
			
			for (int i = 0; i < size; i++) {
				obj = m_itemsList.get(i);
				if (obj instanceof ContactBean) {
					contact = (ContactBean) obj;

					if (contact.isChosen()) {
						Log.d(LOG_TAG, "Chosen contact: " + contact);
						m_chosenContacts.add(contact);
					}
				}

			}
			Intent intent = new Intent();
			intent.putExtra(KeyConstants.KEY_CHOSEN_CONTACTS, m_chosenContacts);
			getActivity().setResult(KeyConstants.RESULT_CONTACTS_CHOSEN, intent);
			getActivity().finish();
			break;
		}
		return true;
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
	public void checkboxChanged(boolean isSelectAll) {
		
	}


	
}
