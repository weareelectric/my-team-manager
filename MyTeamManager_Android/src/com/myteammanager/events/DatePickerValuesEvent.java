package com.myteammanager.events;

public class DatePickerValuesEvent {

	private int m_id = -1;
	private long m_timestamp = -1;

	public DatePickerValuesEvent() {
		super();
	}

	public DatePickerValuesEvent(int m_id, long m_timestamp) {
		super();
		this.m_id = m_id;
		this.m_timestamp = m_timestamp;
	}

	public int getId() {
		return m_id;
	}

	public long getTimestamp() {
		return m_timestamp;
	}

}
