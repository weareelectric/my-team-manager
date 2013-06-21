package com.myteammanager.beans;

import java.util.Comparator;
import java.util.Date;

import com.myteammanager.beans.comparators.MatchComparator;
import com.myteammanager.storage.SettingsManager;
import com.myteammanager.util.DateTimeUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import com.myteammanager.util.Log;
import com.parse.ParseObject;

public class MatchBean extends BaseBean implements Parcelable {

	public static final String TABLE = "matches";

	public static final int TYPE_HOME = 0;
	public static final int TYPE_AWAY = 1;
	
	public String KEY_TEAM1 = "team1";
	public String KEY_TEAM2 = "team2";
	public String KEY_TIMESTAMP = "timestamp";
	public String KEY_LOCATION = "location";
	public String KEY_NOTE = "note";
	public String KEY_HOMEAWAYTYPE = "homeawaytype";
	public String KEY_CANCELED = "canceled";
	public String KEY_NUMBER_PLAYER_CONVOCATED = "numconvocated";
	public String KEY_LINEUP_CONFIGURED = "lineup_configured";
	public String KEY_RESULT_ENTERED = "result_entered";
	public String KEY_GOALHOME = "goalhome";
	public String KEY_GOALAWAY = "goalaway";
	public String KEY_APPOINTEMENTPLACEANDTIME = "appointementplaceandtime";

	private int m_key_id;
	private TeamBean m_team1;
	private TeamBean m_team2;
	private long m_timestamp;
	private String m_location;
	private String m_note;
	private int m_homeAwayType;
	private int m_canceled;
	private int m_numberOfPlayerConvocated;
	private int m_lineupConfigured = 0;
	private int m_resultEntered = 0;
	private int m_goalHome = -1;
	private int m_goalAway = -1;
	private String m_appointmentPlaceAndTime;
	private String m_parseId;

	public MatchBean() {
		super();
	}

