package com.myteammanager.ui.fragments;

import java.util.ArrayList;
import java.util.Collections;

import android.R.array;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.myteammanager.MyTeamManagerActivity;
import com.myteammanager.R;
import com.myteammanager.adapter.ConvocatedPlayerListAdapterWithCheckbox;
import com.myteammanager.adapter.RosterListAdapter;
import com.myteammanager.adapter.ScorersListAdapter;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.ConvocationBean;
import com.myteammanager.beans.LineupBean;
import com.myteammanager.beans.MatchBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.beans.ScorerBean;
import com.myteammanager.beans.SeparatorBean;
import com.myteammanager.beans.comparators.RosterComparator;
import com.myteammanager.events.ScorersChangeEvent;
import com.myteammanager.storage.DBManager;
import com.myteammanager.ui.CheckboxListener;
import com.myteammanager.ui.phone.AddEventInfoActivity;
import com.myteammanager.ui.phone.EditPlayerInfoActivity;
import com.myteammanager.util.KeyConstants;
import com.myteammanager.util.PlayerAndroidUtil;

public class PlayerListForScorerChoice extends RosterFragment {

	private MatchBean m_match;
	private boolean m_isUpdate = false;

	private View m_root;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle bundle = getSherlockActivity().getIntent().getExtras();
		if (bundle != null) {
			m_match = (MatchBean) bundle.get(KeyConstants.KEY_MATCH);
		}

		m_root = inflater.inflate(R.layout.fragment_base_list, null);
		m_listView = (ListView) m_root.findViewById(R.id.list);

		return m_root;

	}

	@Override
	protected ArrayList<? extends BaseBean> getData() {
		
		ArrayList<ScorerBean> scorers = (ArrayList<ScorerBean>) DBManager.getInstance()
				.getListOfBeansWhere(new ScorerBean(), "match = " + m_match.getId(), true);
		m_isUpdate = scorers.size() > 0;

		// The scorer needs to be chosen in the selected player lists or in the convocated players list

		ArrayList<PlayerBean> listOfPlayers = new ArrayList<PlayerBean>();

		ArrayList<LineupBean> alreadyStoredLineupPlayers = (ArrayList<LineupBean>) DBManager.getInstance().getListOfBeansWhere(new LineupBean(), "match = " + m_match.getId(), false);
		int size = 0;
		if (alreadyStoredLineupPlayers != null && (size = alreadyStoredLineupPlayers.size()) > 0) {
			LineupBean lineupPlayer = null;
			Log.d(LOG_TAG, "Size of chosen player: " + size);
			for (int k = 0; k < size; k++) {
				lineupPlayer = alreadyStoredLineupPlayers.get(k);

				if ( !listOfPlayers.contains(lineupPlayer.getPlayer())) {
					listOfPlayers.add(lineupPlayer.getPlayer());
					Log.d(LOG_TAG, "lineupPlayer.getPlayer(): " + lineupPlayer.getPlayer().getSurnameAndName(false, getSherlockActivity()));
				}
				

				
			}

		} else if (m_match.getNumberOfPlayerConvocated() > 0) {
			ArrayList<ConvocationBean> convocations = (ArrayList<ConvocationBean>) DBManager.getInstance().getListOfBeansWhere(new ConvocationBean(), "match = " + m_match.getId(), false);

			size = convocations.size();

			ConvocationBean convocation = null;
			for (int k = 0; k < size; k++) {
				convocation = (ConvocationBean) convocations.get(k);

				if ( !listOfPlayers.contains(convocation.getPlayer())) {
					listOfPlayers.add(convocation.getPlayer());
				}
			}
		} else {
			if ( m_isUpdate ) {
				listOfPlayers = (ArrayList<PlayerBean>) DBManager.getInstance().getListOfBeans(
						new PlayerBean(), false);
			}
			else {
				listOfPlayers = (ArrayList<PlayerBean>) DBManager.getInstance().getListOfBeansWhere(
						new PlayerBean(), "isDeleted=0", false);
			}
		}



		for (ScorerBean scorer : scorers) {
			PlayerBean player = scorer.getPlayer();
			Log.d(LOG_TAG, "Player that scored is: " + scorer.getId());
			int indexOfPlayer = listOfPlayers.indexOf(player);
			if (indexOfPlayer != -1) {
				PlayerBean playerFromList = listOfPlayers.get(indexOfPlayer);
				playerFromList.setGoalScoredInTheMatch(scorer.getScoredGoal());
			}

		}

		Collections.sort(listOfPlayers, new PlayerBean().getComparator());

		return listOfPlayers;
	}

	@Override
	protected ArrayAdapter<? extends BaseBean> initAdapter() {
		m_itemsList.clear();
		return new ScorersListAdapter(getActivity(), R.layout.list_scores_item, m_itemsList);
	}

	@Override
	protected void init() {
		Log.d(LOG_TAG, "init");

		setHasOptionsMenu(true);

	}

	@Override
	protected void addSectionHeadersToItemsList() {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.edit_scorers, menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_save_scorers:
			DBManager.getInstance().deleteBeanWithWhere(new ScorerBean(), "match=" + m_match.getId());
			ArrayList<ScorerBean> scorers = new ArrayList<ScorerBean>();

			int size = m_itemsList.size();
			for (int k = 0; k < size; k++) {
				PlayerBean player = (PlayerBean) m_itemsList.get(k);
				Log.d(LOG_TAG, "Scored by " + player.getSurnameAndName(true, getSherlockActivity()) + ": " + player.getGoalScoredInTheMatch());
				if (player.getGoalScoredInTheMatch() > 0) {
					ScorerBean scorer = new ScorerBean();
					scorer.setMatch(m_match);
					scorer.setPlayer(player);
					scorer.setScoredGoal(player.getGoalScoredInTheMatch());
					scorers.add(scorer);
				}

			}

			// Activity will be closed at the end of the insert
			MyTeamManagerActivity.getBus().post(new ScorersChangeEvent(scorers));
			insertBeans(scorers, m_isUpdate, true);
			break;

		}
		return super.onOptionsItemSelected(item);
	}

}
