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
import com.myteammanager.adapter.SubstitutionsListAdapter;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.ConvocationBean;
import com.myteammanager.beans.LineupBean;
import com.myteammanager.beans.MatchBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.beans.ScorerBean;
import com.myteammanager.beans.SeparatorBean;
import com.myteammanager.beans.SubstitutionBean;
import com.myteammanager.beans.comparators.RosterComparator;
import com.myteammanager.events.ScorersChangeEvent;
import com.myteammanager.storage.DBManager;
import com.myteammanager.ui.CheckboxListener;
import com.myteammanager.ui.phone.AddEventInfoActivity;
import com.myteammanager.ui.phone.EditPlayerInfoActivity;
import com.myteammanager.util.KeyConstants;
import com.myteammanager.util.PlayerAndroidUtil;

public class PlayerListForMatchSubstitutions extends RosterFragment {

	private MatchBean m_match;
	private boolean m_isUpdate = false;
	private ArrayList<PlayerBean> m_titularPlayers;

	private View m_root;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle bundle = getSherlockActivity().getIntent().getExtras();
		if (bundle != null) {
			m_match = (MatchBean) bundle.get(KeyConstants.KEY_MATCH);
		}

		m_root = inflater.inflate(R.layout.fragment_base_list, null);
		m_listView = (ListView) m_root.findViewById(R.id.list);
		
		int size = 0;
		// Load the existent titular players. We need to load here to make it
		// available for the Adapter
		if (m_titularPlayers == null) {
			m_titularPlayers = new ArrayList<PlayerBean>();
			ArrayList<LineupBean> alreadyStoredLineupPlayers = (ArrayList<LineupBean>) DBManager
					.getInstance().getListOfBeansWhere(
							new LineupBean(), "match = " + m_match.getId() + " and onTheBench = 0",
							false);
			size = 0;
			if (alreadyStoredLineupPlayers != null
					&& (size = alreadyStoredLineupPlayers.size()) > 0) {
				LineupBean lineupPlayer = null;
				Log.d(LOG_TAG, "Size of chosen player: " + size);
				for (int k = 0; k < size; k++) {
					lineupPlayer = alreadyStoredLineupPlayers.get(k);
					Log.d(LOG_TAG, "Player in first eleven: " + lineupPlayer.getPlayer().getSurnameAndName(false));
					m_titularPlayers.add(lineupPlayer.getPlayer());
				}

			}
			
			// Add a none player to allow to let no substitutions
			PlayerBean player = new PlayerBean();
			player.setName("");
			player.setLastName("");
			m_titularPlayers.add(0, player);
		}

		return m_root;

	}
	

	@Override
	protected ArrayList<? extends BaseBean> getData() {
		
		// First: load all convocated players or all the player in the roster
		int size = 0;
		ArrayList<PlayerBean> playerNotInLineup = new ArrayList<PlayerBean>();
		if (m_match.getNumberOfPlayerConvocated() > 0) {
			ArrayList<ConvocationBean> convocations = (ArrayList<ConvocationBean>) DBManager.getInstance().getListOfBeansWhere(new ConvocationBean(), "match = " + m_match.getId(), false);

			size = convocations.size();

			ConvocationBean convocation = null;
			for (int k = 0; k < size; k++) {
				convocation = (ConvocationBean) convocations.get(k);

				playerNotInLineup.add(convocation.getPlayer());
			}
		} else {
			playerNotInLineup = (ArrayList<PlayerBean>) DBManager.getInstance().getListOfBeans(
					new PlayerBean(), false);
		}
		Log.d(LOG_TAG, "Player not in lineup: " + playerNotInLineup.size());
		
		

		
		for (PlayerBean titular : m_titularPlayers ) {
			playerNotInLineup.remove(titular);
		}
		
		// Load the existent substitutions and set the values
		ArrayList<SubstitutionBean> substitutions = (ArrayList<SubstitutionBean>) DBManager.getInstance().getListOfBeansWhere(new SubstitutionBean(), "match = " + m_match.getId(), false);
		for ( SubstitutionBean sub : substitutions ) {
			PlayerBean player = sub.getPlayerIn();
			PlayerBean playerOut = sub.getPlayerOut();
			
			if ( playerOut != null ) {
				int index= playerNotInLineup.indexOf(player);
				if ( index != -1 ) {
					playerNotInLineup.get(index).setReplacedPlayer(playerOut);
				}
			}
		}

		return playerNotInLineup;
	}

	@Override
	protected ArrayAdapter<? extends BaseBean> initAdapter() {
		m_itemsList.clear();
		return new SubstitutionsListAdapter(getActivity(), R.layout.list_substitutions_item, m_itemsList, m_titularPlayers);
	}

	@Override
	protected void init() {
		Log.d(LOG_TAG, "init");

		setHasOptionsMenu(true);
		m_isFastScrolledEnabled = true;

	}

	@Override
	protected void addSectionHeadersToItemsList() {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.edit_substitutions, menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_save_substitutions:
			DBManager.getInstance().deleteBeanWithWhere(new SubstitutionBean(), "match = " + m_match.getId());
			ArrayList<SubstitutionBean> substitutions = new ArrayList<SubstitutionBean>();

			int size = m_itemsList.size();
			for (int k = 0; k < size; k++) {
				PlayerBean player = (PlayerBean) m_itemsList.get(k);
				Log.d(LOG_TAG, "Store subst: " + player.getReplacedPlayer());
				if (player.getReplacedPlayer() != null) {
					SubstitutionBean substitution = new SubstitutionBean();
					substitution.setMatch(m_match);
					substitution.setPlayerIn(player);
					substitution.setPlayerOut(player.getReplacedPlayer());
					substitutions.add(substitution);
				}

			}

			// Activity will be closed at the end of the insert
			insertBeans(substitutions, false, true);
			break;

		}
		return super.onOptionsItemSelected(item);
	}

}
