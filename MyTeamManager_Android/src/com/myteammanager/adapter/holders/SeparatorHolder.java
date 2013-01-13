package com.myteammanager.adapter.holders;

import android.view.View;
import android.widget.TextView;

import com.myteammanager.R;
import com.myteammanager.beans.BaseBean;

public class SeparatorHolder extends BaseHolder {

	private TextView m_separatorLabelTextView;

	public TextView getSeparatorLabelTextView() {
		return m_separatorLabelTextView;
	}

	public void setSeparatorLabelTextView(TextView m_separatorLabelTextView) {
		this.m_separatorLabelTextView = m_separatorLabelTextView;
	}

	@Override
	public void configureViews(View convertView, BaseBean bean) {
		m_separatorLabelTextView = (TextView)convertView.findViewById(R.id.headerLabel);		
	}
	
}
