package com.myteammanager.util;

import android.content.Context;
import android.content.res.Resources;

import com.myteammanager.R;
import com.myteammanager.beans.PlayerBean;

public class PlayerAndroidUtil {

	private static String[] tshirt_numbers;

	public synchronized static String[] getTShirtNumbers(Context context) {
		if (tshirt_numbers == null) {
			tshirt_numbers = new String[100];
			for (int k = 0; k < 100; k++) {
				if (k == 0) {
					tshirt_numbers[0] = context.getResources().getString(R.string.label_shirt_number);
				} else {
					tshirt_numbers[k] = "" + k;
				}

			}
		}
		return tshirt_numbers;
	}

	public static String getRoleString(Context context, int role) {
		Resources res = context.getResources();
		String[] roles = res.getStringArray(R.array.positions);
		return roles[role];
	}

	public static String toString(Context context, PlayerBean player) {
		StringBuffer playerString = new StringBuffer();
		playerString.append("[PLAYER name:");
		playerString.append(" [m_id: " + player.getId());

		if (player.getName() != null)
			playerString.append("] [name: " + player.getName());

		playerString.append("] [m_lastName: " + player.getLastName());
		playerString.append("] [m_role: " + getRoleString(context, player.getRole()));
		playerString.append("] [m_shirtNumber: " + player.getShirtNumber());
		playerString.append("] [m_phone: " + player.getPhone());
		playerString.append("] [m_email: " + player.getEmail());

		if (player.getBirthDate() != null)
			playerString.append("] [m_birthDate: " + player.getBirthDate().toString());

		playerString.append("]]");
		return playerString.toString();
	}

}
