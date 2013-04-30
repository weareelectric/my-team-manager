package com.myteammanager.ui.fragments;

import java.util.ArrayList;
import java.util.Date;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import org.holoeverywhere.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.holoeverywhere.widget.AdapterView;
import org.holoeverywhere.widget.AdapterView.OnItemSelectedListener;
import org.holoeverywhere.widget.CheckBox;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.LinearLayout;
import android.widget.ScrollView;
import org.holoeverywhere.widget.Spinner;
import org.holoeverywhere.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.myteammanager.MyTeamManagerActivity;
import com.myteammanager.R;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.EventBean;
import com.myteammanager.beans.MatchBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.beans.TeamBean;
import com.myteammanager.events.EventOrMatchChanged;
import com.myteammanager.storage.DBManager;
import com.myteammanager.ui.phone.AddEventInfoActivity;
import com.myteammanager.ui.views.DatePickerEasy;
import com.myteammanager.ui.views.TimePickerEasy;
import com.myteammanager.util.CheckboxWithViewGroupHelper;
import com.myteammanager.util.DateTimeUtil;
import com.myteammanager.util.EventDeleteConfirmationManager;
import com.myteammanager.util.KeyConstants;
import com.myteammanager.util.StringUtil;

public class EventDetailFragment extends BaseFragment implements TextWatcher {

	private static final String LOG_TAG = EventDetailFragment.class.getName();

	private static final String TAG_TAB_EVENT = "event";

	private EventBean m_event;

	private TextView m_textViewEventDate;
	private TextView m_textViewEventTime;
	private TextView m_textViewEventLocation;
	// private TextView m_textViewEventArrivalTime;
	private TextView m_textViewEventNote;
	// private CheckBox m_eventCanceledCheckBox;

	protected ScrollView m_root;

	private Resources m_res;

	public EventDetailFragment() {
		super();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		m_root = (ScrollView) inflater.inflate(R.layout.fragment_event_detail, null, false);

		Bundle extra = getActivity().getIntent().getExtras();
		m_res = getActivity().getResources();

		m_event = (EventBean) extra.get(KeyConstants.KEY_BEANDATA);
		getActivity().setTitle(m_res.getString(R.string.label_practice));

		// Event
		// m_textViewEventArrivalTime = (TextView) m_root.findViewById(R.id.textViewArrivalTime);
		m_textViewEventTime = (TextView) m_root.findViewById(R.id.textViewTime);
		m_textViewEventLocation = (TextView) m_root.findViewById(R.id.textViewLocation);
		m_textViewEventDate = (TextView) m_root.findViewById(R.id.textViewDate);
		m_textViewEventNote = (TextView) m_root.findViewById(R.id.textViewNote);
		// m_eventCanceledCheckBox = (CheckBox) m_root.findViewById(R.id.checkBoxCanceled);

		updateFields();

		return m_root;
	}

	@Override
	public String getDeleteConfirmationMsg() {
		Resources res = getActivity().getResources();
		return res.getString(R.string.alert_event_confirmation_msg);
	}

	public void updateFields() {
		m_textViewEventDate.setText(m_res.getString(R.string.label_date_semicoloumn,
				DateTimeUtil.getDateFrom(m_event.getTimestamp(), getActivity())));
		// m_textViewEventArrivalTime.setText(m_res.getString(R.string.label_arrival_time_semicoloumn,
				// DateTimeUtil.getTimeStringFrom(m_event.getArrivalTime())));
		m_textViewEventTime.setText(m_res.getString(R.string.label_time_semicoloumn,
				DateTimeUtil.getTimeStringFrom(m_event.getTimestamp())));
		m_textViewEventLocation.setText(m_res.getString(R.string.label_location_semicoloumn, m_event.getLocation()));
		m_textViewEventNote.setText(m_res.getString(R.string.label_note_semicoloumn, m_event.getNote()));
		// m_eventCanceledCheckBox.setChecked(m_event.getCanceled() == 1);

	}

	@Override
	public void afterTextChanged(Editable arg0) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void button1Pressed(int alertId) {
		Log.d(LOG_TAG, "button2Pressed");
		DBManager.getInstance().deleteBean(m_event);
		
		MyTeamManagerActivity.getBus().post(new EventOrMatchChanged());

		Intent intent = new Intent();
		intent.putExtra(KeyConstants.KEY_BEANDATA, m_event);
		getActivity().setResult(KeyConstants.RESULT_BEAN_DELETED, intent);

		getActivity().finish();
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.event_menu_detail, menu);
		
		MenuItem shareMatch = menu.findItem(R.id.menu_share_match);
		shareMatch.setVisible(false);
		
		super.onCreateOptionsMenu(menu, inflater);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_edit_event_match:
			Intent intent = new Intent(getActivity(), AddEventInfoActivity.class);
			intent.putExtra(KeyConstants.KEY_EVENT_OR_MATCH, true);
			intent.putExtra(KeyConstants.KEY_BEANDATA, m_event);
			startActivityForResult(intent, KeyConstants.CODE_BEAN_CHANGE);
			break;

		case R.id.menu_edit_delete_event_match:
			showThreeButtonDialog(getDeleteConfirmationTitle(), getDeleteConfirmationMsg(),
					R.string.alert_delete_event_conf_btn1, R.string.alert_delete_event_conf_btn2,
					R.string.alert_delete_event_conf_btn3);
			break;
		}
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(LOG_TAG, "requestCode: " + requestCode);
		Log.d(LOG_TAG, "resultCode: " + resultCode);
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == KeyConstants.CODE_BEAN_CHANGE) {
			if (resultCode == KeyConstants.RESULT_BEAN_EDITED) {

				m_event = (EventBean) data.getExtras().get(KeyConstants.KEY_BEANDATA);
				updateFields();
			} else if (resultCode == KeyConstants.RESULT_BEAN_DELETED) {
				getActivity().finish();
			}
		}
	}

	@Override
	public void button2Pressed(int alertId) {
		Log.d(LOG_TAG, "button2Pressed");
		Intent intent = new Intent();
		intent.putExtra(KeyConstants.KEY_BEANDATA, m_event);
		EventDeleteConfirmationManager.deleteAllLinkedEvents(m_event, getActivity(), true);
		
		MyTeamManagerActivity.getBus().post(new EventOrMatchChanged());

		intent.setAction(KeyConstants.INTENT_BEAN_DELETED);
		getActivity().setResult(KeyConstants.RESULT_BEAN_DELETED, intent);

		getActivity().finish();

	}

	@Override
	public void button3Pressed(int alertId) {
		// TODO Auto-generated method stub

	}

}
