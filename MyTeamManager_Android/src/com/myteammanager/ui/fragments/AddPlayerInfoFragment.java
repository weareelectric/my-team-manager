package com.myteammanager.ui.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.actionbarsherlock.view.MenuItem;
import com.myteammanager.MyTeamManagerActivity;
import com.myteammanager.R;
import com.myteammanager.beans.ContactBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.storage.DBManager;
import com.myteammanager.util.CheckboxWithViewGroupHelper;
import com.myteammanager.util.KeyConstants;
import com.myteammanager.util.PlayerAndroidUtil;
import com.myteammanager.util.StringUtil;

public class AddPlayerInfoFragment extends BaseTwoButtonActionsFormFragment implements TextWatcher {

	private static final String LOG_TAG = AddPlayerInfoFragment.class.getName();

	protected EditText m_playerLastName;
	protected EditText m_playerName;
	protected Spinner m_positionSpinner;
	protected PlayerBean m_player;
	protected CheckBox m_birthDateCheckBox;
	protected EditText m_emailEditText;
	protected EditText m_phoneEditText;
	protected Spinner m_shirtNumberSpinner;
	protected DatePicker m_birthDateDatePicker;
	protected CheckboxWithViewGroupHelper m_birthDateCompositeField;
	
	private int m_indexOfLastAddedPlayer = 0;
	private ArrayList<ContactBean> m_contacts;
	private boolean m_addFromContacts = false;



	public AddPlayerInfoFragment() {
		super(R.layout.fragment_edit_player);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		
		Intent intent = getSherlockActivity().getIntent();
		
		if ( intent != null ) {
			Bundle bundle = intent.getExtras();
			if ( bundle != null ) {
				m_contacts = (ArrayList<ContactBean>) bundle.get(KeyConstants.KEY_CHOSEN_CONTACTS);
				m_addFromContacts = true;
				Log.d(LOG_TAG, "Found contacts to add from address book");
			}
		}

		m_playerName = (EditText) m_root.findViewById(R.id.editTextPlayerName);
		m_playerLastName = (EditText) m_root.findViewById(R.id.editTextPlayerLastName);
		m_playerLastName.addTextChangedListener(this);
		m_positionSpinner = (Spinner) m_root.findViewById(R.id.spinnerPosition);
		m_emailEditText = (EditText) m_root.findViewById(R.id.editTextMainEmail);
		m_phoneEditText = (EditText) m_root.findViewById(R.id.editTextMainPhone);
		m_shirtNumberSpinner = (Spinner) m_root.findViewById(R.id.spinnerShirtNumbers);
		ArrayAdapter<String> shirtNumbersAdapter = new ArrayAdapter<String>(getSherlockActivity(),
				android.R.layout.simple_spinner_item, PlayerAndroidUtil.getTShirtNumbers(getSherlockActivity()));
		m_shirtNumberSpinner.setAdapter(shirtNumbersAdapter);

		m_birthDateCheckBox = (CheckBox) m_root.findViewById(R.id.checkboxBirthDate);
		m_birthDateDatePicker = (DatePicker) m_root.findViewById(R.id.datePickerBirthDate);

		m_birthDateCompositeField = new CheckboxWithViewGroupHelper(m_birthDateCheckBox, m_birthDateDatePicker);
		
		if ( m_addFromContacts ) {
			populateFormWithDataFromContact(0);
		}
		
		return m_root;
	}
	
	private void populateFormWithDataFromContact(int i) {
		ContactBean contact = m_contacts.get(i);
		if ( m_addFromContacts ) {
			m_playerName.setText(contact.getFirstName());
			m_playerLastName.setText(contact.getLastName());
			if ( contact.getEmails() != null ) {
				if ( contact.getEmails().size() == 1 ) {
					m_emailEditText.setText(contact.getEmails().get(0));
				}
			}
			
			if ( contact.getPhones() != null ) {
				if ( contact.getPhones().size() == 1 ) {
					m_phoneEditText.setText(contact.getPhones().get(0));
				}
			}

		}
	}

