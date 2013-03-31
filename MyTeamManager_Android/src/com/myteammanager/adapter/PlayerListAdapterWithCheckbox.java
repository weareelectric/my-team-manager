package com.myteammanager.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.myteammanager.adapter.holders.BaseHolder;
import com.myteammanager.adapter.holders.ConvocatedPlayerWithCheckboxRowHolder;
import com.myteammanager.adapter.holders.PlayerNameWithCheckboxRowHolder;
import com.myteammanager.adapter.holders.TextWithCheckboxItemRowHolder;
import com.myteammanager.adapter.holders.RosterItemRowHolder;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.beans.SeparatorBean;
import com.myteammanager.ui.CheckboxListener;
import com.myteammanager.util.PlayerAndroidUtil;

public abstract class PlayerListAdapterWithCheckbox extends BaseAdapterWithCheckbox {

	private String LOG_TAG = PlayerListAdapterWithCheckbox.class.getName();

	public PlayerListAdapterWithCheckbox(Context context, int layoutResourceId, ArrayList<BaseBean> objects,
			CheckboxListener checkboxListener) {
		super(context, layoutResourceId, objects, checkboxListener);
	}
	
	@Override
	protected String getText(BaseBean bean) {
		PlayerBean player = (PlayerBean) bean;
		return player.getSurnameAndName(false, m_context);
	}


	@Override
	protected BaseHolder getHolder() {
		return new PlayerNameWithCheckboxRowHolder();
	}
	

}
