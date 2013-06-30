package com.myteammanager.beans;

import java.util.Comparator;

import com.myteammanager.util.DateTimeUtil;
import com.parse.ParseObject;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class ConvocationBean extends BaseBean implements Parcelable {

	public static final String TABLE = "convocations";
	
	public static String KEY_MATCH = "match";
	public static String KEY_PLAYER = "player";

	protected int m_key_id;
	protected MatchBean m_match;
	protected PlayerBean m_player;
	protected String m_parseId;

	public ConvocationBean() {
		super();
	}

	private ConvocationBean(Parcel in) {
		m_key_id = in.readInt();
		m_match = in.readParcelable(MatchBean.class.getClassLoader());
		m_player = in.readParcelable(PlayerBean.class.getClassLoader());
		m_parseId = in.readString();
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
	
	public String getParseId() {
		return m_parseId;
	}

	public void setParseId(String parseId) {
		m_parseId = parseId;
	}

	@Override
	public String getDatabaseTableName() {
		return TABLE;
	}

	@Override
	public BaseBean getEmptyNewInstance() {
		return new ConvocationBean();
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
		dest.writeParcelable(this.m_match, flags);
		dest.writeParcelable(this.m_player, flags);
		dest.writeString(this.m_parseId);
	}

	public static final Parcelable.Creator<ConvocationBean> CREATOR = new Parcelable.Creator<ConvocationBean>() {
		public ConvocationBean createFromParcel(Parcel in) {
			return new ConvocationBean(in);
		}

		public ConvocationBean[] newArray(int size) {
			return new ConvocationBean[size];
		}
	};
	
	public ParseObject getParseObject(Context context) {
		ParseObject convocationObject = new ParseObject("Convocation");
		if (m_parseId !=null) {
			convocationObject.setObjectId(m_parseId);
		}
		convocationObject.put(KEY_MATCH, getMatch().getParseObject(context));
		convocationObject.put(KEY_PLAYER, getPlayer().getParseObject(context));
		return convocationObject;
	}
}
