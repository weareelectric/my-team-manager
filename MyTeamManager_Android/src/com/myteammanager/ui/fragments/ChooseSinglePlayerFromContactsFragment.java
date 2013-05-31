package com.myteammanager.ui.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.content.res.Resources;
import com.myteammanager.util.Log;
import android.view.View;
import org.holoeverywhere.widget.AdapterView;
import org.holoeverywhere.widget.ListView;

import android.widget.ArrayAdapter;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.myteammanager.R;
import com.myteammanager.adapter.ContactListAdapterWithCheckbox;
import com.myteammanager.adapter.SimpleContactAdapter;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.ContactBean;
import com.myteammanager.beans.ConvocationBean;
import com.myteammanager.beans.MatchBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.contacts.PhonebookManager;
import com.myteammanager.exceptions.NoDataException;
import com.myteammanager.storage.SettingsManager;
import com.myteammanager.ui.CheckboxListener;
import com.myteammanager.ui.phone.MatchDetailActivity;
import com.myteammanager.util.KeyConstants;

public class ChooseSinglePlayerFromContactsFragment extends BaseListFragment implements CheckboxListener {
	
	private ArrayList<ContactBean> m_chosenContacts;

	@Override
	protected void init() {
		m_showSearchEditText = true;
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
		return new SimpleContactAdapter(getActivity(), R.layout.list, m_itemsList);
	}
	


	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		ContactBean contact = (ContactBean) m_itemsList.get(position);
		Intent intent = new Intent();
		intent.putExtra(KeyConstants.KEY_CHOSEN_CONTACT, contact);
		getActivity().setResult(KeyConstants.RESULT_CONTACT_CHOSEN, intent);
		getActivity().finish();
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
	
	@Override
	protected void resetAdapterData() {
		((SimpleContactAdapter)m_adapter).resetData();
	}
	
}
