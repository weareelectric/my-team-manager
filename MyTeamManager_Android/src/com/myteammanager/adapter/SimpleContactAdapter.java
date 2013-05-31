package com.myteammanager.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.widget.Filter;

import com.myteammanager.adapter.holders.BaseHolder;
import com.myteammanager.adapter.holders.ContactHolder;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.ContactBean;
import com.ubikod.capptain.bq;

public class SimpleContactAdapter extends BaseAdapterWithSectionHeaders {

	public SimpleContactAdapter(Context context, int layoutResourceId,
			ArrayList<BaseBean> objects) {
		super(context, layoutResourceId, objects);
	}

	@Override
	protected BaseHolder getHolder() {
		return new ContactHolder();
	}

	@Override
	protected void populateHolder(BaseHolder holder, BaseBean bean) {
		ContactHolder textHolder = (ContactHolder)holder;
		ContactBean contact = (ContactBean)bean;
		
		textHolder.getTextView().setText(contact.getDisplayName());
	}

	@Override
	protected boolean selectedByTheFilter(CharSequence constraint, BaseBean bean) {
		if ( !(bean instanceof ContactBean) ) {
			return false;
		}
		
		ContactBean contact = (ContactBean)bean;
		return contact.getDisplayName().toLowerCase()
                .contains(constraint.toString());
		
	}




	
	

}
