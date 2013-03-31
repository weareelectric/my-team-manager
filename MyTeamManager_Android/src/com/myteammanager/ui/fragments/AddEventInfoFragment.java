package com.myteammanager.ui.fragments;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.myteammanager.MyTeamManagerActivity;
import com.myteammanager.R;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.EventBean;
import com.myteammanager.beans.MatchBean;
import com.myteammanager.beans.TeamBean;
import com.myteammanager.events.DatePickerValuesEvent;
import com.myteammanager.events.EventOrMatchChanged;
import com.myteammanager.events.TimePickerValuesEvent;
import com.myteammanager.storage.DBManager;
import com.myteammanager.util.DateTimeUtil;
import com.myteammanager.util.DeleteMatchManager;
import com.myteammanager.util.EventDeleteConfirmationManager;
import com.myteammanager.util.KeyConstants;
import com.myteammanager.util.StringUtil;
import com.squareup.otto.Subscribe;

public class AddEventInfoFragment extends BaseTwoButtonActionsFormFragment {

	private static final String LOG_TAG = AddEventInfoFragment.class.getName();

	/*	private static final String TAG_TAB_EVENT = "event";
		private static final String TAG_TAB_MATCH = "match";*/

	private static final int DATE_PICKER_FOR_EVENT_DATE = 0;
	private static final int DATE_PICKER_FOR_EVENT_END_REPEAT_DATE = 1;
	private static final int DATE_PICKER_FOR_MATCH_DATE = 2;
	private static final int TIME_PICKER_FOR_MATCH_TIME = 3;
	private static final int TIME_PICKER_FOR_EVENT_TIME = 4;

	private static final int SPINNER_REPEAT_NONE_POSITION = 0;
	private static final int SPINNER_REPEAT_DAILY_POSITION = 1;
	private static final int SPINNER_REPEAT_WEEKLY_POSITION = 2;

	protected EventBean m_event;
	protected MatchBean m_match;
	protected ArrayList<EventBean> m_repeatedEvents;

	protected LinearLayout m_addEventLayout;
	protected LinearLayout m_addMatchLayout;

	protected EditText m_opponentEditText;
	protected Spinner m_homeAwaySpinner;
	protected EditText m_matchDateEditText;
	protected EditText m_matchTimeEditText;
	protected EditText m_locationEditText;
	protected EditText m_noteEditText;

	protected EditText m_eventDateEditText;
	protected EditText m_eventTimeEditText;
	protected EditText m_eventEndRepeatDateEditText;
	protected Spinner m_eventRepeatSpinner;
	protected EditText m_eventLocationEditText;
	protected EditText m_eventNoteEditText;
	protected EditText m_editTextForFocus;
	protected TextView m_endRepeatDataText;

	private boolean m_isEvent = false;
	private boolean m_isUpdate = false;

	private boolean m_datePickerAlreadyShown = false;

	private Menu m_menu;
	private EventBean m_parentEvent;
	private Context m_context;

