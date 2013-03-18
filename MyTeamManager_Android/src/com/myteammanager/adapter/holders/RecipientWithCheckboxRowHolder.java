package com.myteammanager.adapter.holders;

import android.view.View;

import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.PlayerBean;

public class RecipientWithCheckboxRowHolder extends
		PlayerNameWithCheckboxRowHolder {
	
	

	@Override
	protected void actionOnCheckboxChange(boolean isChecked) {
		if (isChecked) {
			m_player.setIsRecipient(true);
		}
		else {
			m_player.setIsRecipient(false);
		}
		
	}

}
