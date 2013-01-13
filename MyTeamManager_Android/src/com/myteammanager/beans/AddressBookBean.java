package com.myteammanager.beans;

import java.util.ArrayList;
import java.util.Comparator;

public class AddressBookBean {

	private String m_id;
	private String m_displayName;
	private String m_lastName;
	private String m_firstName;
	private ArrayList<String> m_emails;
	private ArrayList<String> m_phones;
	
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
	
	

}