	public AddEventInfoFragment() {
		super(R.layout.fragment_edit_event);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		
		m_context = getSherlockActivity();

		Bundle extra = getSherlockActivity().getIntent().getExtras();
		m_isEvent = extra.getBoolean(KeyConstants.KEY_EVENT_OR_MATCH);
		Log.d(LOG_TAG, "m_isEvent: " + m_isEvent);

		if (m_isEvent) {
			getSherlockActivity().setTitle(R.string.title_add_event);
			m_event = (EventBean) extra.get(KeyConstants.KEY_BEANDATA);
			Log.d(LOG_TAG, "m_event: " + m_event);
			if (m_event != null) {
				m_isUpdate = true;
			} else {
				m_event = new EventBean();
			}
		} else {
			getSherlockActivity().setTitle(R.string.title_add_match);
			m_match = (MatchBean) extra.get(KeyConstants.KEY_BEANDATA);
			Log.d(LOG_TAG, "m_match: " + m_match);
			if (m_match != null) {
				m_isUpdate = true;
			} else {
				m_match = new MatchBean();
			}
			
		}

		Log.d(LOG_TAG, "m_isUpdate: " + m_isUpdate);
		if (!m_isUpdate) {
			Log.d(LOG_TAG, "Remove delete menu item");
			m_menu.removeItem(R.id.menu_edit_delete_event_match);
		}

		m_addEventLayout = (LinearLayout) m_root.findViewById(R.id.editEventLayout);
		m_addMatchLayout = (LinearLayout) m_root.findViewById(R.id.editMatchLayout);

		// User add match for default m_addEventLayout = (LinearLayout)m_root.findViewById(R.id.editEventLayout)
		m_addEventLayout.setVisibility(View.GONE);

		// Match
		m_opponentEditText = (EditText) m_root.findViewById(R.id.textFieldOpponent);
		m_matchDateEditText = (EditText) m_root.findViewById(R.id.datePickerMatchDate);
		m_matchTimeEditText = (EditText) m_root.findViewById(R.id.timePickerMatchTime);
		m_locationEditText = (EditText) m_root.findViewById(R.id.textFieldLocation);
		m_noteEditText = (EditText) m_root.findViewById(R.id.textFieldNote);
		m_homeAwaySpinner = (Spinner) m_root.findViewById(R.id.spinnerHomeAway);
		// m_matchCancledCheckBox = (CheckBox) m_root.findViewById(R.id.checkBoxCanceled);

		m_matchDateEditText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showMatchDatePicker();
			}
		});

		m_matchTimeEditText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showMatchTimePicker();
			}
		});

		// Event

		m_eventDateEditText = (EditText) m_root.findViewById(R.id.datePickerEventDate);
		m_eventTimeEditText = (EditText) m_root.findViewById(R.id.timePickerEventTime);
		m_eventEndRepeatDateEditText = (EditText) m_root.findViewById(R.id.textFieldEndRepeatDate);

		m_eventRepeatSpinner = (Spinner) m_root.findViewById(R.id.spinnerRepeat);
		m_eventLocationEditText = (EditText) m_root.findViewById(R.id.textFieldEventLocation);
		m_eventNoteEditText = (EditText) m_root.findViewById(R.id.textFieldEventNote);
		m_endRepeatDataText = (TextView) m_root.findViewById(R.id.textFieldRepeat);
		m_editTextForFocus = (EditText)m_root.findViewById(R.id.editTextForFocus);
		m_editTextForFocus.requestFocus();

		m_eventDateEditText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showEventDatePicker();
			}
		});

		m_eventTimeEditText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showEventTimePicker();
			}
		});

		m_eventEndRepeatDateEditText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showEventEndDateRepeatPicker();
			}
		});

		m_eventRepeatSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				if (position == SPINNER_REPEAT_NONE_POSITION) {
					m_eventEndRepeatDateEditText.setVisibility(View.GONE);
					m_endRepeatDataText.setVisibility(View.GONE);
				} else {
					m_eventEndRepeatDateEditText.setVisibility(View.VISIBLE);
					m_endRepeatDataText.setVisibility(View.VISIBLE);

					// Set a date one day or one week later if it is the first time we show the date picker
					if (!m_datePickerAlreadyShown && !m_isUpdate) {
						if (position == SPINNER_REPEAT_DAILY_POSITION) {
							long endRepeat = System.currentTimeMillis() + 86400000;
							m_event.setRepeatEndDate(new Date(endRepeat));
							m_eventEndRepeatDateEditText.setText(DateUtils.formatDateTime(m_context, endRepeat,
									DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
						} else {
							long endRepeat = System.currentTimeMillis() + 604800000;
							m_event.setRepeatEndDate(new Date(endRepeat));
							m_eventEndRepeatDateEditText.setText(DateUtils.formatDateTime(m_context, endRepeat,
									DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
						}
					}

					m_datePickerAlreadyShown = true;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}

		});

		if (m_isUpdate) {
			populateFields();

		}

		m_menuItem1.setEnabled(true);

		setEventOrMatchView();

		MyTeamManagerActivity.getBus().register(this);
		MyTeamManagerActivity.getBus().register(this);

		return m_root;
	}

	private void populateFields() {
		if (m_isEvent) {
			populateEventForm();
		} else {
			populateMatchForm();
		}
	}

	private void populateMatchForm() {
		m_opponentEditText.setText(m_match.getOpponentTeam());
		m_noteEditText.setText(m_match.getNote());
		m_homeAwaySpinner.setSelection(m_match.getHomeAwayType());
		refreshtMatchDateAndTime();
		m_locationEditText.setText(m_match.getLocation());
		// m_matchCancledCheckBox.setChecked(m_match.getCanceled() == 1);
	}

	public void refreshtMatchDateAndTime() {
		long timestamp = m_match.getTimestamp();
		if ( timestamp > 0 ) {
			m_matchDateEditText.setText(DateUtils.formatDateTime(m_context, timestamp,
					DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
			m_matchTimeEditText.setText(DateUtils.formatDateTime(m_context, timestamp,
					DateUtils.FORMAT_SHOW_TIME));
		}
		
	}

	private void populateEventForm() {
		refreshEventDateAndTime();
		m_eventRepeatSpinner.setSelection(m_event.getRepeat());
		m_eventLocationEditText.setText(m_event.getLocation());
		m_eventNoteEditText.setText(m_event.getNote());

	}

	public void refreshEventDateAndTime() {
		Log.d(LOG_TAG, "refreshEventDateAndTime: " + m_event.getTimestamp());
		
		m_eventDateEditText.setText(DateUtils.formatDateTime(m_context, m_event.getTimestamp(),
				DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
		m_eventTimeEditText.setText(DateUtils.formatDateTime(m_context, m_event.getTimestamp(),
				DateUtils.FORMAT_SHOW_TIME));
		m_eventEndRepeatDateEditText.setText(DateUtils.formatDateTime(m_context,
				m_event.getRepeatEndDateLong(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
	}


	protected void performActionsAndExit() {
		
		// If the button save is enabled user entered some valid info. Save player 
		saveEvent();

		notifyForResult();

	}

	private void notifyForResult() {
		Intent intent = new Intent();
		int resultCode = 0;

		resultCode = KeyConstants.RESULT_BEAN_ADDED;
		intent.setAction(KeyConstants.INTENT_BEAN_ADDED);

		if (m_isUpdate) {
			resultCode = KeyConstants.RESULT_BEAN_EDITED;
			intent.setAction(KeyConstants.INTENT_BEAN_CHANGED);
		}

		if (m_isEvent) {

			intent.putExtra(KeyConstants.KEY_BEANDATA, m_event);

			if (m_repeatedEvents != null) {
				intent.putExtra(KeyConstants.KEY_BEANDATA_LIST, m_repeatedEvents);
			}
		} else {
			intent.putExtra(KeyConstants.KEY_BEANDATA, m_match);
		}

		Log.d(LOG_TAG, "Result for calling activity: " + resultCode);
		getSherlockActivity().setResult(resultCode, intent);

		// Notify also possible broadcastReceiver 
		getSherlockActivity().sendBroadcast(intent);
	}

	protected void saveEvent() {

		if (m_isEvent) {
			if (m_event == null) {
				m_event = new EventBean();
			}
			populatePracticeObject(m_event);
			storeMainEventInfo(m_event);
			

			// create and store repeated events
			int dayRepeatInterval = -1;
			if (m_eventRepeatSpinner.getSelectedItemPosition() == SPINNER_REPEAT_DAILY_POSITION) {
				dayRepeatInterval = 1;
			} else if (m_eventRepeatSpinner.getSelectedItemPosition() == SPINNER_REPEAT_WEEKLY_POSITION) {
				dayRepeatInterval = 7;
			}
			if (!m_isUpdate && dayRepeatInterval != -1) {
				ArrayList<Date> datesForRepeatedEvents = DateTimeUtil.getDatesStartingFromToWithInterval(
						m_event.getTimestamp(), m_event.getRepeatEndDate().getTime(), dayRepeatInterval);
				for (Date date : datesForRepeatedEvents) {
					Log.d(LOG_TAG, "Date: " + date.toString());
					Log.d(LOG_TAG, "Parent event: : " + m_parentEvent);
					EventBean childevent = new EventBean();
					childevent.setParentEvent(m_parentEvent);
					populatePracticeObject(childevent);

					// Now change the value because this is an event repeated
					childevent.setRepeat(SPINNER_REPEAT_NONE_POSITION);
					childevent.setRepeatEndDate(null);
					childevent.setTimestamp(date.getTime());

					if (m_repeatedEvents == null) {
						m_repeatedEvents = new ArrayList<EventBean>();
					}

					m_repeatedEvents.add(childevent);

				}

			}

			if (m_repeatedEvents != null) {
				// the activity will be closed after the list of events is stored and the notify result is called in doActionAfetrInsertBeans
				storeEventsList(m_repeatedEvents);
			} else {
				Intent intent = new Intent();
				intent.putExtra(KeyConstants.KEY_BEANDATA, m_event);

				int resultCode = KeyConstants.RESULT_BEAN_ADDED;

				if (m_isUpdate) {
					resultCode = KeyConstants.RESULT_BEAN_EDITED;
				}

				getSherlockActivity().setResult(resultCode, intent);
				
				notifyForResult();

				MyTeamManagerActivity.getBus().post(new EventOrMatchChanged());
				getSherlockActivity().finish();
			}

		} else {
			if (m_match == null) {
				m_match = new MatchBean();
			}

			populateMatchObject();
			storeMainEventInfo(m_match);

			MyTeamManagerActivity.getBus().post(new EventOrMatchChanged());

			int resultCode = KeyConstants.RESULT_BEAN_ADDED;

			if (m_isUpdate) {
				resultCode = KeyConstants.RESULT_BEAN_EDITED;
			}

			Intent intent = new Intent();
			intent.putExtra(KeyConstants.KEY_BEANDATA, m_match);
			
			notifyForResult();

			getSherlockActivity().setResult(resultCode, intent);

			getSherlockActivity().finish();
		}

	}

	protected void resetObjectAndInterface() {
		if (m_event != null) {
			m_event.reset();
		}

		if (m_match != null) {
			m_match.reset();
		}

		// Reset the fields to be used for the next player data
		resetInterface();
	}

	private void resetInterface() {
		m_opponentEditText.setText("");
		m_locationEditText.setText("");
		m_noteEditText.setText("");
		m_opponentEditText.requestFocus();
		m_homeAwaySpinner.setSelection(0);

		ActionBar actionBar = getSherlockActivity().getSupportActionBar();


		// m_matchCancledCheckBox.setChecked(false);
		m_event = null;
		m_match = null;

		if (m_repeatedEvents != null) {
			m_repeatedEvents.clear();
			m_repeatedEvents = null;
		}

		m_locationEditText.setText("");
		m_matchDateEditText.setText("");
		m_matchTimeEditText.setText("");

		m_eventDateEditText.setText("");
		m_eventTimeEditText.setText("");
		m_eventLocationEditText.setText("");
		m_eventRepeatSpinner.setSelection(SPINNER_REPEAT_NONE_POSITION);
		m_eventEndRepeatDateEditText.setVisibility(View.GONE);
		m_endRepeatDataText.setVisibility(View.GONE);
		m_eventNoteEditText.setText("");
		// m_eventCanceledCheckBox.setChecked(false);
	}

	protected void populateMatchObject() {
		m_match.setHomeAwayType(m_homeAwaySpinner.getSelectedItemPosition());
		// m_match.setCanceled(m_matchCancledCheckBox.isChecked() ? 1 : 0);

		if (m_match.getTeam1() == null) {
			if (m_match.getHomeAwayType() == MatchBean.TYPE_AWAY) {
				m_match.setTeam1(new TeamBean(-1, m_opponentEditText.getEditableText().toString()));
				m_match.setTeam2(null);
				m_match.setHomeAwayType(MatchBean.TYPE_AWAY);
			} else {
				m_match.setTeam2(new TeamBean(-1, m_opponentEditText.getEditableText().toString()));
				m_match.setTeam1(null);
				m_match.setHomeAwayType(MatchBean.TYPE_HOME);
			}
		} else {
			if (m_match.getHomeAwayType() == MatchBean.TYPE_AWAY) {
				if (m_match.getTeam1() == null) {
					m_match.setTeam1(new TeamBean(-1, m_opponentEditText.getEditableText().toString()));
				} else {
					m_match.getTeam1().setName(m_opponentEditText.getEditableText().toString());
				}
				m_match.getTeam1().setName(m_opponentEditText.getEditableText().toString());
				m_match.setTeam2(null);
				m_match.setHomeAwayType(MatchBean.TYPE_AWAY);
			} else {
				if (m_match.getTeam2() == null) {
					m_match.setTeam2(new TeamBean(-1, m_opponentEditText.getEditableText().toString()));
				} else {
					m_match.getTeam2().setName(m_opponentEditText.getEditableText().toString());
				}

				m_match.setTeam1(null);
				m_match.setHomeAwayType(MatchBean.TYPE_HOME);
			}
		}

		m_match.setLocation(m_locationEditText.getText().toString());
		m_match.setNote(m_noteEditText.getText().toString());
	}

	protected void populatePracticeObject(EventBean eventbean) {
		// eventbean.setCanceled(m_eventCanceledCheckBox.isChecked() ? 1 : 0);

		eventbean.setRepeat(m_eventRepeatSpinner.getSelectedItemPosition());

		eventbean.setLocation(m_eventLocationEditText.getText().toString());
		eventbean.setNote(m_eventNoteEditText.getText().toString());

	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.event_menu_edit, menu);
		m_menu = menu;
		super.onCreateOptionsMenu(menu, inflater);

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_edit_delete_event_match:
			showThreeButtonDialog(getDeleteConfirmationTitle(), getDeleteConfirmationMsg(),
					R.string.alert_delete_event_conf_btn1, R.string.alert_delete_event_conf_btn2,
					R.string.alert_delete_event_conf_btn3);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public String getDeleteConfirmationMsg() {
		Resources res = getSherlockActivity().getResources();
		return res.getString(R.string.alert_event_confirmation_msg);
	}

	protected void storeMainEventInfo(BaseBean bean) {
		if (m_isUpdate) {
			DBManager.getInstance().updateBean(bean);
		} else {
			long id = DBManager.getInstance().storeBean(bean);
			if (m_parentEvent == null)
				m_parentEvent = new EventBean();
			m_parentEvent.setId((int) id);
		}
	}

	protected void storeEventsList(ArrayList<? extends BaseBean> beans) {

		if (m_isUpdate) {
			insertBeans(beans, true);
		} else {
			insertBeans(beans, false);
		}
	}

	@Override
	protected void clickOnMenuItem1() {
		boolean canSave = validateData();

		if ( canSave ) {
			saveEvent();
		}
		

		
	}

	protected boolean validateData() {
		boolean canSave = true;
		if ( !m_isEvent ) {
			if (!StringUtil.isNotEmpty(m_opponentEditText.getText().toString())) {
				m_opponentEditText
						.setError(getString(R.string.msg_player_lastname_is_mandatory));
				canSave = false;
			}
		}
		else {
			if (!StringUtil.isNotEmpty(m_eventDateEditText.getText().toString())) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity()).setMessage(getString(R.string.msg_date_needed_for_event));
				builder.show();
				canSave = false;
			}
		}
		return canSave;
	}

	@Override
	protected void clickOnMenuItem2() {
		boolean canSave = validateData();
		
		if ( canSave ) {
			performActionsAndExit();
		}
	}

	@Override
	protected void customizeMenuItem1(View root) {
		m_menuItem1.setTitle(getSherlockActivity().getResources().getString(R.string.save));
	}

	@Override
	protected void customizeMenuItem2(View root) {
		m_menuItem2.setVisible(false);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(LOG_TAG, "Received result <" + requestCode + "> <" + resultCode + ">");

		//		if (requestCode == CODE_PLAYER_DETAILS_EDITED) {
		//			if (resultCode == RESULT_PLAYER_DETAILS_EDITED) {
		//				m_player = (PlayerBean)data.getExtras().get(KEY_PLAYER);
		//			}
		//		}
	}

	private void setEventOrMatchView() {
		if (m_isEvent) {
			m_addMatchLayout.setVisibility(View.GONE);
			m_addEventLayout.setVisibility(View.VISIBLE);

		} else {
			m_addEventLayout.setVisibility(View.GONE);
			m_addMatchLayout.setVisibility(View.VISIBLE);

			
			m_opponentEditText.requestFocus();
			getSherlockActivity().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		}
	}

	@Override
	public void button1Pressed(int alertId) {
		Intent intent = new Intent();
		if (m_isEvent) {
			intent.putExtra(KeyConstants.KEY_BEANDATA, m_event);
			DBManager.getInstance().deleteBean(m_event);
		} else {
			intent.putExtra(KeyConstants.KEY_BEANDATA, m_match);
			DeleteMatchManager.deleteMatch(m_match, getSherlockActivity());
		}

		intent.setAction(KeyConstants.INTENT_BEAN_DELETED);
		getActivity().setResult(KeyConstants.RESULT_BEAN_DELETED, intent);

		// Notify also possible broadcastReceiver 
		getSherlockActivity().sendBroadcast(intent);
		MyTeamManagerActivity.getBus().post(new EventOrMatchChanged());
		getSherlockActivity().finish();
	}

	@Override
	public void button2Pressed(int alertId) {

		Intent intent = new Intent();
		intent.putExtra(KeyConstants.KEY_BEANDATA, m_event);
		EventDeleteConfirmationManager.deleteAllLinkedEvents(m_event, getSherlockActivity(), true);

		intent.setAction(KeyConstants.INTENT_BEAN_DELETED);
		getActivity().setResult(KeyConstants.RESULT_BEAN_DELETED, intent);

		MyTeamManagerActivity.getBus().post(new EventOrMatchChanged());
		getSherlockActivity().finish();
	}

	@Override
	public void button3Pressed(int alertId) {

	}

	private void showDatePicker(int id, long timestamp) {
		DialogFragment newFragment = new DatePickerFragment(id, timestamp);
		newFragment.show(getSherlockActivity().getSupportFragmentManager(), "datePicker");
	}

	private void showTimePicker(int id, long timestamp) {
		DialogFragment newFragment = new TimePickerFragment(id, timestamp);
		newFragment.show(getSherlockActivity().getSupportFragmentManager(), "timePicker");
	}

	private void showMatchDatePicker() {
		showDatePicker(DATE_PICKER_FOR_MATCH_DATE, m_match.getTimestamp());
	}

	private void showMatchTimePicker() {
		showTimePicker(TIME_PICKER_FOR_MATCH_TIME, m_match.getTimestamp());
	}

	private void showEventEndDateRepeatPicker() {
		showDatePicker(DATE_PICKER_FOR_EVENT_END_REPEAT_DATE, m_event.getRepeatEndDateLong());
	}

	private void showEventDatePicker() {
		showDatePicker(DATE_PICKER_FOR_EVENT_DATE, m_event.getTimestamp());
	}

	private void showEventTimePicker() {
		showTimePicker(TIME_PICKER_FOR_EVENT_TIME, m_event.getTimestamp());
	}

	@Subscribe
	public void datePickerChanged(DatePickerValuesEvent event) {
		Log.d(LOG_TAG, "datePickerChanged " + event.getTimestamp());
		Log.d(LOG_TAG, "datePickerChanged " + event.getId());
		switch (event.getId()) {
		case DATE_PICKER_FOR_EVENT_DATE:
			m_event.setTimestamp(event.getTimestamp());
			refreshEventDateAndTime();
			break;

		case DATE_PICKER_FOR_EVENT_END_REPEAT_DATE:
			m_event.setRepeatEndDate(new Date(event.getTimestamp()));
			refreshEventDateAndTime();
			break;

		case DATE_PICKER_FOR_MATCH_DATE:
			m_match.setTimestamp(event.getTimestamp());
			refreshtMatchDateAndTime();
			break;
		}
	}

	@Subscribe
	public void timePickerChanged(TimePickerValuesEvent event) {
		Log.d(LOG_TAG, "timePickerChanged " + event.getId());
		switch (event.getId()) {
		case TIME_PICKER_FOR_EVENT_TIME:
			m_event.setTimestamp(event.getTimestamp());
			refreshEventDateAndTime();
			break;

		case TIME_PICKER_FOR_MATCH_TIME:
			m_match.setTimestamp(event.getTimestamp());
			refreshtMatchDateAndTime();
			break;
		}
	}

	@Override
	protected void doActionAfetrInsertBeans() {
		notifyForResult();
		MyTeamManagerActivity.getBus().post(new EventOrMatchChanged());
	}

}
