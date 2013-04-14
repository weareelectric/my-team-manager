package com.myteammanager.ui.fragments;

import java.util.Calendar;

import com.myteammanager.MyTeamManagerActivity;
import com.myteammanager.R;
import com.myteammanager.events.TimePickerValuesEvent;

import android.animation.TimeInterpolator;
import android.content.DialogInterface;

import org.holoeverywhere.app.Dialog;
import org.holoeverywhere.app.TimePickerDialog;
import android.os.Bundle;
import org.holoeverywhere.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import org.holoeverywhere.widget.TimePicker;

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

		// Create a new instance of TimePickerDialog and return it. Need a fix for Holoeverywhere that doesn' take in count click on cancel
		TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));

		timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getText(R.string.cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss();
			}
			
		});
		return timePickerDialog ;
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		Log.d("TimePickerDialog", "Maremma maiala");
		m_calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		m_calendar.set(Calendar.MINUTE, minute);

		MyTeamManagerActivity.getBus().post(new TimePickerValuesEvent(m_id, m_calendar.getTimeInMillis()));
		
		dismiss();
		
	}
	
	

}
