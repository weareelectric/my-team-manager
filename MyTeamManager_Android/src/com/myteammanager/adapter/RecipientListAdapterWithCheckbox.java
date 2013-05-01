package com.myteammanager.adapter;

import java.util.ArrayList;

import android.content.Context;
import com.myteammanager.util.Log;

import com.myteammanager.adapter.holders.BaseHolder;
import com.myteammanager.adapter.holders.ConvocatedPlayerWithCheckboxRowHolder;
import com.myteammanager.adapter.holders.RecipientWithCheckboxRowHolder;
import com.myteammanager.adapter.holders.TextWithCheckboxItemRowHolder;
import com.myteammanager.adapter.holders.RosterItemRowHolder;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.beans.SeparatorBean;
import com.myteammanager.ui.CheckboxListener;
import com.myteammanager.util.PlayerAndroidUtil;

public class RecipientListAdapterWithCheckbox extends BaseAdapterWithCheckbox {

	private String LOG_TAG = RecipientListAdapterWithCheckbox.class.getName();

	public RecipientListAdapterWithCheckbox(Context context, int layoutResourceId, ArrayList<BaseBean> objects,
			CheckboxListener checkboxListener) {
		super(context, layoutResourceId, objects, checkboxListener);
	}
	
	@Override
	protected void populateHolder(BaseHolder holder, BaseBean baseBean) {
		super.populateHolder(holder, baseBean);
		RecipientWithCheckboxRowHolder realHolder = (RecipientWithCheckboxRowHolder)holder;
		PlayerBean player = (PlayerBean)baseBean;
		realHolder.setPlayer(player);
	}
	
	@Override
	protected String getText(BaseBean bean) {
		PlayerBean player = (PlayerBean) bean;
		return player.getSurnameAndName(false, m_context);
	}

	@Override
	protected BaseHolder getHolder() {
		return new RecipientWithCheckboxRowHolder();
	}

	@Override
	protected boolean flagTheCheckbox(BaseBean bean) {
		if ( bean instanceof PlayerBean ) {
			PlayerBean player = (PlayerBean)bean;
			return player.isRecipient();
		}
		
		return false;
	}


}
