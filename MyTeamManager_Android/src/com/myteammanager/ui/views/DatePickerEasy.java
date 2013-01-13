package com.myteammanager.ui.views;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.DatePicker;

public class DatePickerEasy extends DatePicker {

	public DatePickerEasy(Context context) {
		super(context);
	}

	public DatePickerEasy(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public DatePickerEasy(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setDateValue(Date date) {
		Calendar calendar = Calendar.getInstance();
		if ( date == null ) {
			date = new Date();
		}
		calendar.setTime(date);
		updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
	}
	
	public Date getDateValue() {
		int day = getDayOfMonth();
		int month = getMonth();
		int year = getYear();
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		
		return calendar.getTime();
	}

}