	private MatchBean(Parcel in) {
		m_key_id = in.readInt();
		m_team1 = in.readParcelable(TeamBean.class.getClassLoader());
		m_team2 = in.readParcelable(TeamBean.class.getClassLoader());
		m_timestamp = in.readLong();
		m_location = in.readString();
		m_note = in.readString();
		m_homeAwayType = in.readInt();
		m_canceled = in.readInt();
		m_numberOfPlayerConvocated = in.readInt();
		m_lineupConfigured = in.readInt();
		m_resultEntered = in.readInt();
		m_goalHome = in.readInt();
		m_goalAway = in.readInt();
		m_appointmentPlaceAndTime = in.readString();
		m_parseId = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.m_key_id);
		dest.writeParcelable(m_team1, flags);
		dest.writeParcelable(m_team2, flags);
		dest.writeLong(getTimestamp());
		dest.writeString(getLocation());
		dest.writeString(getNote());
		dest.writeInt(getHomeAwayType());
		dest.writeInt(m_canceled);
		dest.writeInt(m_numberOfPlayerConvocated);
		dest.writeInt(m_lineupConfigured);
		dest.writeInt(m_resultEntered);
		dest.writeInt(m_goalHome);
		dest.writeInt(m_goalAway);
		dest.writeString(m_appointmentPlaceAndTime);
		dest.writeString(m_parseId);
	}

	public int getId() {
		return m_key_id;
	}

	public void setId(int m_id) {
		this.m_key_id = m_id;
	}

	public TeamBean getTeam1() {
		return m_team1;
	}

	public String getTeam1StringToShow(Context context) {
		return m_team1 != null ? m_team1.getName() : SettingsManager.getInstance(context).getTeamName();
	}

	public void setTeam1(TeamBean m_team1) {
		this.m_team1 = m_team1;
	}

	public TeamBean getTeam2() {
		return m_team2;
	}

	public String getTeam2StringToShow(Context context) {
		return m_team2 != null ? m_team2.getName() : SettingsManager.getInstance(context).getTeamName();
	}

	public void setTeam2(TeamBean m_team2) {
		this.m_team2 = m_team2;
	}

	public String getOpponentTeam() {
		return m_team2 != null ? m_team2.getName() : m_team1.getName();
	}

	public long getTimestamp() {
		return m_timestamp;
	}

	public void setTimestamp(long m_timestamp) {
		this.m_timestamp = m_timestamp;
	}

	public String getLocation() {
		return m_location;
	}

	public void setLocation(String m_location) {
		this.m_location = m_location;
	}

	public String getNote() {
		return m_note;
	}

	public void setNote(String m_note) {
		this.m_note = m_note;
	}

	public int getHomeAwayType() {
		return m_homeAwayType;
	}

	public void setHomeAwayType(int home_away_type) {
		this.m_homeAwayType = home_away_type;
	}

	public int getCanceled() {
		return m_canceled;
	}

	public void setCanceled(int m_canceled) {
		this.m_canceled = m_canceled;
	}

	public int getNumberOfPlayerConvocated() {
		return m_numberOfPlayerConvocated;
	}

	public void setNumberOfPlayerConvocated(int m_numberOfPlayerConvocated) {
		this.m_numberOfPlayerConvocated = m_numberOfPlayerConvocated;
	}

	public int getLineupConfigured() {
		return m_lineupConfigured;
	}

	public void setLineupConfigured(int m_lineupConfigured) {
		this.m_lineupConfigured = m_lineupConfigured;
	}

	public int getResultEntered() {
		return m_resultEntered;
	}

	public void setResultEntered(int m_resultEntered) {
		this.m_resultEntered = m_resultEntered;
	}

	public int getGoalHome() {
		return m_goalHome;
	}

	public void setGoalHome(int m_goalHome) {
		this.m_goalHome = m_goalHome;
	}

	public int getGoalAway() {
		return m_goalAway;
	}

	public void setGoalAway(int m_goalAway) {
		this.m_goalAway = m_goalAway;
	}

	public String getAppointmentPlaceAndTime() {
		return m_appointmentPlaceAndTime;
	}

	public void setAppointmentPlaceAndTime(String m_appointmentPlaceAndTime) {
		this.m_appointmentPlaceAndTime = m_appointmentPlaceAndTime;
	}
	
	public String getParseId() {
		return m_parseId;
	}

	public void setParseId(String parseId) {
		m_parseId = parseId;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public void reset() {
		this.m_key_id = -1;

		m_team1 = null;
		m_team2 = null;
		m_timestamp = -1;
		m_location = null;
		m_note = null;
		m_homeAwayType = -1;
		m_canceled = 0;
		m_numberOfPlayerConvocated = 0;
		m_lineupConfigured = 0;
		m_resultEntered = 0;
		m_goalHome = 0;
		m_goalAway = 0;
		m_appointmentPlaceAndTime = null;
		m_parseId = null;
	}

	public static final Parcelable.Creator<MatchBean> CREATOR = new Parcelable.Creator<MatchBean>() {
		public MatchBean createFromParcel(Parcel in) {
			return new MatchBean(in);
		}

		public MatchBean[] newArray(int size) {
			return new MatchBean[size];
		}
	};

	@Override
	public String getDatabaseTableName() {
		return TABLE;
	}

	@Override
	public BaseBean getEmptyNewInstance() {
		return new MatchBean();
	}

	@Override
	public String orderByRule() {
		return "timestamp ASC, id ASC";
	}

	@Override
	public Comparator getComparator() {
		return new MatchComparator();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof MatchBean) {
			MatchBean match = (MatchBean) o;
			return match.m_key_id == m_key_id;
		} else
			return false;
	}

	public String getMatchString(Context context) {
		return getTeam1StringToShow(context) + " - " + getTeam2StringToShow(context);
	}

	public String getMatchResult() {
		if ( getGoalHome() >= 0 && getGoalAway() >= 0 ) {
			return getGoalHome() + " - " + getGoalAway();
		}
		else {
			return "";
		}
		
	}
	
	public ParseObject getMatchParseObject() {
		ParseObject matchObj = new ParseObject("Match");
		if (m_parseId !=null) {
			matchObj.setObjectId(m_parseId);
		}
		matchObj.put(KEY_APPOINTEMENTPLACEANDTIME, getAppointmentPlaceAndTime());
		matchObj.put(KEY_CANCELED, getCanceled());
		matchObj.put(KEY_GOALAWAY, getGoalAway());
		matchObj.put(KEY_GOALHOME, getGoalHome());
		matchObj.put(KEY_HOMEAWAYTYPE, getHomeAwayType());
		matchObj.put(KEY_LINEUP_CONFIGURED, getLineupConfigured());
		matchObj.put(KEY_LOCATION, getLocation());
		matchObj.put(KEY_NOTE, getNote());
		matchObj.put(KEY_NUMBER_PLAYER_CONVOCATED, getNumberOfPlayerConvocated());
		matchObj.put(KEY_RESULT_ENTERED, getResultEntered());
		if ( getTeam1() != null )
			matchObj.put(KEY_TEAM1, getTeam1());
		else 
			matchObj.put(KEY_TEAM1, null);
		if ( getTeam2() != null )
			matchObj.put(KEY_TEAM2, getTeam2());
		else 
			matchObj.put(KEY_TEAM2, null);
		matchObj.put(KEY_TIMESTAMP, getTimestamp());
		
		return matchObj;
	}

	public int[] getChangeInStatsForDelete() {

		int[] varations = null;
		int[] empty = new int[] { 0, 0, 0, 0, 0, 0, 0 };

		boolean userTeamAtHome = (m_team1 == null);

		if (m_resultEntered == 0) {
			return empty;
		} else {
			if (userTeamAtHome) {
				if (m_goalHome > m_goalAway) {
					varations = new int[] { -3, -1, -1, 0, 0, -m_goalHome, -m_goalAway };
				} else if (m_goalHome == m_goalAway) {
					varations = new int[] { -1, -1, 0, -1, 0, -m_goalHome, -m_goalAway };
				} else {
					varations = new int[] { 0, -1, 0, 0, -1, -m_goalHome, -m_goalAway };
				}
			} else {
				if (m_goalHome > m_goalAway) {
					varations = new int[] { 0, -1, 0, 0, -1, -m_goalAway, -m_goalHome };
				} else if (m_goalHome == m_goalAway) {
					varations = new int[] { -1, -1, 0, -1, 0, -m_goalAway, -m_goalHome };
				} else {
					varations = new int[] { -3, -1, -1, 0, 0, -m_goalAway, -m_goalHome };
				}
			}

			return varations;

		}
	}

	/**
	 * To be called before setting the new value for goal home and away.
	 * 
	 * @param goalHome
	 * @param goalAway
	 * @return array containing the variations in the stats numbers for the user's team. Each position of the array has a
	 * specific meaning. 0: points, 1: played, 2: won, 3: null, 4: lost, 5: goal scored; 6: goal against
	 */
	public int[] getChangeInStatsAfterNewResult(int goalHome, int goalAway) {
		int[] variations = null;
		int[] actual = null;
		int[] newValues = null;
		int[] empty = new int[] { 0, 0, 0, 0, 0, 0, 0 };

		if (m_goalHome == goalHome && m_goalAway == goalAway) {
			return empty;
		}

		boolean userTeamAtHome = (m_team1 == null);
		boolean newResultIsValid = (goalHome != -1 && goalAway != -1);

		if (userTeamAtHome) {

			if (m_resultEntered == 1) {
				if (m_goalHome > m_goalAway) {
					actual = new int[] { 3, 1, 1, 0, 0, m_goalHome, m_goalAway };
				} else if (m_goalHome == m_goalAway) {
					actual = new int[] { 1, 1, 0, 1, 0, m_goalHome, m_goalAway };
				} else {
					actual = new int[] { 0, 1, 0, 0, 1, m_goalHome, m_goalAway };
				}
			} else {
				actual = empty;
			}

			if (!newResultIsValid) {
				newValues = empty;
			} else {
				if (goalHome > goalAway) {
					newValues = new int[] { 3, 1, 1, 0, 0, goalHome, goalAway };
				} else if (goalHome == goalAway) {
					newValues = new int[] { 1, 1, 0, 1, 0, goalHome, goalAway };
				} else {
					newValues = new int[] { 0, 1, 0, 0, 1, goalHome, goalAway };
				}
			}

		} else {

			if (m_resultEntered == 1) {
				if (m_goalHome > m_goalAway) {
					actual = new int[] { 0, 1, 0, 0, 1, m_goalAway, m_goalHome };
				} else if (m_goalHome == m_goalAway) {
					actual = new int[] { 1, 1, 0, 1, 0, m_goalAway, m_goalHome };
				} else {
					actual = new int[] { 3, 1, 1, 0, 0, m_goalAway, m_goalHome };
				}
			} else {
				actual = empty;
			}

			if (!newResultIsValid) {
				newValues = empty;
			} else {
				if (goalHome > goalAway) {
					newValues = new int[] { 0, 1, 0, 0, 1, goalAway, goalHome };
				} else if (goalHome == goalAway) {
					newValues = new int[] { 1, 1, 0, 1, 0, goalAway, goalHome };
				} else {
					newValues = new int[] { 3, 1, 1, 0, 0, goalAway, goalHome };
				}
			}

		}

		variations = new int[] { newValues[0] - actual[0], newValues[1] - actual[1], newValues[2] - actual[2],
				newValues[3] - actual[3], newValues[4] - actual[4], newValues[5] - actual[5], newValues[6] - actual[6] };
		return variations;

	}
}