	@Override
	public void onTextChanged(CharSequence string, int arg1, int arg2, int arg3) {
		Log.d(LOG_TAG, "String changed: " + string);
		if (StringUtil.isNotEmpty(string)) {
			if (!m_menuItem1.isEnabled()) {
				m_menuItem1.setEnabled(true);
			}
		} else {
			if (m_menuItem1.isEnabled()) {
				m_menuItem1.setEnabled(false);
			}
		}

	}

	@Override
	public void beforeTextChanged(CharSequence string, int arg1, int arg2, int arg3) {

	}

	@Override
	public void afterTextChanged(Editable arg0) {

	}

	protected void performActionsAndExit() {
		Log.d(LOG_TAG, "Entered players done");
		if (m_menuItem1.isEnabled()) {
			// If the button save is enabled user entered some valid info. Save player 
			savePlayer();
		}

		getActivity().setResult(MyTeamManagerActivity.RESULT_ENTER_PLAYERS_LIST_DONE);
		getActivity().finish();
	}

	protected void savePlayer() {
		if (m_player == null) {
			m_player = new PlayerBean();

		}

		populatePlayerObject();

		storePlayerInfo();
	}

	protected void resetObjectAndInterface() {
		// Reset the object to be used for next player data
		m_player.reset();

		// Reset the fields to be used for the next player data
		resetInterface();
	}

	private void resetInterface() {
		m_playerName.setText("");
		m_playerLastName.setText("");
		m_playerLastName.requestFocus();
		m_positionSpinner.setSelection(PlayerBean.ROLE_GK);
		m_menuItem1.setEnabled(false);

		m_shirtNumberSpinner.setSelection(0);
		m_emailEditText.setText(null);
		m_phoneEditText.setText(null);

		m_birthDateCompositeField.setDateValue(null);
	}

	protected void populatePlayerObject() {
		m_player.setName(m_playerName.getText().toString());
		m_player.setLastName(m_playerLastName.getText().toString());
		m_player.setRole(m_positionSpinner.getSelectedItemPosition());
		m_player.setShirtNumber(m_shirtNumberSpinner.getSelectedItemPosition());
		m_player.setEmail(m_emailEditText.getText().toString());
		m_player.setPhone(m_phoneEditText.getText().toString());
		m_player.setBirthDate(m_birthDateCompositeField.getDateValueFromView());
	}

	protected void storePlayerInfo() {
		DBManager.getInstance().storeBean(m_player);
		Log.d(LOG_TAG, "Stored player: " + PlayerAndroidUtil.toString(getActivity(), m_player));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//		switch (item.getItemId()) {
		//
		//		case R.id.menu_add_additionalInfo:
		//			Intent intent = new Intent(getSherlockActivity(),
		//					AddPlayerDetailsActivity.class);
		//			if ( m_player == null ) {
		//				m_player = new PlayerBean();
		//				
		//			}
		//			
		//			intent.putExtra(KEY_PLAYER, m_player);
		//			startActivityForResult(intent, CODE_PLAYER_DETAILS_EDITED);
		//			break;
		//		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void clickOnMenuItem1() {
		savePlayer();

		resetObjectAndInterface();
		
		if ( m_addFromContacts ) {
			m_indexOfLastAddedPlayer++;
			if ( m_indexOfLastAddedPlayer < m_contacts.size() ) {
				populateFormWithDataFromContact(m_indexOfLastAddedPlayer);
			}
			else {
				m_addFromContacts = false;
			}
		}
	}

	@Override
	protected void clickOnMenuItem2() {
		performActionsAndExit();
	}

	@Override
	protected void customizeMenuItem1(View root) {
	}

	@Override
	protected void customizeMenuItem2(View root) {
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(LOG_TAG, "Received result <" + requestCode + "> <" + resultCode + ">");

		if (requestCode == KeyConstants.CODE_PLAYER_DETAILS_EDITED) {
			if (resultCode == KeyConstants.RESULT_PLAYER_DETAILS_EDITED) {
				m_player = (PlayerBean) data.getExtras().get(KeyConstants.KEY_PLAYER);
			}
		}
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
}