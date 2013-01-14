package com.myteammanager.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.myteammanager.adapter.holders.BaseHolder;
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

	private CheckboxListener m_checkboxListener;

	public PlayerListAdapterWithCheckbox(Context context, int layoutResourceId, ArrayList<BaseBean> objects,
			CheckboxListener checkboxListener) {
		super(context, layoutResourceId, objects, checkboxListener);
	}
	
	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	@Override
	protected void populateHolder(BaseHolder holder, BaseBean baseBean) {
		super.populateHolder(holder, baseBean);
		PlayerBean player = (PlayerBean)baseBean;
		PlayerNameWithCheckboxRowHolder realHolder = (PlayerNameWithCheckboxRowHolder)holder;
		realHolder.setPlayer(player);
	}
	
	@Override
	protected String getText(BaseBean bean) {
		PlayerBean player = (PlayerBean) bean;
		return player.getSurnameAndName(false);
	}

	protected abstract boolean flagTheCheckbox(BaseBean bean);

	@Override
	protected BaseHolder getHolder() {
		return new PlayerNameWithCheckboxRowHolder();
	}

	@Override
	protected BaseBean getBean(int i) {
		return getItem(i);
	}

}
