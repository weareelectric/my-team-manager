package com.myteammanager.ui.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import com.myteammanager.util.Log;
import org.holoeverywhere.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.widget.CheckBox;
import org.holoeverywhere.widget.DatePicker;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.Spinner;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class AddPlayerInfoFragment extends BaseTwoButtonActionsFormFragment  {

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
	protected Spinner m_spinnerMultiplePhones;
	protected Spinner m_spinnerMultipleEmails;
	
	private int m_indexOfLastAddedPlayer = 0;
	private ArrayList<ContactBean> m_contacts;
	private boolean m_addFromContacts = false;

	private String[] m_multipleEmails;

	private String[] m_multiplePhones;




	public AddPlayerInfoFragment() {
		super(R.layout.fragment_edit_player);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		
		Intent intent = getActivity().getIntent();
		
		if ( intent != null ) {
			Bundle bundle = intent.getExtras();
			if ( bundle != null ) {
				m_contacts = (ArrayList<ContactBean>) bundle.get(KeyConstants.KEY_CHOSEN_CONTACTS);
				if ( m_contacts != null && m_contacts.size() > 0 ) {
					m_addFromContacts = true;
					Log.d(LOG_TAG, "Found contacts to add from address book");
				}

			}
		}

		m_playerName = (EditText) m_root.findViewById(R.id.editTextPlayerName);
		m_playerLastName = (EditText) m_root.findViewById(R.id.editTextPlayerLastName);
		
		m_positionSpinner = (Spinner) m_root.findViewById(R.id.spinnerPosition);
		m_emailEditText = (EditText) m_root.findViewById(R.id.editTextMainEmail);
		m_phoneEditText = (EditText) m_root.findViewById(R.id.editTextMainPhone);
		m_shirtNumberSpinner = (Spinner) m_root.findViewById(R.id.spinnerShirtNumbers);
		m_spinnerMultipleEmails = (Spinner)m_root.findViewById(R.id.spinnerMultipleEmails);
		m_spinnerMultiplePhones = (Spinner)m_root.findViewById(R.id.spinnerMultiplePhones);
		ArrayAdapter<String> shirtNumbersAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, PlayerAndroidUtil.getTShirtNumbers(getActivity()));
		m_shirtNumberSpinner.setAdapter(shirtNumbersAdapter);

		m_birthDateCheckBox = (CheckBox) m_root.findViewById(R.id.checkboxBirthDate);
		m_birthDateDatePicker = (DatePicker) m_root.findViewById(R.id.datePickerBirthDate);

		m_birthDateCompositeField = new CheckboxWithViewGroupHelper(m_birthDateCheckBox, m_birthDateDatePicker);
		
		if ( m_addFromContacts ) {
			populateFormWithDataFromContact(0);
		}
		
		
		return m_root;
	}
	
	/**
	 * Populate form with data from contacts showing the spinner for emails or phone if the user has to choose from multiple choices
	 * @param i
	 */
	private void populateFormWithDataFromContact(int i) {
		ContactBean contact = m_contacts.get(i);
		if ( m_addFromContacts ) {
			writeContactDataInTheForm(contact);

		}
		
		if( !StringUtil.isNotEmpty(m_playerLastName.getText().toString()) ) {
			m_playerLastName.setError(getString(R.string.msg_player_lastname_is_mandatory));
		}
	}

	protected void writeContactDataInTheForm(ContactBean contact) {
		m_playerName.setText(contact.getFirstName());
		m_playerLastName.setText(contact.getLastName());
		if ( contact.getEmails() != null ) {
			if ( contact.getEmails().size() == 1 ) {
				m_emailEditText.setText(contact.getEmails().get(0));
			}
			else if ( contact.getEmails().size() > 1 ) {
				m_spinnerMultipleEmails.setVisibility(Spinner.VISIBLE);
				m_emailEditText.setVisibility(Spinner.GONE);
				
				int emailNumber = contact.getEmails().size();
				m_multipleEmails = new String[emailNumber];
				for ( int k = 0; k < emailNumber; k++ ) {
					m_multipleEmails[k] = contact.getEmails().get(k);
				}
				
				ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, m_multipleEmails);
				spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
				m_spinnerMultipleEmails.setAdapter(spinnerArrayAdapter);
			}
		}
		
		if ( contact.getPhones() != null ) {
			if ( contact.getPhones().size() == 1 ) {
				m_phoneEditText.setText(contact.getPhones().get(0));
			}
			else if ( contact.getPhones().size() > 1 ) {
				m_spinnerMultiplePhones.setVisibility(Spinner.VISIBLE);
				m_phoneEditText.setVisibility(Spinner.GONE);
				
				int phoneNumber = contact.getPhones().size();
				m_multiplePhones = new String[phoneNumber];
				for ( int k = 0; k < phoneNumber; k++ ) {
					m_multiplePhones[k] = contact.getPhones().get(k);
				}
				
				ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, m_multiplePhones);
				spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
				m_spinnerMultiplePhones.setAdapter(spinnerArrayAdapter);
			}
		}
	}


	protected void performActionsAndExit() {
		Log.d(LOG_TAG, "Entered players done");
		// If the the lastname has been entered store the player object
		if (StringUtil.isNotEmpty(m_playerLastName.getText().toString())) {
			savePlayer(true);
		}
		else {
			setResultAndExit();
		}


		
	}

	protected void savePlayer(boolean exitAfter) {
		if (m_player == null) {
			m_player = new PlayerBean();

		}

		populatePlayerObject();

		storePlayerInfo(false, exitAfter);
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
		m_menuItem1.setEnabled(true);

		m_shirtNumberSpinner.setSelection(0);
		m_emailEditText.setText(null);
		m_phoneEditText.setText(null);
		m_phoneEditText.setVisibility(EditText.VISIBLE);
		m_emailEditText.setVisibility(EditText.VISIBLE);
		m_spinnerMultipleEmails.setVisibility(Spinner.GONE);
		m_spinnerMultiplePhones.setVisibility(Spinner.GONE);
		m_spinnerMultipleEmails.setAdapter(null);
		m_spinnerMultiplePhones.setAdapter(null);

		m_birthDateCompositeField.setDateValue(null);
		
		m_multipleEmails = null;
		m_multiplePhones = null;
	}

	protected void populatePlayerObject() {
		m_player.setName(m_playerName.getText().toString());
		m_player.setLastName(m_playerLastName.getText().toString());
		m_player.setRole(m_positionSpinner.getSelectedItemPosition());
		m_player.setShirtNumber(m_shirtNumberSpinner.getSelectedItemPosition());
		
		// Check if user chosen a valid email from multiple choices (index = 0 is the hint of the spinner)
		if ( m_spinnerMultipleEmails.getVisibility() == Spinner.VISIBLE  && m_spinnerMultipleEmails.getSelectedItemPosition() > 0 ) {
			m_player.setEmail(m_multipleEmails[m_spinnerMultipleEmails.getSelectedItemPosition()]);
		}
		else {
			m_player.setEmail(m_emailEditText.getText().toString());
		}
		
		// Check if user chosen a valid phones from multiple choices (index = 0 is the hint of the spinner)
		if ( m_spinnerMultiplePhones.getVisibility() == Spinner.VISIBLE && m_spinnerMultiplePhones.getSelectedItemPosition() > 0 ) {
			m_player.setEmail(m_multiplePhones[m_spinnerMultiplePhones.getSelectedItemPosition()]);
		}
		else {
			m_player.setPhone(m_phoneEditText.getText().toString());
		}
		
		
		
		m_player.setBirthDate(m_birthDateCompositeField.getDateValueFromView());
	}

	protected void storePlayerInfo(final boolean update, final boolean exitAfter) {

		if (update) {
			DBManager.getInstance().updateBean(m_player);
		} else {
			DBManager.getInstance().storeBean(m_player);
		}
		Log.d(LOG_TAG,
				"Stored player: "
						+ PlayerAndroidUtil.toString(getActivity(), m_player));

		if (exitAfter) {
			setResultAndExit();
		} else {
			nextActionAfterPlayerStoring();
		}

	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//		switch (item.getItemId()) {
		//
		//		case R.id.menu_add_additionalInfo:
		//			Intent intent = new Intent(getActivity(),
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
		Log.d(LOG_TAG, "clickOnMenuItem1");
		if( !StringUtil.isNotEmpty(m_playerLastName.getText().toString()) ) {
			m_playerLastName.setError(getString(R.string.msg_player_lastname_is_mandatory));
			return;
		}
		
		savePlayer(false);

	}

	protected void nextActionAfterPlayerStoring() {
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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		m_menuItem1.setEnabled(true);
	}

	protected void setResultAndExit() {
		getActivity().setResult(
				MyTeamManagerActivity.RESULT_ENTER_PLAYERS_LIST_DONE);
		getActivity().finish();
	}
	
	
}
