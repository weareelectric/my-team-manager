 package com.myteammanager.beans;

import java.util.Comparator;

import com.parse.ParseObject;

import android.os.Parcel;
import android.os.Parcelable;


public class TeamBean extends BaseBean implements Parcelable {
	
	public static final String TABLE = "teams";
	
	public static final String TEAM_NAME = "TEAM_NAME";

	private int m_key_id;
	private String m_unique_name;
	
	public TeamBean() {
		super();
	}
	
	public TeamBean(int m_id, String m_name) {
		super();
		this.m_key_id = m_id;
		this.m_unique_name = m_name;
	}
	
	private TeamBean(Parcel in) {
		m_key_id = in.readInt();
		m_unique_name = in.readString();
	}

	public int getId() {
		return m_key_id;
	}

	public void setId(int m_id) {
		this.m_key_id = m_id;
	}

	public String getName() {
		return m_unique_name;
	}

	public void setName(String m_name) {
		this.m_unique_name = m_name;
	}


	@Override
	public String getDatabaseTableName() {
		return TABLE;
	}

	@Override
	public BaseBean getEmptyNewInstance() {
		return new TeamBean();
	}
	@Override
	
	public String orderByRule() {
		return null;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.m_key_id);
		dest.writeString(getName());
	}
	
	public static final Parcelable.Creator<TeamBean> CREATOR = new Parcelable.Creator<TeamBean>() {
		public TeamBean createFromParcel(Parcel in) {
			return new TeamBean(in);
		}

		public TeamBean[] newArray(int size) {
			return new TeamBean[size];
		}
	};

	@Override
	public Comparator getComparator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ParseObject getMyTeamParseObject() {
		ParseObject teamObj = new ParseObject("UserTeam");
		teamObj.put("name", getName());
		return teamObj;
	}

}
