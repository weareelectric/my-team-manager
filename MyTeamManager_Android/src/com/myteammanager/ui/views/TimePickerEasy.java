package com.myteammanager.ui.views;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TimePicker;

public class TimePickerEasy extends TimePicker {

	private Calendar m_calendar;

	public TimePickerEasy(Context context) {
		super(context);
	}

	public TimePickerEasy(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TimePickerEasy(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setCalendar(Calendar m_calendar) {
		this.m_calendar = m_calendar;
	}

	public void setTimeValue(long timestamp) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(timestamp));
		setCurrentHour(calendar.get(Calendar.HOUR));
		setCurrentMinute(calendar.get(Calendar.MINUTE));
	}

	public long getTimeValue() {

		if (m_calendar == null) {
			m_calendar = Calendar.getInstance();
		}

		Log.d("TimePickerEasy", "hour: " + getCurrentHour());
		Log.d("TimePickerEasy", "minute: " + getCurrentMinute());
		Log.d("TimePickerEasy", "jour: " + m_calendar.get(Calendar.DAY_OF_MONTH));
		Log.d("TimePickerEasy", "month: " + m_calendar.get(Calendar.MONTH));
		Log.d("TimePickerEasy", "year: " + m_calendar.get(Calendar.YEAR));
		m_calendar.set(Calendar.HOUR, getCurrentHour());
		m_calendar.set(Calendar.MINUTE, getCurrentMinute());

		Log.d("TimePickerEasy", "calendar.getTimeInMillis(): " + m_calendar.getTimeInMillis());

		return m_calendar.getTimeInMillis();
	}

}
