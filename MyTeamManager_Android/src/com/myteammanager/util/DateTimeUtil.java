package com.myteammanager.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.text.format.DateFormat;

public class DateTimeUtil {

	public static long getDateLong(Date date) {
		if (date == null) {
			return 0;
		} else {
			return date.getTime();
		}
	}

	public static Date getDateFromLong(long dateLong) {
		if (dateLong == 0) {
			return null;
		}
		return new Date(dateLong);
	}
	
	/**
	 * Add days to an existent date
	 * @param date
	 * @param days
	 * @return
	 */
    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

	/**
	 * Return un ArrayList of Date objects from the starting date at the end date using the interval time from a date and
	 * the next one
	 * 
	 * @param startDate
	 * @param endDate
	 * @param dayInterval
	 * @return
	 */
	public static ArrayList<Date> getDatesStartingFromToWithInterval(long startDate, long endDate, int dayInterval) {
		ArrayList<Date> dates = new ArrayList<Date>();

		Date date = addDays(new Date(startDate), dayInterval);
		dates.add(date);
		while (date.getTime() <= endDate) {
			date = addDays(date, dayInterval);
			dates.add(date);
		}

		return dates;
	}

	public static String getTimeStringFrom(long timestamp) {
		if (timestamp <= 0)
			return "";

		Date date = new Date(timestamp);
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

		return formatter.format(date);
	}

	public static Calendar getCalendarFormDate(Date date) {
		if (date == null) {
			return null;
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return calendar;
		}
	}

	public static String getDateFrom(long timestamp, Context context) {
		if (timestamp <= 0)
			return "";

		return DateFormat.getDateFormat(context).format(getDateFromLong(timestamp));
	}
}
