package com.myteammanager.adapter.holders;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.myteammanager.R;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.PlayerBean;

public class ScorerItemRowHolder extends BaseHolder implements OnItemSelectedListener {

	private TextView m_surnameAndNameTextView;
	private Spinner m_goalSpinner;
	private PlayerBean m_player;

	private Context m_context;

	public ScorerItemRowHolder(Context m_context) {
		super();
		this.m_context = m_context;
	}

	public TextView getSurnameAndNameTextView() {
		return m_surnameAndNameTextView;
	}

	public void setSurnameAndNameTextView(TextView m_surnameAndNameTextView) {
		this.m_surnameAndNameTextView = m_surnameAndNameTextView;
	}

	public Spinner getGoalSpinner() {
		return m_goalSpinner;
	}

	public void setGoalSpinner(Spinner goalSpinner) {
		this.m_goalSpinner = goalSpinner;
	}

	@Override
	public void configureViews(View convertView, BaseBean bean) {
		m_player = (PlayerBean) bean;

		setSurnameAndNameTextView((TextView) convertView.findViewById(R.id.playerTextView));
		Spinner spinnerGoal = (Spinner) convertView.findViewById(R.id.goalSpinner);
		spinnerGoal.setOnItemSelectedListener(this);

		setGoalSpinner(spinnerGoal);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		m_player.setGoalScoredInTheMatch(pos);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		m_player.setGoalScoredInTheMatch(0);
	}

}
