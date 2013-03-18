package com.myteammanager.ui.fragments;

import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.myteammanager.MyTeamManagerActivity;
import com.myteammanager.R;
import com.myteammanager.adapter.ConvocationAdapter;
import com.myteammanager.adapter.RecipientListAdapterWithCheckbox;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.EventBean;
import com.myteammanager.beans.MatchBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.events.EventOrMatchChanged;
import com.myteammanager.events.EventsListChanged;
import com.myteammanager.events.MatchListChanged;
import com.myteammanager.events.ResultEnteredEvent;
import com.myteammanager.specializedStorage.MyTeamManagerDBManager;
import com.myteammanager.storage.DBManager;
import com.myteammanager.storage.SettingsManager;
import com.myteammanager.ui.ButtonsAlertDialogListener;
import com.myteammanager.ui.CheckboxListener;
import com.myteammanager.ui.phone.AddEventInfoActivity;
import com.myteammanager.ui.phone.EventDetailActivity;
import com.myteammanager.ui.phone.EventsListActivity;
import com.myteammanager.ui.phone.MatchDetailActivity;
import com.myteammanager.ui.phone.MatchesListActivity;
import com.myteammanager.ui.phone.RosterActivity;
import com.myteammanager.ui.phone.SendMessageActivity;
import com.myteammanager.util.DateTimeUtil;
import com.myteammanager.util.KeyConstants;
import com.squareup.otto.Subscribe;

