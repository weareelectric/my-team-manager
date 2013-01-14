package com.myteammanager.adapter.holders;

import com.myteammanager.beans.ContactBean;

public class ContactListWithCheckboxRowHolder extends
		TextWithCheckboxItemRowHolder {

	private ContactBean m_contact;
	
	public ContactBean getContact() {
		return m_contact;
	}

	public void setContact(ContactBean contact) {
		this.m_contact = contact;
	}

	@Override
	protected void actionOnCheckboxChange(boolean isChecked) {
		if (isChecked) {
			m_contact.setChosen(true);
		}
		else {
			m_contact.setChosen(false);
		}
		
	}

}
