package com.myteammanager.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.myteammanager.R;
import com.myteammanager.adapter.holders.BaseHolder;
import com.myteammanager.adapter.holders.RosterItemRowHolder;
import com.myteammanager.adapter.holders.MatchSubstitutionsRowHolder;
import com.myteammanager.adapter.holders.ScorerItemRowHolder;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.util.PlayerAndroidUtil;

public class ScorersListAdapter extends BaseAdapterWithSectionHeaders {

	private String LOG_TAG = ScorersListAdapter.class.getName();

	private Context m_context;

	private PlayerBean m_player;

	public ScorersListAdapter(Context context, int layoutResourceId, ArrayList<BaseBean> objects) {
		super(context, layoutResourceId, objects);
		m_context = context;
	}

	@Override
	protected void populateHolder(BaseHolder holder, BaseBean baseBean) {
		ScorerItemRowHolder realHolder = (ScorerItemRowHolder) holder;
		m_player = (PlayerBean) baseBean;
		Log.d(LOG_TAG, "holder: " + holder);
		realHolder.getSurnameAndNameTextView().setText(m_player.getSurnameAndName(false, m_context));
		realHolder.getGoalSpinner().setSelection(m_player.getGoalScoredInTheMatch());
		// realHolder.getRoleLabelTextView().setText(PlayerAndroidUtil.getRoleString(getContext(), player.getRole()));

	}

	@Override
	protected BaseHolder getHolder() {
		return new ScorerItemRowHolder(m_context);
	}

	@Override
	protected BaseBean getBean(int i) {
		return getItem(i);
	}

}
