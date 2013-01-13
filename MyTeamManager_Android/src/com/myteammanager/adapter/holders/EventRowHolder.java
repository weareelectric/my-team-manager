package com.myteammanager.adapter.holders;

import android.view.View;
import android.widget.TextView;

import com.myteammanager.R;
import com.myteammanager.beans.BaseBean;

public class EventRowHolder extends BaseHolder {

	private TextView m_eventLabelTextView;
	private TextView m_eventLocationTextView;
	private TextView m_eventTimeTextView;
	private TextView m_eventDateTextView;
	
	public TextView getEventLabelTextView() {
		return m_eventLabelTextView;
	}
	
	public void setEventLabelTextView(TextView m_eventLabelTextView) {
		this.m_eventLabelTextView = m_eventLabelTextView;
	}
	
	public TextView getEventLocationTextView() {
		return m_eventLocationTextView;
	}
	
	public void setEventLocationTextView(TextView m_eventLocationTextView) {
		this.m_eventLocationTextView = m_eventLocationTextView;
	}
	
	public TextView getEventTimeTextView() {
		return m_eventTimeTextView;
	}
	
	public void setEventTimeTextView(TextView m_eventTimeTextView) {
		this.m_eventTimeTextView = m_eventTimeTextView;
	}
	
	public TextView getEventDateTextView() {
		return m_eventDateTextView;
	}

	public void setEventDateTextView(TextView m_eventDateTextView) {
		this.m_eventDateTextView = m_eventDateTextView;
	}

	@Override
	public void configureViews(View convertView, BaseBean bean) {
		setEventLabelTextView((TextView)convertView.findViewById(R.id.eventLabel));
		setEventLocationTextView((TextView)convertView.findViewById(R.id.LocationLabel));
		setEventTimeTextView((TextView)convertView.findViewById(R.id.timeLabel));
		setEventDateTextView((TextView)convertView.findViewById(R.id.dateLabel));
	}

	
	
	
}
