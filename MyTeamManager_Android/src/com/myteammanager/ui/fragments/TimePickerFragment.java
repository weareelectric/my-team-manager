package com.myteammanager.ui.fragments;

import java.util.Calendar;

import com.myteammanager.MyTeamManagerActivity;
import com.myteammanager.events.TimePickerValuesEvent;

import android.animation.TimeInterpolator;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

	private int m_id;
	private long m_timestamp = -1;
	private Calendar m_calendar;

	public TimePickerFragment(int m_id, long timestamp) {
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

		int hour = m_calendar.get(Calendar.HOUR_OF_DAY);
		int minute = m_calendar.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		m_calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		m_calendar.set(Calendar.MINUTE, minute);

		MyTeamManagerActivity.getBus().post(new TimePickerValuesEvent(m_id, m_calendar.getTimeInMillis()));
	}

}
