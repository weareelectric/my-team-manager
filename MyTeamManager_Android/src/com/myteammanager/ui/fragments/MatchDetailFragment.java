package com.myteammanager.ui.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import com.myteammanager.util.Log;
import org.holoeverywhere.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.CheckBox;
import android.widget.ScrollView;
import org.holoeverywhere.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.myteammanager.MyTeamManagerActivity;
import com.myteammanager.R;
import com.myteammanager.beans.MatchBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.beans.ScorerBean;
import com.myteammanager.events.EventOrMatchChanged;
import com.myteammanager.events.ResultEnteredEvent;
import com.myteammanager.events.ScorersChangeEvent;
import com.myteammanager.events.TeamLineupSelectionEvent;
import com.myteammanager.storage.DBManager;
import com.myteammanager.storage.SettingsManager;
import com.myteammanager.ui.phone.AddEventInfoActivity;
import com.myteammanager.ui.phone.EditConvocationActivity;
import com.myteammanager.ui.phone.EditTeamLineUpActivity;
import com.myteammanager.ui.phone.MatchSubstitutionsActivity;
import com.myteammanager.ui.phone.PostMatchDetailActivity;
import com.myteammanager.ui.phone.ScorersActivity;
import com.myteammanager.util.DateTimeUtil;
import com.myteammanager.util.DeleteMatchManager;
import com.myteammanager.util.KeyConstants;
import com.squareup.otto.Subscribe;

public class MatchDetailFragment extends BaseFragment implements TextWatcher {

	private static final String LOG_TAG = MatchDetailFragment.class.getName();

	private MatchBean m_match;

	private TextView m_textViewMatch;
	private TextView m_textViewMatchDate;
	private TextView m_textViewMatchTime;
	// private TextView m_textViewMatchLocation;
	private TextView m_textViewMatchNote;
	private TextView m_textViewScorer;
	private TextView m_textViewResult;
	// private CheckBox m_eventCanceledCheckBox;
	private Button m_convocationButton;
	private Button m_lineupButton;
	private Button m_resultButton;
	private Button m_scorerButton;
	private Button m_substitutionsButton;

	protected ScrollView m_root;

	private Resources m_res;

	private ArrayList<ScorerBean> m_scorers;

	private MenuItem m_menuItem1;

