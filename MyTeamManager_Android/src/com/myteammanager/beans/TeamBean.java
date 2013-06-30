 package com.myteammanager.beans;

import java.util.Comparator;

import com.parse.ParseObject;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;


public class TeamBean extends BaseBean implements Parcelable {
	
	public static final String TABLE = "teams";
	
	private int m_key_id;
	private String m_unique_name;
	private String m_parseId;

	// ParseUser TeamBean
	public static final String FIELD_TEAMBEAN_NAME = "name";
	
	public TeamBean() {
		super();
	}
	
	public TeamBean(int m_id, String m_name) {
		super();
		this.m_key_id = m_id;
		this.m_unique_name = m_name;
	}
	
	public TeamBean(String parseId, String m_name) {
		super();
		this.m_parseId = parseId;
		this.m_unique_name = m_name;
	}
	
	private TeamBean(Parcel in) {
		m_key_id = in.readInt();
		m_unique_name = in.readString();
		m_parseId = in.readString();
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
	public String getParseId() {
		return super.getParseId();
	}

	@Override
	public void setParseId(String parseId) {
		super.setParseId(parseId);
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
		dest.writeString(m_parseId);
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
		return null;
	}
	
	public ParseObject getParseObject(Context context) {
		ParseObject teamObj = new ParseObject("UserTeam");
		teamObj.put(TeamBean.FIELD_TEAMBEAN_NAME, getName());
		
		if ( m_parseId != null ) {
			teamObj.setObjectId(m_parseId);
		}
		
		return teamObj;
	}
	
	public static ParseObject getParseObjectFor(String parseId, String teamName) {
		TeamBean team = new TeamBean(parseId, teamName);
		return team.getParseObject(null);
	}
	
	public static TeamBean getTeamBeanFor(ParseObject object) {
		TeamBean team = new TeamBean(-1, object.getString(TeamBean.FIELD_TEAMBEAN_NAME));
		return team;
	}

}
