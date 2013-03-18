package com.myteammanager.adapter.holders;

import android.view.View;

import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.PlayerBean;

public class PlayerNameWithCheckboxRowHolder extends
		TextWithCheckboxItemRowHolder {

	protected PlayerBean m_player;
	
	public PlayerBean getPlayer() {
		return m_player;
	}

	public void setPlayer(PlayerBean player) {
		this.m_player = player;
		
		m_isSelectAll = this.m_player.isFakeSelectAll();
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
