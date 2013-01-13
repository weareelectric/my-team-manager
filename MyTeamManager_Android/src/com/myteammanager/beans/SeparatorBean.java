package com.myteammanager.beans;

import java.util.Comparator;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class SeparatorBean extends BaseBean implements Parcelable {

	private String m_separatorString;
	
	public SeparatorBean(String m_separatorString) {
		super();
		this.m_separatorString = m_separatorString;
	}
	
	public SeparatorBean(Parcel in) {
		m_separatorString = in.readString();
	}

	public String getSeparatorString() {
		return m_separatorString;
	}

	public void setSeparatorString(String m_separatorString) {
		this.m_separatorString = m_separatorString;
	}

	@Override
	public String getDatabaseTableName() {
		return null;
	}

	@Override
	public BaseBean getEmptyNewInstance() {
		return null;
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
		dest.writeString(m_separatorString);
		
	}
	
	public static final Parcelable.Creator<SeparatorBean> CREATOR = new Parcelable.Creator<SeparatorBean>() {
		public SeparatorBean createFromParcel(Parcel in) {
			return new SeparatorBean(in);
		}

		public SeparatorBean[] newArray(int size) {
			return new SeparatorBean[size];
		}
	};
}
