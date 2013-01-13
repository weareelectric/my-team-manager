package com.myteammanager.beans.comparators;

import java.util.Comparator;
import java.util.Date;

import android.util.Log;

import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.EventBean;

public class EventComparator implements Comparator<EventBean> {

	@Override
	public int compare(EventBean event1, EventBean event2) {
		Date date1 = new Date(event1.getTimestamp());
		Date date2 = new Date(event2.getTimestamp());

		long dateDiff = 0;

		if ((dateDiff = date1.getTime() - date2.getTime()) != 0) {
			return dateDiff > 0 ? 1 : -1;
		} else {
			long timeDiff = 0;
			if ((timeDiff = (event1.getTimestamp() - event2.getTimestamp())) != 0) {
				return timeDiff > 0 ? 1 : -1;
			}

			return 0;
		}

	}
}
