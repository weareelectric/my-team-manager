package com.myteammanager.adapter;

import java.util.ArrayList;

import android.content.Context;
import com.myteammanager.util.Log;

import com.myteammanager.adapter.holders.BaseHolder;
import com.myteammanager.adapter.holders.ContactListWithCheckboxRowHolder;
import com.myteammanager.adapter.holders.ConvocatedPlayerWithCheckboxRowHolder;
import com.myteammanager.adapter.holders.TextWithCheckboxItemRowHolder;
import com.myteammanager.adapter.holders.RosterItemRowHolder;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.ContactBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.beans.SeparatorBean;
import com.myteammanager.ui.CheckboxListener;
import com.myteammanager.util.PlayerAndroidUtil;

public class ContactListAdapterWithCheckbox extends BaseAdapterWithCheckbox {

	private String LOG_TAG = ContactListAdapterWithCheckbox.class.getName();


	public ContactListAdapterWithCheckbox(Context context, int layoutResourceId, ArrayList<BaseBean> objects,
			CheckboxListener checkboxListener) {
		super(context, layoutResourceId, objects, checkboxListener);
	}
	

	@Override
	protected void populateHolder(BaseHolder holder, BaseBean baseBean) {
		// Log.d(LOG_TAG, "Populate holder");
		super.populateHolder(holder, baseBean);
		ContactBean contact = (ContactBean)baseBean;
		ContactListWithCheckboxRowHolder realHolder = (ContactListWithCheckboxRowHolder)holder;
		realHolder.setContact(contact);
	}
	
	@Override
	protected String getText(BaseBean bean) {
		ContactBean contact = (ContactBean) bean;
		return contact.getDisplayName();
	}

	protected boolean flagTheCheckbox(BaseBean bean) {
		if ( ! ( bean instanceof ContactBean ) ) {
			return false;
		}
		else {
			
			ContactBean contact = (ContactBean)bean;
			// Log.d(LOG_TAG, "Contact " + contact.getDisplayName() + " isChosen: " + contact.isChosen());
			return contact.isChosen();
		}
	}

	@Override
	protected BaseHolder getHolder() {
		return new ContactListWithCheckboxRowHolder();
	}
	
	@Override
	protected boolean selectedByTheFilter(CharSequence constraint, BaseBean bean) {
		if ( !(bean instanceof ContactBean) ) {
			return false;
		}
		
		ContactBean contact = (ContactBean)bean;
		return contact.getDisplayName().toLowerCase()
                .contains(constraint.toString().toLowerCase());
	}

}
