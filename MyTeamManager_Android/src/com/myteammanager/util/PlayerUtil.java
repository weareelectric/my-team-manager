package com.myteammanager.util;

import java.util.ArrayList;

import android.content.Context;

import com.myteammanager.beans.PlayerBean;

public class PlayerUtil {

	public static String[] numbers;

	public synchronized static String[] getShirtNumbers() {

		if (numbers == null) {
			numbers = new String[99];
			for (int k = 1; k < 100; k++) {
				numbers[k] = "" + k;
			}
		}

		return numbers;
	}

	public static String getListOfPlayersFromList(Context context, ArrayList<PlayerBean> players, boolean withShortrole) {

		StringBuffer sb = new StringBuffer();

		for (PlayerBean player : players) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(player.getSurnameAndName(true));
			if (withShortrole) {
				sb.append(" (");
				sb.append(player.getAbbreviatedRole(context));
				sb.append(")");
			}
		}

		return sb.toString();
	}
}