	public MatchDetailFragment() {
		super();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		m_root = (ScrollView) inflater.inflate(R.layout.fragment_match_detail, null, false);

		Bundle extra = getActivity().getIntent().getExtras();
		m_res = getActivity().getResources();

		m_match = (MatchBean) extra.get(KeyConstants.KEY_BEANDATA);
		getActivity().setTitle(m_res.getString(R.string.label_practice));

		// Match
		m_textViewMatch = (TextView) m_root.findViewById(R.id.textViewMatch);
		m_textViewMatchTime = (TextView) m_root.findViewById(R.id.textViewTime);
		// m_textViewMatchLocation = (TextView) m_root.findViewById(R.id.textViewLocation);
		m_textViewMatchDate = (TextView) m_root.findViewById(R.id.textViewDate);
		m_textViewMatchNote = (TextView) m_root.findViewById(R.id.textViewNote);
		m_textViewScorer = (TextView) m_root.findViewById(R.id.textViewScorer);
		m_textViewResult = (TextView) m_root.findViewById(R.id.textViewResult);
		// m_eventCanceledCheckBox = (CheckBox) m_root.findViewById(R.id.checkBoxCanceled);
		m_convocationButton = (Button) m_root.findViewById(R.id.buttonConvocation);
		m_lineupButton = (Button) m_root.findViewById(R.id.buttonLineUp);
		m_resultButton = (Button) m_root.findViewById(R.id.buttonResult);
		m_scorerButton = (Button) m_root.findViewById(R.id.buttonScorer);
		m_substitutionsButton = (Button) m_root.findViewById(R.id.buttonSubstitutions);

		if (m_match.getResultEntered() == 1) {
			m_textViewResult.setText(m_match.getMatchResult());
		}

		m_resultButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fm = getFragmentManager();
				ResultDialog editNameDialog = new ResultDialog();
				editNameDialog.setMatch(m_match);
				editNameDialog.show(fm, "fragment_edit_result");

			}
		});

		updateButtons();

		m_lineupButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), EditTeamLineUpActivity.class);
				intent.putExtra(KeyConstants.KEY_MATCH, m_match);
				startActivityForResult(intent, KeyConstants.CODE_BEAN_CHANGE);
			}
		});

		m_scorerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ScorersActivity.class);
				intent.putExtra(KeyConstants.KEY_MATCH, m_match);
				startActivity(intent);
			}
		});
		
		m_substitutionsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MatchSubstitutionsActivity.class);
				intent.putExtra(KeyConstants.KEY_MATCH, m_match);
				startActivity(intent);
			}
		});


		updateFields();

		MyTeamManagerActivity.getBus().register(this);

		getActivity().setTitle(R.string.title_match);

		return m_root;
	}

	protected void updateButtons() {
		if (m_match.getLineupConfigured() == 1) {
			disableConvocationButton();
			enableSubstitutionsButton();
		} else {
			disableSubstitutionsButton();
			m_convocationButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), EditConvocationActivity.class);
					intent.putExtra(KeyConstants.KEY_MATCH, m_match);
					startActivityForResult(intent, KeyConstants.CODE_BEAN_CHANGE);
				}
			});
		}
	}

	public void disableConvocationButton() {
		m_convocationButton.setClickable(false);
		m_convocationButton.setEnabled(false);
	}

	public void enableConvocationButton() {
		m_convocationButton.setClickable(true);
		m_convocationButton.setEnabled(true);
	}
	
	public void disableSubstitutionsButton() {
		m_substitutionsButton.setClickable(false);
		m_substitutionsButton.setEnabled(false);
	}
	
	public void enableSubstitutionsButton() {
		m_substitutionsButton.setClickable(true);
		m_substitutionsButton.setEnabled(true);
	}
	

	protected void updateFields() {
		m_textViewMatch.setText(m_match.getTeam1StringToShow(getActivity()) + " - "
				+ m_match.getTeam2StringToShow(getActivity()));
		m_textViewMatchDate.setText(m_res.getString(R.string.label_date_semicoloumn,
				DateTimeUtil.getDateFrom(m_match.getTimestamp(), getActivity())));
		m_textViewMatchTime.setText(m_res.getString(R.string.label_time_semicoloumn,
				DateTimeUtil.getTimeStringFrom(m_match.getTimestamp())));
		// m_textViewMatchLocation.setText(m_res.getString(R.string.label_location_semicoloumn, m_match.getLocation()));
		m_textViewMatchNote.setText(m_res.getString(R.string.label_note_semicoloumn, m_match.getNote()));
		// m_eventCanceledCheckBox.setChecked(m_match.getCanceled() == 1);
		m_convocationButton.setText(getString(R.string.btn_convocation, "" + m_match.getNumberOfPlayerConvocated()));

		m_scorers = (ArrayList<ScorerBean>) DBManager.getInstance().getListOfBeansWhere(
				new ScorerBean(), "match = " + m_match.getId(), false);

		updateScorerTextView(m_scorers);

	}

	public void updateScorerTextView(ArrayList<ScorerBean> scorers) {
		StringBuffer stringForScorer = getScorersString(scorers);

		m_textViewScorer.setText(stringForScorer.toString());
	}

	public StringBuffer getScorersString(ArrayList<ScorerBean> scorers) {
		StringBuffer stringForScorer = new StringBuffer();
		int i = 0;
		if (scorers.size() > 0) {
			for (ScorerBean scorer : scorers) {
				if (scorer.getMatch().getId() == m_match.getId()) {
					if (i != 0) {
						stringForScorer.append(" - ");
					}
					PlayerBean player = scorer.getPlayer();
					if (player != null) {
						stringForScorer.append(player.getSurnameAndName(true, getActivity()));
					} else {
						stringForScorer.append(getResources().getString(R.string.label_unknown_player));
					}

					if (scorer.getScoredGoal() > 1) {
						stringForScorer.append(" (" + scorer.getScoredGoal() + ")");
					}

					i++;
				}
			}
		}
		return stringForScorer;
	}

	@Override
	public void afterTextChanged(Editable arg0) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void button1Pressed(int alertId) {
		DeleteMatchManager.deleteMatch(m_match, getActivity());
		MyTeamManagerActivity.getBus().post(new EventOrMatchChanged());

		Intent intent = new Intent();
		intent.putExtra(KeyConstants.KEY_BEANDATA, m_match);
		getActivity().setResult(KeyConstants.RESULT_BEAN_DELETED, intent);
		
		
		getActivity().finish();
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.event_menu_detail, menu);
		m_menuItem1 = menu.findItem(R.id.menu_share_match);
		
		super.onCreateOptionsMenu(menu, inflater);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_edit_event_match:
			Intent intent = new Intent(getActivity(), AddEventInfoActivity.class);
			intent.putExtra(KeyConstants.KEY_BEANDATA, m_match);
			intent.putExtra(KeyConstants.KEY_EVENT_OR_MATCH, false);
			startActivityForResult(intent, KeyConstants.CODE_BEAN_CHANGE);
			break;

		case R.id.menu_edit_delete_event_match:
			showDeleteConfirmation();
			break;

		case R.id.menu_share_match:
			if ( SettingsManager.getInstance(getSupportActivity()).isFacebookActivated()) {
				intent = new Intent(getActivity(), PostMatchDetailActivity.class);
				StringBuffer sb = new StringBuffer();
				sb.append(m_match.getMatchString(getActivity()));
				if (m_match.getResultEntered() > 0) {
					sb.append(" ");
					sb.append(m_match.getMatchResult());
				}

				if (null != m_scorers && m_scorers.size() > 0) {
					sb.append("\n\n");
					sb.append(m_res.getString(R.string.label_scorer));
					sb.append(" ");
					sb.append(getScorersString(m_scorers));
				}

				intent.putExtra(KeyConstants.KEY_MSG_TEXT, sb.toString());

				startActivity(intent);
			}
			else {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage(getString(R.string.msg_suggest_activate_facebook_from_settings));
				builder.show();
			}

			break;
		}
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(LOG_TAG, "Request Code: " + requestCode);
		Log.d(LOG_TAG, "Result Code: " + resultCode);
		if (requestCode == KeyConstants.CODE_BEAN_CHANGE) {
			if (resultCode == KeyConstants.RESULT_BEAN_EDITED) {

				m_match = (MatchBean) data.getExtras().get(KeyConstants.KEY_BEANDATA);
				Log.d(LOG_TAG, "Number of players convocated: " + m_match.getNumberOfPlayerConvocated());
				updateFields();
				
				updateButtons();
				
			} else if (resultCode == KeyConstants.RESULT_BEAN_DELETED) {
				getActivity().finish();
			}
		}
	}

	@Override
	public void button2Pressed(int alertId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void button3Pressed(int alertId) {
		// TODO Auto-generated method stub
	}

	@Subscribe
	public void resultChanged(ResultEnteredEvent event) {

		if (event.isResultDeleted()) {
			m_match.setResultEntered(0);
			m_match.setGoalHome(event.getGoalHome());
			m_match.setGoalAway(event.getGoalAway());

			m_textViewResult.setText("");

			DBManager.getInstance().updateBean(m_match);
		} else {
			m_match.setResultEntered(1);
			m_match.setGoalHome(event.getGoalHome());
			m_match.setGoalAway(event.getGoalAway());

			m_textViewResult.setText(m_match.getMatchResult());

			DBManager.getInstance().updateBean(m_match);
			
		}
		
		MyTeamManagerActivity.getBus().post(new EventOrMatchChanged(m_match));
		updateButtons();
	}

	@Subscribe
	public void lineupChanged(TeamLineupSelectionEvent event) {
		Log.d(LOG_TAG, "lineupChangedEvent: " + event.isLineupHasBeenDefined());
		if (event.isLineupHasBeenDefined()) {
			disableConvocationButton();
			enableSubstitutionsButton();
		} else {
			enableConvocationButton();
			disableSubstitutionsButton();
		};
	}

	@Subscribe
	public void scorersChanged(ScorersChangeEvent event) {
		ArrayList<ScorerBean> scorers = event.getScorers();
		updateScorerTextView(scorers);
	}

	@Override
	public void onDestroy() {
		super.onStop();
		MyTeamManagerActivity.getBus().unregister(this);
	}
}
