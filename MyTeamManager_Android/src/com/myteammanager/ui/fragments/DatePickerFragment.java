package com.myteammanager.ui.fragments;

import java.util.Calendar;

import com.myteammanager.MyTeamManagerActivity;
import com.myteammanager.events.DatePickerValuesEvent;
import com.myteammanager.events.TimePickerValuesEvent;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements OnDateSetListener {

	private int m_id;
	private long m_timestamp = -1;
	private Calendar m_calendar;

	public DatePickerFragment(int m_id, long timestamp) {
		super();
		this.m_id = m_id;
		this.m_timestamp = timestamp;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		m_calendar = Calendar.getInstance();

		if (m_timestamp > 0) {
			m_calendar.setTimeInMillis(m_timestamp);
		}

		int year = m_calendar.get(Calendar.YEAR);
		int month = m_calendar.get(Calendar.MONTH);
		int day = m_calendar.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		m_calendar.set(Calendar.YEAR, year);
		m_calendar.set(Calendar.MONTH, month);
		m_calendar.set(Calendar.DAY_OF_MONTH, day);

		MyTeamManagerActivity.getBus().post(new DatePickerValuesEvent(m_id, m_calendar.getTimeInMillis()));
	}

}
