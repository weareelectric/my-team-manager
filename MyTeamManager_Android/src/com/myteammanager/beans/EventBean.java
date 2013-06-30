package com.myteammanager.beans;

import java.util.Comparator;
import java.util.Date;

import org.json.JSONObject;

import com.myteammanager.beans.comparators.EventComparator;
import com.myteammanager.storage.SettingsManager;
import com.myteammanager.util.DateTimeUtil;
import com.myteammanager.util.StringUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import com.myteammanager.util.Log;
import com.parse.ParseObject;

public class EventBean extends BaseBean implements Parcelable {

	public static final String TABLE = "events";

	public static final int TYPE_EVENT_NO_REPEAT = 0;
	public static final int TYPE_EVENT_REPEAT_DAILY = 1;
	public static final int TYPE_EVENT_REPEAT_WEEKLY = 2;
	
	public static final String KEY_TIMESTAMP = "timestamp";
	public static final String KEY_LOCATION = "location";
	public static final String KEY_ARRIVAL_TIME = "location";
	public static final String KEY_NOTE = "note";
	public static final String KEY_CANCELED = "canceled";
	public static final String KEY_REPEAT = "repeat";
	public static final String KEY_REPEAT_END_TIME = "repeat_end_time";
	public static final String KEY_PARENT_EVENT = "parent_event";
	public static final String KEY_TEAM_FOR_EVENT = "team";

	private int m_key_id;
	private long m_timestamp;
	private String m_location;
	private long m_arrivalTime;
	private String m_note;
	private int m_canceled;
	private int m_repeat;
	private Date m_repeatEndDate;
	private EventBean m_parentEvent;
	private String m_parseId;

	public EventBean() {
		super();
	}

	private EventBean(Parcel in) {
		m_key_id = in.readInt();
		m_timestamp = in.readLong();
		m_location = in.readString();
		m_arrivalTime = in.readLong();
		m_note = in.readString();
		m_canceled = in.readInt();
		m_repeat = in.readInt();
		m_repeatEndDate = DateTimeUtil.getDateFromLong(in.readLong());
		m_parentEvent = in.readParcelable(EventBean.class.getClassLoader());
		m_parseId = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.m_key_id);
		dest.writeLong(getTimestamp());
		dest.writeString(getLocation());
		dest.writeLong(getArrivalTime());
		dest.writeString(getNote());
		dest.writeInt(m_canceled);
		dest.writeInt(m_repeat);
		dest.writeLong(getRepeatEndDateLong());
		dest.writeParcelable(m_parentEvent, flags);
		dest.writeString(m_parseId);
	}

	public int getId() {
		return m_key_id;
	}

	public void setId(int m_id) {
		this.m_key_id = m_id;
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

	public long getArrivalTime() {
		return m_arrivalTime;
	}

	public void setArrivalTime(long m_arrivalTime) {
		this.m_arrivalTime = m_arrivalTime;
	}

	public String getNote() {
		return m_note;
	}

	public void setNote(String m_note) {
		this.m_note = m_note;
	}

	public int getCanceled() {
		return m_canceled;
	}

	public void setCanceled(int m_canceled) {
		this.m_canceled = m_canceled;
	}

	public int getRepeat() {
		return m_repeat;
	}

	public void setRepeat(int m_repeat) {
		this.m_repeat = m_repeat;
	}

	public Date getRepeatEndDate() {
		return m_repeatEndDate;
	}

	public long getRepeatEndDateLong() {
		return DateTimeUtil.getDateLong(getRepeatEndDate());
	}

	public void setRepeatEndDate(Date m_repeatEndDate) {
		this.m_repeatEndDate = m_repeatEndDate;
	}

	public EventBean getParentEvent() {
		return m_parentEvent;
	}

	public void setParentEvent(EventBean m_parentEvent) {
		this.m_parentEvent = m_parentEvent;
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

		m_timestamp = -1;
		m_location = null;
		m_arrivalTime = -1;
		m_note = null;
		m_canceled = 0;
		m_repeat = 0;
		m_repeatEndDate = null;
		m_parentEvent = null;
		m_parseId = null;
	}

	public static final Parcelable.Creator<EventBean> CREATOR = new Parcelable.Creator<EventBean>() {
		public EventBean createFromParcel(Parcel in) {
			return new EventBean(in);
		}

		public EventBean[] newArray(int size) {
			return new EventBean[size];
		}
	};

	@Override
	public String getDatabaseTableName() {
		return TABLE;
	}

	@Override
	public BaseBean getEmptyNewInstance() {
		return new EventBean();
	}

	@Override
	public String orderByRule() {
		return "timestamp ASC";
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof EventBean) {
			EventBean event = (EventBean) o;
			return event.m_key_id == m_key_id;
		} else
			return false;
	}

	@Override
	public Comparator getComparator() {
		return new EventComparator();
	}

	public boolean isEventLinkedToOthers() {
		return getRepeat() != TYPE_EVENT_NO_REPEAT || getParentEvent() != null;
	}
	
	
	public ParseObject getParseObject(Context context) {
		ParseObject eventObject = new ParseObject("Event");
		if (m_parseId !=null) {
			eventObject.setObjectId(m_parseId);
		}
		eventObject.put(KEY_ARRIVAL_TIME, getArrivalTime());
		eventObject.put(KEY_CANCELED, getCanceled());
		eventObject.put(KEY_LOCATION, StringUtil.getValue(getLocation()));
		eventObject.put(KEY_NOTE, StringUtil.getValue(getNote()));
		if ( getParentEvent() != null ) {
			eventObject.put(KEY_PARENT_EVENT, getParentEvent().getParseObject(context));
		}
		else {
			eventObject.put(KEY_PARENT_EVENT, JSONObject.NULL);
		}
		
		String userTeamName = SettingsManager.getInstance(context).getTeamName();
		String userTeamParseId = SettingsManager.getInstance(context).getTeamParseId();
		
		eventObject.put(KEY_TEAM_FOR_EVENT,  TeamBean.getParseObjectFor(userTeamParseId, userTeamName));
		
		eventObject.put(KEY_REPEAT, getRepeat());
		eventObject.put(KEY_REPEAT_END_TIME, getRepeatEndDateLong());
		return eventObject;
	}
	

}
