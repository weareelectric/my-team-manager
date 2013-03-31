package com.myteammanager.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.myteammanager.R;
import com.myteammanager.adapter.holders.BaseHolder;
import com.myteammanager.adapter.holders.RosterItemRowHolder;
import com.myteammanager.adapter.holders.MatchSubstitutionsRowHolder;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.beans.SubstitutionBean;
import com.myteammanager.util.PlayerAndroidUtil;

public class SubstitutionsListAdapter extends BaseAdapterWithSectionHeaders {

	private String LOG_TAG = SubstitutionsListAdapter.class.getName();

	private Context m_context;

	private ArrayList<PlayerBean> m_titulars;

	public SubstitutionsListAdapter(Context context, int layoutResourceId, ArrayList<BaseBean> substitutes, ArrayList<PlayerBean> titulars) {
		super(context, layoutResourceId, substitutes);
		m_context = context;
		m_titulars = titulars;
	}

	@Override
	protected void populateHolder(BaseHolder holder, BaseBean baseBean) {
		PlayerBean replacerPlayerBean = (PlayerBean)baseBean;
		MatchSubstitutionsRowHolder realHolder = (MatchSubstitutionsRowHolder) holder;
		
		if ( replacerPlayerBean != null ) {
			realHolder.getSubstituteName().setText(replacerPlayerBean.getSurnameAndName(true, m_context));
		}
		
		if (  m_titulars != null) {
			int size = m_titulars.size();
			String[] titularsName = new String[size];
			int i = 0;
			for ( PlayerBean player : m_titulars ) {
				titularsName[i] = player.getSurnameAndName(true, m_context);
				i++;
			}
			
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(m_context,android.R.layout.simple_spinner_item, titularsName);
			spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			realHolder.getTitularSpinner().setAdapter(spinnerArrayAdapter);
			realHolder.getTitularSpinner().setSelection(m_titulars.indexOf(replacerPlayerBean.getReplacedPlayer()));
		}
		

	}

	@Override
	protected BaseHolder getHolder() {
		return new MatchSubstitutionsRowHolder(m_context, m_titulars);
	}

	@Override
	protected BaseBean getBean(int i) {
		return getItem(i);
	}

}
