package com.myteammanager.adapter.holders;

import android.view.View;
import org.holoeverywhere.widget.TextView;

import com.myteammanager.R;
import com.myteammanager.beans.BaseBean;

public class MatchRowHolder extends BaseHolder {

	private TextView m_team1TextView;
	private TextView m_team2TextView;
	private TextView m_resultTextView;
	private TextView m_matchTimeTextView;
	private TextView m_matchDateTextView;

	public TextView getTeam1TextView() {
		return m_team1TextView;
	}

	public void setTeam1TextView(TextView m_team1TextView) {
		this.m_team1TextView = m_team1TextView;
	}

	public TextView getTeam2TextView() {
		return m_team2TextView;
	}

	public void setTeam2TextView(TextView m_team2TextView) {
		this.m_team2TextView = m_team2TextView;
	}

	public TextView getMatchTimeTextView() {
		return m_matchTimeTextView;
	}

	public void setMatchTimeTextView(TextView m_matchTimeTextView) {
		this.m_matchTimeTextView = m_matchTimeTextView;
	}

	public TextView getMatchDateTextView() {
		return m_matchDateTextView;
	}

	public void setMatchDateTextView(TextView m_matchDateTextView) {
		this.m_matchDateTextView = m_matchDateTextView;
	}

	public TextView getResultTextView() {
		return m_resultTextView;
	}

	public void setResultTextView(TextView m_resultTextView) {
		this.m_resultTextView = m_resultTextView;
	}

	@Override
	public void configureViews(View convertView, BaseBean bean) {
		setMatchTimeTextView((TextView) convertView.findViewById(R.id.matchTimeLabel));
		setMatchDateTextView((TextView) convertView.findViewById(R.id.matchDateLabel));
		setTeam1TextView((TextView) convertView.findViewById(R.id.team1TextView));
		setTeam2TextView((TextView) convertView.findViewById(R.id.team2TextView));
		setResultTextView((TextView) convertView.findViewById(R.id.resultTextView));
	}

}
