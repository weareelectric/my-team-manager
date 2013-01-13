package com.myteammanager.adapter.holders;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.myteammanager.R;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.ui.PlayerCheckboxListener;

public class PlayerWithCheckboxItemRowHolder extends BaseHolder implements OnCheckedChangeListener {

	private TextView m_surnameAndNameTextView;
	private CheckBox m_checkboxConvocated;
	private PlayerCheckboxListener m_convocationListener;
	
	private PlayerBean m_player;
	
	public TextView getSurnameAndNameTextView() {
		return m_surnameAndNameTextView;
	}
	
	public void setSurnameAndNameTextView(TextView m_surnameAndNameTextView) {
		this.m_surnameAndNameTextView = m_surnameAndNameTextView;
	}
	
	public CheckBox getCheckboxConvocated() {
		return m_checkboxConvocated;
	}
	
	public void setCheckboxConvocated(CheckBox roleLabelTextView) {
		this.m_checkboxConvocated = roleLabelTextView;
	}

	@Override
	public void configureViews(View convertView, BaseBean bean) {
		setSurnameAndNameTextView((TextView)convertView.findViewById(R.id.textViewSurnameAndName));
		setCheckboxConvocated((CheckBox)convertView.findViewById(R.id.checkboxConvocated));
		
		
		m_checkboxConvocated.setOnCheckedChangeListener(this);
	}

	public PlayerBean getPlayer() {
		return m_player;
	}

	public void setPlayer(PlayerBean m_player) {
		this.m_player = m_player;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			m_player.setConvocated(true);
		}
		else {
			m_player.setConvocated(false);
		}
		
		m_convocationListener.convocationChanged();
	}

	public void setConvocationListener(
			PlayerCheckboxListener m_convocationListener) {
		this.m_convocationListener = m_convocationListener;
	}

	
}
