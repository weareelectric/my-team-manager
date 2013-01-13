package com.myteammanager.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.myteammanager.adapter.holders.BaseHolder;
import com.myteammanager.adapter.holders.PlayerWithCheckboxItemRowHolder;
import com.myteammanager.adapter.holders.RosterItemRowHolder;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.beans.SeparatorBean;
import com.myteammanager.ui.PlayerCheckboxListener;
import com.myteammanager.util.PlayerAndroidUtil;

public abstract class PlayerListAdapterWithCheckbox extends BaseAdapterWithSectionHeaders {

	private String LOG_TAG = PlayerListAdapterWithCheckbox.class.getName();

	private PlayerCheckboxListener m_playerCheckboxListener;

	public PlayerListAdapterWithCheckbox(Context context, int layoutResourceId, ArrayList<BaseBean> objects,
			PlayerCheckboxListener playerCheckboxListeenr) {
		super(context, layoutResourceId, objects);
		m_playerCheckboxListener = playerCheckboxListeenr;
	}
	
	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	@Override
	protected void populateHolder(BaseHolder holder, BaseBean baseBean) {
		PlayerWithCheckboxItemRowHolder realHolder = (PlayerWithCheckboxItemRowHolder) holder;
		realHolder.setConvocationListener(m_playerCheckboxListener);
		PlayerBean player = (PlayerBean) baseBean;
		realHolder.setPlayer(player);
		realHolder.getSurnameAndNameTextView().setText(player.getSurnameAndName(false));

		if (flagTheCheckbox(player)) {
			realHolder.getCheckboxConvocated().setChecked(true);
		} else {
			realHolder.getCheckboxConvocated().setChecked(false);
		}
	}

	protected abstract boolean flagTheCheckbox(BaseBean bean);

	@Override
	protected BaseHolder getHolder() {
		return new PlayerWithCheckboxItemRowHolder();
	}

	@Override
	protected BaseBean getBean(int i) {
		return getItem(i);
	}

}
