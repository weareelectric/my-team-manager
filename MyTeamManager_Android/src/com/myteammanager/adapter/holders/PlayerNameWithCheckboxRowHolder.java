package com.myteammanager.adapter.holders;

import android.view.View;

import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.PlayerBean;

public class PlayerNameWithCheckboxRowHolder extends
		TextWithCheckboxItemRowHolder {

	private PlayerBean m_player;
	
	public PlayerBean getPlayer() {
		return m_player;
	}

	public void setPlayer(PlayerBean m_player) {
		this.m_player = m_player;
	}
	
	

	@Override
	public void configureViews(View convertView, BaseBean bean) {
		m_player = (PlayerBean)bean;
		super.configureViews(convertView, bean);
	}

	@Override
	protected void actionOnCheckboxChange(boolean isChecked) {
		if (isChecked) {
			m_player.setConvocated(true);
		}
		else {
			m_player.setConvocated(false);
		}
		
	}

}
