package com.myteammanager.adapter.holders;

import android.view.View;
import org.holoeverywhere.widget.TextView;

import com.myteammanager.R;
import com.myteammanager.beans.BaseBean;

public class RosterItemRowHolder extends BaseHolder {

	private TextView m_surnameAndNameTextView;
	private TextView m_secondLineTextView;
	
	public TextView getSurnameAndNameTextView() {
		return m_surnameAndNameTextView;
	}
	
	public void setSurnameAndNameTextView(TextView m_surnameAndNameTextView) {
		this.m_surnameAndNameTextView = m_surnameAndNameTextView;
	}
	
	public TextView getSecondLineTextView() {
		return m_secondLineTextView;
	}
	
	public void setSecondLineTextView(TextView m_roleLabelTextView) {
		this.m_secondLineTextView = m_roleLabelTextView;
	}

	@Override
	public void configureViews(View convertView, BaseBean bean) {
		setSurnameAndNameTextView((TextView)convertView.findViewById(R.id.surnameAndName));
		setSecondLineTextView((TextView)convertView.findViewById(R.id.secondLine));
	}

	
	
	
}
