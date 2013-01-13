package com.myteammanager.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.SectionIndexer;

import com.myteammanager.R;
import com.myteammanager.adapter.holders.BaseHolder;
import com.myteammanager.adapter.holders.EventRowHolder;
import com.myteammanager.adapter.holders.RosterItemRowHolder;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.EventBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.beans.SeparatorBean;
import com.myteammanager.util.DateTimeUtil;
import com.myteammanager.util.PlayerAndroidUtil;
import com.myteammanager.util.StringUtil;

public class EventListAdapter extends BaseAdapterWithSectionHeaders implements SectionIndexer {

	private String LOG_TAG = EventListAdapter.class.getName();

	public EventListAdapter(Context context, int layoutResourceId, ArrayList<BaseBean> objects) {
		super(context, layoutResourceId, objects);
	}

	@Override
	protected void populateHolder(BaseHolder holder, BaseBean baseBean) {
		EventRowHolder realHolder = (EventRowHolder) holder;
		EventBean event = (EventBean) baseBean;

		// For the moment all events are practice
		realHolder.getEventLabelTextView().setText(m_context.getResources().getString(R.string.label_practice));

		realHolder.getEventDateTextView().setText(DateTimeUtil.getDateFrom(event.getTimestamp(), m_context));

		if (StringUtil.isNotEmpty(event.getLocation())) {
			realHolder.getEventLocationTextView().setText(event.getLocation());
			realHolder.getEventLocationTextView().setVisibility(View.VISIBLE);
		} else {
			realHolder.getEventLocationTextView().setText("");
			realHolder.getEventLocationTextView().setVisibility(View.GONE);
		}

		if (event.getTimestamp() > 0) {
			realHolder.getEventTimeTextView().setText(DateTimeUtil.getTimeStringFrom(event.getTimestamp()));
			realHolder.getEventTimeTextView().setVisibility(View.VISIBLE);
		} else {
			realHolder.getEventTimeTextView().setText("");
			realHolder.getEventTimeTextView().setVisibility(View.GONE);
		}

	}

	@Override
	protected BaseHolder getHolder() {
		return new EventRowHolder();
	}

}
