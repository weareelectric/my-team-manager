package com.myteammanager.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.Dialog;
import org.holoeverywhere.app.DialogFragment;
import com.myteammanager.util.Log;
import org.holoeverywhere.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.EditText;

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
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater factory = LayoutInflater.from(getActivity());
	    final View view = factory.inflate(R.layout.fragment_match_result, null);
	    
		m_goalHome = (EditText) view.findViewById(R.id.editTextGoalHome);
		m_goalAway = (EditText) view.findViewById(R.id.editTextGoalAway);
	    
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				saveResult(true);
			}

		});
	    
	    if (m_match.getResultEntered() == 1) {
	    	m_goalHome.setText("" + m_match.getGoalHome());
			m_goalAway.setText("" + m_match.getGoalAway());
			
	    	builder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {

					resetMatchResult();
				}
			});
	    	
	    	builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dismiss();
					
				}
			});
	    }
	    
	    
	    
	    builder.setTitle(R.string.enter_result);
	    
	    builder.setView(view);
	    
	    AlertDialog d = builder.create();
	    d.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	    
		return d;
	}

	public void saveResult(boolean dismiss) {
		String goalHome = m_goalHome.getText().toString();
		String goalAway = m_goalAway.getText().toString();

		Log.d("ResultDialog", "goalHome: " + goalHome + " goalAway: " + goalAway);
		

		

		if (StringUtil.isNotEmpty(goalHome) && StringUtil.isNotEmpty(goalAway)) {
			int[] variations = m_match.getChangeInStatsAfterNewResult(Integer.parseInt(goalHome), Integer.parseInt(goalAway));

			m_match.setGoalHome(Integer.parseInt(goalHome));
			m_match.setGoalAway(Integer.parseInt(goalAway));
			m_match.setResultEntered(1);
			
			ResultEnteredEvent event = new ResultEnteredEvent(m_match, false, Integer.parseInt(goalHome),
					Integer.parseInt(goalAway));
			event.setVariationsForStats(variations);
			MyTeamManagerActivity.getBus().post(event);
			if (dismiss) {
				dismiss();
			}

		}
		else {
			resetMatchResult();
		}
	}

	protected void resetMatchResult() {
		int[] variations = m_match.getChangeInStatsAfterNewResult(-1, -1);

		m_match.setGoalHome(-1);
		m_match.setGoalAway(-1);
		m_match.setResultEntered(0);
		ResultEnteredEvent event = new ResultEnteredEvent(m_match, true, -1, -1);
		event.setVariationsForStats(variations);
		MyTeamManagerActivity.getBus().post(event);
		dismiss();
	}

}
