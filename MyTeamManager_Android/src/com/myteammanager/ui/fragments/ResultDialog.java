package com.myteammanager.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;

import com.myteammanager.MyTeamManagerActivity;
import com.myteammanager.R;
import com.myteammanager.beans.MatchBean;
import com.myteammanager.events.ResultEnteredEvent;
import com.myteammanager.ui.phone.MatchDetailActivity;
import com.myteammanager.ui.phone.ScorersActivity;
import com.myteammanager.util.KeyConstants;
import com.myteammanager.util.StringUtil;

public class ResultDialog extends DialogFragment {

	private EditText m_goalHome;
	private EditText m_goalAway;

	private Button m_save;
	private Button m_delete;

	private MatchBean m_match;

	public ResultDialog() {
		// Empty constructor required for DialogFragment
	}

	public void setMatch(MatchBean match) {
		this.m_match = match;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_match_result, container);
		m_goalHome = (EditText) view.findViewById(R.id.editTextGoalHome);
		m_goalAway = (EditText) view.findViewById(R.id.editTextGoalAway);
		m_save = (Button) view.findViewById(R.id.buttonSave);
		m_delete = (Button) view.findViewById(R.id.buttonDelete);

		m_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveResult(true);
			}
		});

		m_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				int[] variations = m_match.getChangeInStatsAfterNewResult(-1, -1);

				m_match.setGoalHome(-1);
				m_match.setGoalAway(-1);
				m_match.setResultEntered(0);
				ResultEnteredEvent event = new ResultEnteredEvent(m_match, true, -1, -1);
				event.setVariationsForStats(variations);
				MyTeamManagerActivity.getBus().post(event);
				dismiss();
			}
		});

		if (m_match.getResultEntered() == 1) {
			m_delete.setVisibility(View.VISIBLE);

			m_goalHome.setText("" + m_match.getGoalHome());
			m_goalAway.setText("" + m_match.getGoalAway());
		}

		getDialog().setTitle(R.string.enter_result);

		m_goalHome.requestFocus();
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);

		return view;
	}

	public void saveResult(boolean dismiss) {
		String goalHome = m_goalHome.getText().toString();
		String goalAway = m_goalAway.getText().toString();

		Log.d("ResultDialog", "goalHome: " + goalHome + " goalAway: " + goalAway);

		int[] variations = m_match.getChangeInStatsAfterNewResult(Integer.parseInt(goalHome), Integer.parseInt(goalAway));

		m_match.setGoalHome(Integer.parseInt(goalHome));
		m_match.setGoalAway(Integer.parseInt(goalAway));
		m_match.setResultEntered(1);

		if (StringUtil.isNotEmpty(goalHome) && StringUtil.isNotEmpty(goalAway)) {
			ResultEnteredEvent event = new ResultEnteredEvent(m_match, false, Integer.parseInt(goalHome),
					Integer.parseInt(goalAway));
			event.setVariationsForStats(variations);
			MyTeamManagerActivity.getBus().post(event);
			if (dismiss) {
				dismiss();
			}

		}
	}

}