public class NewHomeFragment extends BaseFragment implements CheckboxListener,
		DialogInterface.OnClickListener {

	public static final String LOG_TAG = NewHomeFragment.class.getName();

	private Button m_rosterButton;
	private Button m_matchesButton;
	private Button m_trainingButton;
	private Button m_buttonNextMatchEntered;
	private Button m_buttonNextTrainingEntered;
	private Button m_messageButton;

	private TextView m_pointsTextView;
	private TextView m_playedTextView;
	private TextView m_wonTextView;
	private TextView m_drawTextView;
	private TextView m_lostTextView;
	private TextView m_scoredTextView;
	private TextView m_againstTextView;

	private Button m_buttonNextEvent;
	private Button m_buttonNextMatch;

	private MatchBean m_nextMatch;
	private EventBean m_nextTraining;

	private ArrayList<BaseBean> m_playersWithPhoneOrEmail;
	private RecipientListAdapterWithCheckbox m_recipientListAdapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		LinearLayout m_root = (LinearLayout) inflater.inflate(
				R.layout.fragment_home, null, false);

		m_rosterButton = (Button) m_root.findViewById(R.id.buttonRoster);
		m_matchesButton = (Button) m_root.findViewById(R.id.buttonMatches);
		m_trainingButton = (Button) m_root.findViewById(R.id.buttonTraining);
		m_messageButton = (Button) m_root.findViewById(R.id.buttonMessage);

		m_buttonNextEvent = (Button) m_root.findViewById(R.id.nextEventButton);
		m_buttonNextMatch = (Button) m_root.findViewById(R.id.nextMatchButton);

		m_buttonNextMatch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getSherlockActivity(),
						AddEventInfoActivity.class);
				intent.putExtra(KeyConstants.KEY_EVENT_OR_MATCH, false);
				startActivity(intent);
			}
		});

		m_buttonNextEvent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getSherlockActivity(),
						AddEventInfoActivity.class);
				intent.putExtra(KeyConstants.KEY_EVENT_OR_MATCH, true);
				startActivity(intent);
			}
		});

		m_messageButton = (Button) m_root.findViewById(R.id.buttonMessage);
		m_messageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getSherlockActivity());
				builder.setTitle(getString(R.string.title_select_recipients));

				m_playersWithPhoneOrEmail = (ArrayList<BaseBean>) MyTeamManagerDBManager
						.getInstance()
						.getListOfBeansWhere(
								new PlayerBean(),
								"(phone is not null and phone <> '') or (email is not null and email <> '')",
								true);
				PlayerBean selectAllPlayer = new PlayerBean();
				selectAllPlayer.setIsFakeSelectAll(true);
				selectAllPlayer
						.setLastName(getString(R.string.label_select_all));
				m_playersWithPhoneOrEmail.add(0, selectAllPlayer);

				m_recipientListAdapter = new RecipientListAdapterWithCheckbox(
						getSherlockActivity(), R.layout.list_with_checkbox,
						m_playersWithPhoneOrEmail, NewHomeFragment.this);

				builder.setAdapter(m_recipientListAdapter, null);
				AlertDialog alert = builder.create();
				alert.setButton(AlertDialog.BUTTON_POSITIVE,
						getString(R.string.label_ok), NewHomeFragment.this);
				alert.show();
			}
		});

		m_buttonNextMatchEntered = (Button) m_root
				.findViewById(R.id.textViewMatch);
		m_buttonNextMatchEntered.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getSherlockActivity(),
						MatchDetailActivity.class);
				intent.putExtra(KeyConstants.KEY_BEANDATA, m_nextMatch);
				startActivity(intent);
			}
		});

		m_buttonNextTrainingEntered = (Button) m_root
				.findViewById(R.id.textViewTrainingDate);
		m_buttonNextTrainingEntered.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getSherlockActivity(),
						EventDetailActivity.class);
				intent.putExtra(KeyConstants.KEY_BEANDATA, m_nextTraining);
				startActivity(intent);
			}
		});

		m_pointsTextView = (TextView) m_root
				.findViewById(R.id.pointsValueTextView);
		m_playedTextView = (TextView) m_root
				.findViewById(R.id.playedValueTextView);
		m_wonTextView = (TextView) m_root.findViewById(R.id.wonValueTextView);
		m_drawTextView = (TextView) m_root.findViewById(R.id.drawValueTextView);
		m_lostTextView = (TextView) m_root.findViewById(R.id.lostValueTextView);
		m_scoredTextView = (TextView) m_root
				.findViewById(R.id.scoredValueTextView);
		m_againstTextView = (TextView) m_root
				.findViewById(R.id.againstValueTextView);

		updateStatsTable();

		m_rosterButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getSherlockActivity(),
						RosterActivity.class);
				startActivity(intent);
			}
		});

		m_matchesButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startAddMatchListActivity();
			}
		});

		m_trainingButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startAddEventListActivity();
			}
		});

		MyTeamManagerActivity.getBus().register(this);

		return m_root;
	}

	public void updateStatsTable() {
		m_pointsTextView.setText(""
				+ SettingsManager.getInstance(getSherlockActivity())
						.getPoints());
		m_playedTextView.setText(""
				+ SettingsManager.getInstance(getSherlockActivity())
						.getPlayed());
		m_wonTextView.setText(""
				+ SettingsManager.getInstance(getSherlockActivity()).getWon());
		m_drawTextView.setText(""
				+ SettingsManager.getInstance(getSherlockActivity()).getDraw());
		m_lostTextView.setText(""
				+ SettingsManager.getInstance(getSherlockActivity()).getLost());
		m_scoredTextView.setText(""
				+ SettingsManager.getInstance(getSherlockActivity())
						.getScored());
		m_againstTextView.setText(""
				+ SettingsManager.getInstance(getSherlockActivity())
						.getAgainst());
	}

	public void startAddMatchListActivity() {
		Intent intent = new Intent(getSherlockActivity(),
				MatchesListActivity.class);
		startActivity(intent);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		refreshInfoHomePage();
	}

	public void refreshInfoHomePage() {
		new AsyncTask<Void, Void, String[]>() {

			@Override
			protected String[] doInBackground(Void... params) {

				loadNextMatch();
				loadNextTraining();

				return null;
			}

			@Override
			protected void onPostExecute(String[] result) {
				changeMatchString();
				changeEventString();
			}

		}.execute();
	}

	@Subscribe
	public void matchesChanged(MatchListChanged event) {
		refreshInfoHomePage();
	}

	@Subscribe
	public void eventsChanged(EventsListChanged event) {
		refreshInfoHomePage();
	}

	@Override
	public void button2Pressed(int alertId) {

	}

	@Override
	public void button3Pressed(int alertId) {

	}

	@Override
	public void button1Pressed(int alertId) {

	}

	public void changeMatchString() {
		if (m_nextMatch != null) {
			String matchString = m_nextMatch
					.getMatchString(getSherlockActivity());

			if (m_nextMatch.getTimestamp() != -1) {
				matchString += "\n"
						+ DateTimeUtil.getDateFrom(m_nextMatch.getTimestamp(),
								getSherlockActivity());
			}

			if (m_nextMatch.getTimestamp() > 0) {
				matchString += " - "
						+ DateTimeUtil.getTimeStringFrom(m_nextMatch
								.getTimestamp());
			}

			m_buttonNextMatchEntered.setText(matchString);

			m_buttonNextMatchEntered.setVisibility(View.VISIBLE);
			m_buttonNextMatch.setVisibility(View.GONE);

		} else {
			// m_nextMatchTextView.setText(getString(R.string.msg_no_next_match_available));

			m_buttonNextMatchEntered.setVisibility(View.GONE);
			m_buttonNextMatch.setVisibility(View.VISIBLE);
		}
	}

	public void changeEventString() {
		if (m_nextTraining != null) {
			String trainingString = DateTimeUtil.getDateFrom(
					m_nextTraining.getTimestamp(), getSherlockActivity());

			if (m_nextTraining.getTimestamp() > 0) {
				trainingString += "\n"
						+ DateTimeUtil.getTimeStringFrom(m_nextTraining
								.getTimestamp());
			}

			m_buttonNextTrainingEntered.setText(trainingString);

			m_buttonNextTrainingEntered.setVisibility(View.VISIBLE);
			m_buttonNextEvent.setVisibility(View.GONE);

		} else {
			// m_nextTrainingTextView.setText(getString(R.string.msg_no_next_training_available));

			m_buttonNextTrainingEntered.setVisibility(View.GONE);
			m_buttonNextEvent.setVisibility(View.VISIBLE);
		}
	}

	public void loadNextMatch() {
		ArrayList<MatchBean> matches = (ArrayList<MatchBean>) DBManager
				.getInstance()
				.getListOfBeansWhere(
						new MatchBean(),
						"timestamp = -1 or timestamp > " + new Date().getTime(),
						false, "1");
		if (matches != null && matches.size() > 0) {
			m_nextMatch = matches.get(0);
		} else {
			m_nextMatch = null;
		}
	}

	public void loadNextTraining() {
		ArrayList<EventBean> events = (ArrayList<EventBean>) DBManager
				.getInstance()
				.getListOfBeansWhere(
						new EventBean(),
						"timestamp = -1 or timestamp > " + new Date().getTime(),
						false, "1");
		if (events != null && events.size() > 0) {
			m_nextTraining = events.get(0);
		} else {
			m_nextTraining = null;
		}
	}

	public void startAddEventListActivity() {
		Intent intent = new Intent(getSherlockActivity(),
				EventsListActivity.class);
		startActivity(intent);
	}

	@Subscribe
	public void resultChanged(ResultEnteredEvent event) {
		int[] variations = event.getVariationsForStats();

		SettingsManager.getInstance(getSherlockActivity()).setPoints(
				SettingsManager.getInstance(getSherlockActivity()).getPoints()
						+ variations[0]);
		SettingsManager.getInstance(getSherlockActivity()).setPlayed(
				SettingsManager.getInstance(getSherlockActivity()).getPlayed()
						+ variations[1]);
		SettingsManager.getInstance(getSherlockActivity()).setWon(
				SettingsManager.getInstance(getSherlockActivity()).getWon()
						+ variations[2]);
		SettingsManager.getInstance(getSherlockActivity()).setDraw(
				SettingsManager.getInstance(getSherlockActivity()).getDraw()
						+ variations[3]);
		SettingsManager.getInstance(getSherlockActivity()).setLost(
				SettingsManager.getInstance(getSherlockActivity()).getLost()
						+ variations[4]);
		SettingsManager.getInstance(getSherlockActivity()).setScored(
				SettingsManager.getInstance(getSherlockActivity()).getScored()
						+ variations[5]);
		SettingsManager.getInstance(getSherlockActivity()).setAgainst(
				SettingsManager.getInstance(getSherlockActivity()).getAgainst()
						+ variations[6]);

		updateStatsTable();
	}

	@Subscribe
	public void matchListChanged(EventOrMatchChanged matchAdded) {
		refreshInfoHomePage();
	}

	@Override
	public void checkboxChanged(boolean isSelectAll) {
		if (isSelectAll) {
			PlayerBean selectAllPlayer = (PlayerBean) m_playersWithPhoneOrEmail
					.get(0);

			PlayerBean player;
			for (BaseBean bean : m_playersWithPhoneOrEmail) {
				player = (PlayerBean) bean;
				if (!player.isFakeSelectAll()) {
					if ( selectAllPlayer.isRecipient() ) {
						player.setIsRecipient(true);
					}
					else {
						player.setIsRecipient(false);
					}
				}
			}
			
			m_recipientListAdapter.notifyDataSetChanged();
			
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == AlertDialog.BUTTON_POSITIVE) {
			Intent intent = new Intent(getSherlockActivity(),
					SendMessageActivity.class);

			PlayerBean player = null;
			ArrayList<PlayerBean> selectedRecipients = new ArrayList<PlayerBean>();
			for (BaseBean bean : m_playersWithPhoneOrEmail) {
				player = (PlayerBean) bean;
				if (player.isRecipient() && !player.isFakeSelectAll()) {
					selectedRecipients.add(player);
				}
			}

			if (selectedRecipients.size() > 0) {
				intent.putExtra(KeyConstants.KEY_SELECTED_RECIPIENT,
						selectedRecipients);
				startActivity(intent);
			}

		}
	}

}
