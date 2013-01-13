package com.myteammanager.beans;

import java.util.Comparator;

import android.os.Parcel;
import android.os.Parcelable;

public class LineupBean extends BaseBean implements Parcelable {

	public static final String TABLE = "lineups";

	public static final int NOT_ON_THE_BENCH = 0;
	public static final int ON_THE_BENCH = 1;

	protected int m_key_id;
	protected MatchBean m_match;
	protected PlayerBean m_player;
	protected int m_idOfCorrespondentView = -1;
	protected int m_onTheBench = NOT_ON_THE_BENCH;

	public LineupBean() {
		super();
	}

	private LineupBean(Parcel in) {
		m_key_id = in.readInt();
		m_match = in.readParcelable(MatchBean.class.getClassLoader());
		m_player = in.readParcelable(PlayerBean.class.getClassLoader());
		m_idOfCorrespondentView = in.readInt();
		m_onTheBench = in.readInt();
	}

	public int getId() {
		return m_key_id;
	}

	public void setId(int m_id) {
		this.m_key_id = m_id;
	}

	public MatchBean getMatch() {
		return m_match;
	}

	public void setMatch(MatchBean m_match) {
		this.m_match = m_match;
	}

	public PlayerBean getPlayer() {
		return m_player;
	}

	public void setPlayer(PlayerBean m_player) {
		this.m_player = m_player;
	}

	public int getIdOfCorrespondentView() {
		return m_idOfCorrespondentView;
	}

	public void setIdOfCorrespondentView(int idOfCorrespondentView) {
		this.m_idOfCorrespondentView = idOfCorrespondentView;
	}

	public int getOnTheBench() {
		return m_onTheBench;
	}

	public void setOnTheBench(int m_onTheBench) {
		this.m_onTheBench = m_onTheBench;
	}

	@Override
	public BaseBean getEmptyNewInstance() {
		return new LineupBean();
	}

	@Override
	public String orderByRule() {
		return null;
	}

	@Override
	public Comparator getComparator() {
		return null;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<LineupBean> CREATOR = new Parcelable.Creator<LineupBean>() {
		public LineupBean createFromParcel(Parcel in) {
			return new LineupBean(in);
		}

		public LineupBean[] newArray(int size) {
			return new LineupBean[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.m_key_id);
		dest.writeParcelable(this.m_match, flags);
		dest.writeParcelable(this.m_player, flags);
		dest.writeInt(this.m_idOfCorrespondentView);
		dest.writeInt(m_onTheBench);
	}

	@Override
	public String getDatabaseTableName() {
		return TABLE;
	}
	
	

}
