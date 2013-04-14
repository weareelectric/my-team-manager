package com.myteammanager.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class SettingsManager {

	private static Context context;

	private final static String SETTINGS_FILENAME = "myTeamManagerStorage";

	private final static String TEAMNAME_ID = "TEAM_NAME";
	private final static String POINTS_ID = "POINTS";
	private final static String PLAYED_ID = "PLAYED";
	private final static String WON_ID = "WON";
	private final static String DRAW_ID = "DRAW";
	private final static String LOST_ID = "LOST";
	private final static String GOAL_SCORED_ID = "GOAL_SCORED";
	private final static String GOAL_AGAINST_ID = "GOAL_AGAINST";
	private final static String FACEBOOK_ACTIVATION_REQUESTED = "FACEBOOK_ACTIVATION_REQUESTED";
	private final static String FACEBOOK_ACTIVATION = "FACEBOOK_ACTIVATION";
	private final static String FACEBOOK_PAGE_ID = "FACEBOOK_PAGE_ID";
	private final static String FACEBOOK_PAGE_NAME = "FACEBOOK_PAGE_NAME";

	private static SettingsManager _instance;
	private SharedPreferences m_settings;

	private SettingsManager() {
	}

	public static SettingsManager getInstance(Context context) {
		SettingsManager.context = context;
		if (_instance == null) {
			_instance = new SettingsManager();
		}
		return _instance;
	}

	private SharedPreferences getSettings() {
		if (this.m_settings == null) {
			this.m_settings = PreferenceManager.getDefaultSharedPreferences(context);

		}
		return this.m_settings;
	}

	private void saveString(String key, String value) {
		SharedPreferences.Editor editor = getSettings().edit();
		editor.putString(key, value);
		editor.commit();
	}

	private void saveInt(String key, int value) {
		SharedPreferences.Editor editor = getSettings().edit();
		editor.putInt(key, value);
		editor.commit();
	}

	private void delete(String key) {
		SharedPreferences.Editor editor = getSettings().edit();
		editor.remove(key);
		editor.commit();
	}

	private void saveBool(String key, boolean value) {
		SharedPreferences.Editor editor = getSettings().edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void setTeamName(String teamName) {
		saveString(TEAMNAME_ID, teamName);
	}

	public String getTeamName() {
		return getSettings().getString(TEAMNAME_ID, null);
	}

	public void deleteTeamname() {
		delete(TEAMNAME_ID);
	}

	public int getPoints() {
		return getSettings().getInt(POINTS_ID, 0);
	}

	public int getPlayed() {
		return getSettings().getInt(PLAYED_ID, 0);
	}

	public int getWon() {
		return getSettings().getInt(WON_ID, 0);
	}

	public int getDraw() {
		return getSettings().getInt(DRAW_ID, 0);
	}

	public int getLost() {
		return getSettings().getInt(LOST_ID, 0);
	}

	public int getAgainst() {
		return getSettings().getInt(GOAL_AGAINST_ID, 0);
	}

	public int getScored() {
		return getSettings().getInt(GOAL_SCORED_ID, 0);
	}

	public boolean isFacebookActivated() {
		return getSettings().getBoolean(FACEBOOK_ACTIVATION, false);
	}

	public boolean hasFacebookActivationBeenRequested() {
		return getSettings().getBoolean(FACEBOOK_ACTIVATION_REQUESTED, false);
	}

	public String getFacebookPageId() {
		return getSettings().getString(FACEBOOK_PAGE_ID, null);
	}

	public String getFacebookPageName() {
		return getSettings().getString(FACEBOOK_PAGE_NAME, null);
	}

	public void setPoints(int points) {
		saveInt(POINTS_ID, points);
	}

	public void setPlayed(int played) {
		saveInt(PLAYED_ID, played);
	}

	public void setWon(int won) {
		saveInt(WON_ID, won);
	}

	public void setDraw(int draw) {
		saveInt(DRAW_ID, draw);
	}

	public void setLost(int lost) {
		saveInt(LOST_ID, lost);
	}

	public void setScored(int scored) {
		saveInt(GOAL_SCORED_ID, scored);
	}

	public void setAgainst(int against) {
		saveInt(GOAL_AGAINST_ID, against);
	}

	public void setFacebookActivation(boolean value) {
		saveBool(FACEBOOK_ACTIVATION, value);
	}

	public void setFacebookActivationRequested(boolean value) {
		saveBool(FACEBOOK_ACTIVATION_REQUESTED, value);
	}

	public void setFacebookPageId(String value) {
		saveString(FACEBOOK_PAGE_ID, value);
	}

	public void setFacebookPageName(String value) {
		saveString(FACEBOOK_PAGE_NAME, value);
	}

	public void deleteAll() {
		deleteTeamname();
	}

}
