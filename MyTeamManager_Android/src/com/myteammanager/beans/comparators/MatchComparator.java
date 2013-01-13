package com.myteammanager.beans.comparators;

import java.util.Comparator;
import java.util.Date;

import com.myteammanager.beans.MatchBean;

public class MatchComparator implements Comparator<MatchBean> {

	@Override
	public int compare(MatchBean match1, MatchBean match2) {
		Date date1 = new Date(match1.getTimestamp());
		Date date2 = new Date(match2.getTimestamp());

		if (date1 == null && date2 == null) {
			return match1.getId() > match2.getId() ? 1 : -1;
		} else if (date1 == null) {
			return -1;
		} else if (date2 == null) {
			return 1;
		}

		long dateDiff = 0;

		if ((dateDiff = date1.getTime() - date2.getTime()) != 0) {
			return dateDiff > 0 ? 1 : -1;
		} else {
			long timeDiff = 0;
			if ((timeDiff = (match1.getTimestamp() - match2.getTimestamp())) != 0) {
				return timeDiff > 0 ? 1 : -1;
			}

			return 0;
		}

	}

}
