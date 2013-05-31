package com.myteammanager.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import android.content.Context;
import com.myteammanager.util.Log;
import android.view.View;
import android.widget.SectionIndexer;

import com.myteammanager.R;
import com.myteammanager.adapter.holders.BaseHolder;
import com.myteammanager.adapter.holders.EventRowHolder;
import com.myteammanager.adapter.holders.MatchRowHolder;
import com.myteammanager.adapter.holders.RosterItemRowHolder;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.EventBean;
import com.myteammanager.beans.MatchBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.beans.SeparatorBean;
import com.myteammanager.beans.TeamBean;
import com.myteammanager.storage.SettingsManager;
import com.myteammanager.util.DateTimeUtil;
import com.myteammanager.util.PlayerAndroidUtil;
import com.myteammanager.util.StringUtil;

public class MatchListAdapter extends BaseAdapterWithSectionHeaders implements SectionIndexer {

	private String LOG_TAG = MatchListAdapter.class.getName();

	public MatchListAdapter(Context context, int layoutResourceId, ArrayList<BaseBean> objects) {
		super(context, layoutResourceId, objects);
	}

	@Override
	protected void populateHolder(BaseHolder holder, BaseBean baseBean) {
		MatchRowHolder realHolder = (MatchRowHolder) holder;
		MatchBean match = (MatchBean) baseBean;
		Log.d(LOG_TAG, "holder: " + holder);
		Log.d(LOG_TAG, "baseBean: " + baseBean);

		// the name of user	's team will be put where the value of team is null
		realHolder.getTeam1TextView().setText(match.getTeam1StringToShow(m_context));
		realHolder.getTeam2TextView().setText(match.getTeam2StringToShow(m_context));

		if (match.getResultEntered() == 1) {
			realHolder.getResultTextView().setText(match.getMatchResult());
		} else {
			realHolder.getResultTextView().setText("");
		}

		if (match.getTimestamp() > 0) {
			realHolder.getMatchDateTextView().setText(DateTimeUtil.getDateFrom(match.getTimestamp(), m_context));
			realHolder.getMatchDateTextView().setVisibility(View.VISIBLE);
			realHolder.getMatchTimeTextView().setText(DateTimeUtil.getTimeStringFrom(match.getTimestamp()));
			realHolder.getMatchTimeTextView().setVisibility(View.VISIBLE);
		} else {
			realHolder.getMatchDateTextView().setText("");
			realHolder.getMatchTimeTextView().setText("");
		}
	}

	@Override
	protected BaseHolder getHolder() {
		return new MatchRowHolder();
	}

	@Override
	protected boolean selectedByTheFilter(CharSequence constraint, BaseBean bean) {
		return false;
	}
}
