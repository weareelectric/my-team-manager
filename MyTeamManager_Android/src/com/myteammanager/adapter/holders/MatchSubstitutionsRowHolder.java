package com.myteammanager.adapter.holders;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import org.holoeverywhere.widget.AdapterView;
import org.holoeverywhere.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import org.holoeverywhere.widget.Spinner;
import org.holoeverywhere.widget.TextView;

import com.myteammanager.R;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.beans.SubstitutionBean;

public class MatchSubstitutionsRowHolder extends BaseHolder implements
		OnItemSelectedListener {

	private TextView m_substituteName;
	private Spinner m_titularSpinner;
	private ArrayList<PlayerBean> m_titulars;
	private PlayerBean m_playerIn;

	private Context m_context;

	public MatchSubstitutionsRowHolder(Context m_context, ArrayList<PlayerBean> titulars) {
		super();
		this.m_context = m_context;
		m_titulars = titulars;
	}


	public Spinner getTitularSpinner() {
		return m_titularSpinner;
	}

	public void setTitularSpinner(Spinner titularSpinner) {
		this.m_titularSpinner = titularSpinner;
	}
	
	public TextView getSubstituteName() {
		return m_substituteName;
	}


	public void setSubstituteName(TextView m_substituteName) {
		this.m_substituteName = m_substituteName;
	}


	@Override
	public void configureViews(View convertView, BaseBean bean) {
		m_playerIn = (PlayerBean)bean;
		Spinner titularSpinner = (Spinner) convertView
				.findViewById(R.id.SpinnerPlayerOut);
		titularSpinner.setOnItemSelectedListener(this);
		setTitularSpinner(titularSpinner);
		
		TextView substituteTextView = (TextView)convertView.findViewById(R.id.textViewPlayer);
		setSubstituteName(substituteTextView);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		if (m_titularSpinner.getSelectedItemPosition() == 0) {
			m_playerIn.setReplacedPlayer(null);
		} else {
			m_playerIn.setReplacedPlayer((PlayerBean) m_titulars
					.get(pos));
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

}
