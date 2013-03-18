package com.myteammanager.beans;

import java.util.Comparator;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import com.myteammanager.R;
import com.myteammanager.beans.comparators.RosterComparator;
import com.myteammanager.util.DateTimeUtil;
import com.myteammanager.util.StringUtil;

public class PlayerBean extends BaseBean implements Parcelable {

	public static final String TABLE = "players";

	public static final int ROLE_GK = 0;
	public static final int ROLE_DEF = 1;
	public static final int ROLE_MID = 2;
	public static final int ROLE_ATT = 3;

	private int m_key_id;
	private String m_name;
	private String m_lastName;
	private int m_role;
	private Date m_birthDate;
	private String m_email;
	private String m_phone;
	private int m_shirtNumber = -1;
	private int m_gamePlayed = 0;
	private int m_goalScored = 0;

	private boolean _convocated = false;
	private boolean _onTheBench = false;
	private PlayerBean _replacedPlayer;
	private int _goalScoredInTheMatch = 0;
	private boolean _isRecipient = false;
	private boolean _isFakeSelectAll = false;

	public PlayerBean() {
		super();
	}

	private PlayerBean(Parcel in) {
		m_key_id = in.readInt();
		m_name = in.readString();
		m_lastName = in.readString();
		m_role = in.readInt();
		m_birthDate = DateTimeUtil.getDateFromLong(in.readLong());
		m_phone = in.readString();
		m_email = in.readString();
		m_shirtNumber = in.readInt();
		m_gamePlayed = in.readInt();
		m_goalScored = in.readInt();

		_convocated = in.readInt() == 1;
		_onTheBench = in.readInt() == 1;
		_replacedPlayer = in.readParcelable(PlayerBean.class.getClassLoader());
		_goalScoredInTheMatch = in.readInt();
		_isRecipient = in.readInt() == 1;
		_isFakeSelectAll = in.readInt() == 1;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.m_key_id);
		dest.writeString(getName());
		dest.writeString(getLastName());
		dest.writeInt(this.m_role);
		dest.writeLong(getBirthDateLong());
		dest.writeString(m_phone);
		dest.writeString(m_email);
		dest.writeInt(m_shirtNumber);
		dest.writeInt(m_gamePlayed);
		dest.writeInt(m_goalScored);
		dest.writeInt(_convocated ? 1 : 0);
		dest.writeInt(_onTheBench ? 1 : 0);
		dest.writeParcelable(_replacedPlayer, flags);
		dest.writeInt(_goalScoredInTheMatch);
		dest.writeInt(_isRecipient ? 1 : 0);
		dest.writeInt(_isFakeSelectAll ? 1 : 0);
	}

	public int getId() {
		return m_key_id;
	}

	public void setId(int m_id) {
		this.m_key_id = m_id;
	}

	public String getName() {
		if (m_name == null) {
			return "";
		}
		return m_name;
	}

	public void setName(String m_name) {
		this.m_name = m_name;
	}

	public String getLastName() {
		return m_lastName;
	}

	public void setLastName(String m_lastName) {
		this.m_lastName = m_lastName;
	}

	public int getRole() {
		return m_role;
	}

	public void setRole(int m_role) {
		this.m_role = m_role;
	}

	public Date getBirthDate() {
		return m_birthDate;
	}

	public long getBirthDateLong() {
		return DateTimeUtil.getDateLong(m_birthDate);
	}

	public void setBirthDate(Date m_date) {
		this.m_birthDate = m_date;
	}

	public String getEmail() {
		if (m_email == null) {
			return "";
		}
		return m_email;
	}

	public void setEmail(String m_email) {
		this.m_email = m_email;
	}

	public String getPhone() {
		if (m_phone == null) {
			return "";
		}
		return m_phone;
	}

	public void setPhone(String m_phone) {
		this.m_phone = m_phone;
	}

	public int getShirtNumber() {
		return m_shirtNumber;
	}

	public void setShirtNumber(int m_shirtNumber) {
		this.m_shirtNumber = m_shirtNumber;
	}

	public boolean isConvocated() {
		return _convocated;
	}

	public void setConvocated(boolean _convocated) {
		this._convocated = _convocated;
	}

	public boolean isOnTheBench() {
		return _onTheBench;
	}

	public void setOnTheBench(boolean _onTheBench) {
		this._onTheBench = _onTheBench;
	}

	public int getGamePlayed() {
		return m_gamePlayed;
	}

	public void setGamePlayed(int m_gamePlayed) {
		this.m_gamePlayed = m_gamePlayed;
	}

	public int getGoalScored() {
		return m_goalScored;
	}

	public void setGoalScored(int m_goalScored) {
		this.m_goalScored = m_goalScored;
	}

	
	public PlayerBean getReplacedPlayer() {
		return _replacedPlayer;
	}

	public void setReplacedPlayer(PlayerBean _replacedPlayer) {
		this._replacedPlayer = _replacedPlayer;
	}
	
	public int getGoalScoredInTheMatch() {
		return _goalScoredInTheMatch;
	}

	public void setGoalScoredInTheMatch(int _goalScoredInTheMatch) {
		this._goalScoredInTheMatch = _goalScoredInTheMatch;
	}
	
	public boolean isRecipient() {
		return _isRecipient;
	}

	public void setIsRecipient(boolean _isRecipient) {
		this._isRecipient = _isRecipient;
	}
	
	public boolean isFakeSelectAll() {
		return _isFakeSelectAll;
	}

	public void setIsFakeSelectAll(boolean _isFakeSelectAll) {
		this._isFakeSelectAll = _isFakeSelectAll;
	}

	public void reset() {
		this.m_key_id = -1;
		this.m_name = null;
		this.m_lastName = null;
		this.m_role = -1;
		this.m_birthDate = null;
		this.m_phone = null;
		this.m_email = null;
		this.m_shirtNumber = -1;
		this.m_gamePlayed = 0;
		this.m_goalScored = 0;
		this._convocated = false;
		this._onTheBench = false;
		this._replacedPlayer = null;
		this._goalScoredInTheMatch = 0;
		this._isRecipient = false;
		this._isFakeSelectAll = false;
	}

	public String getSurnameAndName(boolean nameCut) {
		StringBuffer surnameAndName = new StringBuffer();
		surnameAndName.append(this.m_lastName);
		if (StringUtil.isNotEmpty(this.m_name)) {
			surnameAndName.append(" ");
			if (nameCut) {
				surnameAndName.append(this.m_name.substring(0, 1));
				surnameAndName.append(".");
			} else {
				surnameAndName.append(this.m_name);
			}

		}
		return surnameAndName.toString();
	}

	public String getAbbreviatedRole(Context context) {
		Resources res = context.getResources();
		switch (m_role) {
		case ROLE_GK:
			return res.getString(R.string.abbr_gk);

		case ROLE_DEF:
			return res.getString(R.string.abbr_def);

		case ROLE_MID:
			return res.getString(R.string.abbr_mid);

		case ROLE_ATT:
			return res.getString(R.string.abbr_att);
		}

		return null;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<PlayerBean> CREATOR = new Parcelable.Creator<PlayerBean>() {
		public PlayerBean createFromParcel(Parcel in) {
			return new PlayerBean(in);
		}

		public PlayerBean[] newArray(int size) {
			return new PlayerBean[size];
		}
	};

	@Override
	public String getDatabaseTableName() {
		return TABLE;
	}

	@Override
	public BaseBean getEmptyNewInstance() {
		return new PlayerBean();
	}

	@Override
	public String orderByRule() {
		return "role" + " ASC, " + "lastName" + " ASC, " + "name" + " ASC";
	}

	@Override
	public Comparator getComparator() {
		return new RosterComparator();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PlayerBean) {
			PlayerBean player = (PlayerBean) o;
			return player.m_key_id == m_key_id;
		} else
			return false;
	}

}
