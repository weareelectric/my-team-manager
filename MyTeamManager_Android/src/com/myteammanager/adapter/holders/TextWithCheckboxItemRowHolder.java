package com.myteammanager.adapter.holders;

import android.view.View;
import org.holoeverywhere.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import org.holoeverywhere.widget.TextView;

import com.myteammanager.R;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.ui.CheckboxListener;

public abstract class TextWithCheckboxItemRowHolder extends BaseHolder implements OnCheckedChangeListener {

	private TextView m_textView;
	private CheckBox m_checkbox;
	private CheckboxListener m_checkboxListener;
	
	protected boolean m_isSelectAll = false;

	
	public TextView getTextView() {
		return m_textView;
	}
	
	public void setTextView(TextView textView) {
		this.m_textView = textView;
	}
	
	public CheckBox getCheckbox() {
		return m_checkbox;
	}
	
	public void setCheckbox(CheckBox checkbox) {
		this.m_checkbox = checkbox;
	}
	
	public boolean isSelectAll() {
		return m_isSelectAll;
	}

	public void setSelectAll(boolean isSelectAll) {
		m_isSelectAll = isSelectAll;
	}

	@Override
	public void configureViews(View convertView, BaseBean bean) {
		setTextView((TextView)convertView.findViewById(R.id.textView));
		setCheckbox((CheckBox)convertView.findViewById(R.id.checkbox));
		
		
		m_checkbox.setOnCheckedChangeListener(this);
	}


	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		actionOnCheckboxChange(isChecked);
		m_checkboxListener.checkboxChanged(m_isSelectAll);
	}
	
	protected abstract void actionOnCheckboxChange(boolean isChecked);

	public void setCheckboxListener(
			CheckboxListener checkboxListener) {
		this.m_checkboxListener = checkboxListener;
	}

	
}
