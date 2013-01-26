package com.myteammanager.adapter.holders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.myteammanager.R;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.ui.CheckboxListener;

public class ContactHolder extends BaseHolder {

	private TextView m_textView;

	
	public TextView getTextView() {
		return m_textView;
	}
	
	public void setTextView(TextView textView) {
		this.m_textView = textView;
	}
	

	@Override
	public void configureViews(View convertView, BaseBean bean) {
		setTextView((TextView)convertView.findViewById(R.id.textView));
	}
	
}
