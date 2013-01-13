package com.myteammanager.beans;

import java.util.Comparator;

import android.os.Parcel;
import android.os.Parcelable;

public class ScorerBean extends BaseBean implements Parcelable {

	public static final String TABLE = "scorers";

	protected int m_key_id;
	private MatchBean m_match;
	private PlayerBean m_player;
	private int m_scoredGoal = 0;

	public ScorerBean() {
		super();
	}

	private ScorerBean(Parcel in) {
		m_key_id = in.readInt();
		m_match = in.readParcelable(MatchBean.class.getClassLoader());
		m_player = in.readParcelable(PlayerBean.class.getClassLoader());
		m_scoredGoal = in.readInt();
	}

	public int getId() {
		return m_key_id;
	}

	public void setId(int m_key_id) {
		this.m_key_id = m_key_id;
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

	public int getScoredGoal() {
		return m_scoredGoal;
	}

	public void setScoredGoal(int m_scoredGoal) {
		this.m_scoredGoal = m_scoredGoal;
	}

	@Override
	public String getDatabaseTableName() {
		return TABLE;
	}

	@Override
	public BaseBean getEmptyNewInstance() {
		return new ScorerBean();
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

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.m_key_id);
		dest.writeParcelable(m_match, flags);
		dest.writeParcelable(m_player, flags);
		dest.writeInt(m_scoredGoal);
	}

	public static final Parcelable.Creator<ScorerBean> CREATOR = new Parcelable.Creator<ScorerBean>() {
		public ScorerBean createFromParcel(Parcel in) {
			return new ScorerBean(in);
		}

		public ScorerBean[] newArray(int size) {
			return new ScorerBean[size];
		}
	};

}
