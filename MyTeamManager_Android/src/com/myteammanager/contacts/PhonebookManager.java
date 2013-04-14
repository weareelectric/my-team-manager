package com.myteammanager.contacts;

import java.util.ArrayList;

import org.holoeverywhere.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.util.Log;

import com.myteammanager.beans.ContactBean;
import com.myteammanager.exceptions.NoDataException;
import com.myteammanager.ui.fragments.MatchDetailFragment;

public class PhonebookManager {
	
	private static final String LOG_TAG = MatchDetailFragment.class.getName();

	public static ArrayList<ContactBean> getContacts(Activity activity)
			throws NoDataException {
		// get all mail addresses from address book

		
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[] { ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts._ID};
		String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = 1";
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";

		Cursor cursor = activity.managedQuery(uri, projection, selection, null,
				sortOrder);

		if (cursor.getCount() == 0) {
			cursor.close();
			throw new NoDataException();
		}
			

		ArrayList<ContactBean> addressBook = new ArrayList<ContactBean>();

		if (cursor.moveToFirst()) {

			do {
				
				ContactBean addressBookBean = new ContactBean();
				addressBookBean.setDisplayName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
				addressBookBean.setId(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)));
				readAllDataFor(addressBookBean, activity);
				addressBook.add(addressBookBean);

			} while (cursor.moveToNext());

		}
		
		cursor.close();
		return addressBook;
	}
	
	/**
	 * Adds specific informations to the AddressBookBean:
	 * - lastname,
	 * - firstname,
	 * - emails,
	 * - phones
	 * @param abBean
	 */
	public static void readAllDataFor(ContactBean abBean, Activity activity) {
		getAllEmails(abBean, activity);
		getAllPhones(abBean, activity);
		getLastAndFirstName(abBean, activity);
	}

	private static void getAllEmails(ContactBean abBean, Activity activity) {
		final String[] projection = new String[] {
				Email.DATA,			
		};
		
		final Cursor email = activity.managedQuery(
				Email.CONTENT_URI,
				projection,
				Data.CONTACT_ID + "=?",
				new String[]{String.valueOf(abBean.getId())},
				null);
		
		if(email.moveToFirst()) {
			final int contactEmailColumnIndex = email.getColumnIndex(Email.DATA);

			while(!email.isAfterLast()) {
				final String address = email.getString(contactEmailColumnIndex);
				Log.d(LOG_TAG, "Adding the email '" + address + "' to " + " '" + abBean.getDisplayName() + "'");
				abBean.addEmail(address);
				email.moveToNext();
			}

		}
		email.close();
	}
	
	private static void getAllPhones(ContactBean abBean, Activity activity) {
		final String[] projection = new String[] {
				Phone.NUMBER,
		};
		
		final Cursor phone = activity.managedQuery(
				Phone.CONTENT_URI,
				projection,
				Data.CONTACT_ID + "=?",
				new String[]{String.valueOf(abBean.getId())},
				null);
		
		if(phone.moveToFirst()) {
			final int contactNumberColumnIndex = phone.getColumnIndex(Phone.NUMBER);

			while(!phone.isAfterLast()) {
				final String number = phone.getString(contactNumberColumnIndex);
				Log.d(LOG_TAG, "Adding the phone number '" + number + "' to " + " '" + abBean.getDisplayName() + "'");
				abBean.addPhone(number);
				phone.moveToNext();
			}

		}
		phone.close();
	}
	
	
	private static void getLastAndFirstName(ContactBean abBean, Activity activity) {
		String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?";
		String[] whereNameParams = new String[] { ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, abBean.getId() };
		Cursor nameCur =  activity.managedQuery(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
		while (nameCur.moveToNext()) {
		    String given = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
		    String family = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
		    abBean.setFirstName(given);
		    abBean.setLastName(family);
		    
		    Log.d(LOG_TAG, "AddressBookBean: " + abBean.toString());
		}
		nameCur.close();
	}
	
	
}
