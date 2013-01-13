package com.myteammanager.util;

import android.content.Context;

import com.myteammanager.MyTeamManagerActivity;
import com.myteammanager.beans.MatchBean;
import com.myteammanager.events.ResultEnteredEvent;
import com.myteammanager.storage.DBManager;

public class DeleteMatchManager {

	public static void deleteMatch(MatchBean matchBean, Context context) {
		int[] variations = matchBean.getChangeInStatsForDelete();

		DBManager.getInstance().deleteBean(matchBean);

		ResultEnteredEvent event = new ResultEnteredEvent(matchBean, false, matchBean.getGoalHome(),
				matchBean.getGoalAway());
		event.setVariationsForStats(variations);
		MyTeamManagerActivity.getBus().post(event);
	}
}
