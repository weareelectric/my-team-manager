package com.myteammanager.adapter;

import java.util.ArrayList;

import android.content.Context;

import com.myteammanager.adapter.holders.BaseHolder;
import com.myteammanager.adapter.holders.ContactHolder;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.ContactBean;

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

}
