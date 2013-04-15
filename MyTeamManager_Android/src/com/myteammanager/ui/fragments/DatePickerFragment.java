package com.myteammanager.ui.fragments;

import java.util.Calendar;

import com.myteammanager.MyTeamManagerActivity;
import com.myteammanager.events.DatePickerValuesEvent;

import org.holoeverywhere.app.DatePickerDialog;
import org.holoeverywhere.app.DatePickerDialog.OnDateSetListener;
import org.holoeverywhere.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import org.holoeverywhere.app.DialogFragment;
import org.holoeverywhere.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements OnDateSetListener {

	private int m_id;
	private long m_timestamp = -1;
	private Calendar m_calendar;

	public DatePickerFragment(int m_id, long timestamp) {
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
		return new DatePickerDialog(getActivity(), this, year, month, day) {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				super.onClick(dialog, which);
		        if (which != DialogInterface.BUTTON_POSITIVE) {
		        	dismiss();
		        }
			}
			
		};
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		m_calendar.set(Calendar.YEAR, year);
		m_calendar.set(Calendar.MONTH, month);
		m_calendar.set(Calendar.DAY_OF_MONTH, day);

		MyTeamManagerActivity.getBus().post(new DatePickerValuesEvent(m_id, m_calendar.getTimeInMillis()));
		
		dismiss();
	}
	



	
	

}
