package com.myteammanager.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.myteammanager.R;
import com.myteammanager.adapter.holders.BaseHolder;
import com.myteammanager.adapter.holders.RosterItemRowHolder;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.util.PlayerAndroidUtil;

public class RosterListAdapter extends BaseAdapterWithSectionHeaders  {
	
	private String LOG_TAG = RosterListAdapter.class.getName();
	
	private Context m_context;
	
	public RosterListAdapter(Context context, int layoutResourceId,
			ArrayList<BaseBean> objects) {
		super(context, layoutResourceId, objects);
		m_context = context;
	}
	

	@Override
	protected void populateHolder(BaseHolder holder, BaseBean baseBean) {
		RosterItemRowHolder realHolder = (RosterItemRowHolder)holder;
		PlayerBean player = (PlayerBean)baseBean;
		Log.d(LOG_TAG, "holder: " + holder);
		Log.d(LOG_TAG, "baseBean: " + baseBean);
		realHolder.getSurnameAndNameTextView().setText(player.getSurnameAndName(false, m_context));
		realHolder.getSecondLineTextView().setText(m_context.getString(R.string.label_played_game) + " " + player.getGamePlayed() + " " + m_context.getString(R.string.label_scored_goals) + " " + player.getGoalScored()  );
		// realHolder.getRoleLabelTextView().setText(PlayerAndroidUtil.getRoleString(getContext(), player.getRole()));
		
	}

	@Override
	protected BaseHolder getHolder() {
		return new RosterItemRowHolder();
	}

	@Override
	protected BaseBean getBean(int i) {
		return getItem(i);
	}


}
