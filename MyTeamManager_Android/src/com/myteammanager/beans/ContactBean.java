package com.myteammanager.beans;

import java.util.ArrayList;
import java.util.Comparator;

import com.myteammanager.util.DateTimeUtil;
import com.parse.ParseObject;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A model to describe a contact load from the address book of the phone
 * @author Emanuele
 *
 */
public class ContactBean extends BaseBean implements Parcelable {

	private String m_id;
	private String m_displayName;
	private String m_lastName;
	private String m_firstName;
	private ArrayList<String> m_emails;
	private ArrayList<String> m_phones;
	private boolean m_isChosen = false;
	
	public ContactBean() {
		super();
		m_emails = new ArrayList<String>();
		m_phones = new ArrayList<String>();
	}

	private ContactBean(Parcel in) {
		this();
		m_id = in.readString();
		m_displayName = in.readString();
		m_lastName = in.readString();
		m_firstName = in.readString();
		in.readStringList(m_emails);
		in.readStringList(m_phones);
		m_isChosen = in.readInt() == 1;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(m_id);
		dest.writeString(m_displayName);
		dest.writeString(m_lastName);
		dest.writeString(m_firstName);
		dest.writeStringList(m_emails);
		dest.writeStringList(m_phones);
		dest.writeInt(m_isChosen ? 1 : 0);
	}
	
	public String getId() {
		return m_id;
	}

	public void setId(String id) {
		m_id = id;
	}

	public String getDisplayName() {
		return m_displayName;
	}

	public void setDisplayName(String displayName) {
		m_displayName = displayName;
	}

	public String getLastName() {
		return m_lastName;
	}
	
	public void setLastName(String lastName) {
		m_lastName = lastName;
	}
	
	public String getFirstName() {
		return m_firstName;
	}
	
	public void setFirstName(String firstName) {
		m_firstName = firstName;
	}

	public ArrayList<String> getEmails() {
		return m_emails;
	}
	
	public void addEmail(String email) {
		if ( m_emails == null ) {
			m_emails = new ArrayList<String>();
		}
		
		m_emails.add(email);
	}

	public ArrayList<String> getPhones() {
		return m_phones;
	}
	
	public void addPhone(String phone) {
		if ( m_phones == null ) {
			m_phones = new ArrayList<String>();
		}
		
		m_phones.add(phone);
	}
	
	public boolean isChosen() {
		return m_isChosen;
	}

	public void setChosen(boolean isChosen) {
		m_isChosen = isChosen;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Id: " + getId());
		sb.append("\n");
		sb.append("Display name: " + getDisplayName());
		sb.append("\n");
		sb.append("Lastname: " + getLastName());
		sb.append("\n");
		sb.append("Firstname: " + getFirstName());
		return sb.toString();
	}

	public static final Parcelable.Creator<ContactBean> CREATOR = new Parcelable.Creator<ContactBean>() {
		public ContactBean createFromParcel(Parcel in) {
			return new ContactBean(in);
		}

		public ContactBean[] newArray(int size) {
			return new ContactBean[size];
		}
	};
	@Override
	public String getDatabaseTableName() {
		return null;
	}

	@Override
	public BaseBean getEmptyNewInstance() {
		return new ContactBean();
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
	public ParseObject getParseObject(Context context) {
		return null;
	}
	
	

}
