package com.myteammanager.ui.fragments;

import java.util.ArrayList;
import java.util.Collections;

import android.R.array;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.FragmentTransaction;
import com.myteammanager.util.Log;
import org.holoeverywhere.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.holoeverywhere.widget.AdapterView;
import android.widget.ArrayAdapter;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;

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
	private ArrayList<PlayerBean> m_possiblePlayerReplaced;

	private View m_root;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle bundle = getActivity().getIntent().getExtras();
		if (bundle != null) {
			m_match = (MatchBean) bundle.get(KeyConstants.KEY_MATCH);
		}

		m_root = inflater.inflate(R.layout.fragment_base_list, null);
		m_listView = (ListView) m_root.findViewById(android.R.id.list);
		
		int size = 0;
		// Load the existent players for the match. We need to load here to make it
		// available for the Adapter
		if (m_possiblePlayerReplaced == null) {
			m_possiblePlayerReplaced = new ArrayList<PlayerBean>();
			ArrayList<LineupBean> playersInTheMatch = (ArrayList<LineupBean>) DBManager
					.getInstance().getListOfBeansWhere(
							new LineupBean(), "match = " + m_match.getId(),
							false);
			size = 0;
			if (playersInTheMatch != null
					&& (size = playersInTheMatch.size()) > 0) {
				LineupBean lineupPlayer = null;
				Log.d(LOG_TAG, "Size of chosen player: " + size);
				for (int k = 0; k < size; k++) {
					lineupPlayer = playersInTheMatch.get(k);
					PlayerBean player = lineupPlayer.getPlayer();
					player.setOnTheBench(lineupPlayer.getOnTheBench()==1);
					Log.d(LOG_TAG, "Player in first eleven: " + player.getSurnameAndName(false, getActivity()));
					m_possiblePlayerReplaced.add(player);
				}

			}
			
			// Add a none player to allow to let no substitutions
			PlayerBean player = new PlayerBean();
			player.setName("");
			player.setLastName("");
			m_possiblePlayerReplaced.add(0, player);
		}

		return m_root;

	}
	

	@Override
	protected ArrayList<? extends BaseBean> getData() {
		
		ArrayList<SubstitutionBean> substitutions = (ArrayList<SubstitutionBean>) DBManager.getInstance().getListOfBeansWhere(new SubstitutionBean(), "match = " + m_match.getId(), false);
		
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
			if ( substitutions != null && substitutions.size() > 0  ){
				playerNotInLineup = (ArrayList<PlayerBean>) DBManager.getInstance().getListOfBeans(
						new PlayerBean(), false);
			}
			else {
				playerNotInLineup = (ArrayList<PlayerBean>) DBManager.getInstance().getListOfBeansWhere(
						new PlayerBean(), "isDeleted=0", false);
			}

		}
		Log.d(LOG_TAG, "Player not in lineup: " + playerNotInLineup.size());
		
		

		
		for (PlayerBean player : m_possiblePlayerReplaced ) {
			if ( !player.isOnTheBench() ) {
				playerNotInLineup.remove(player);
			}
		}
		
		// Set the values for the already existant sostitution
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
		return new SubstitutionsListAdapter(getActivity(), R.layout.list_substitutions_item, m_itemsList, m_possiblePlayerReplaced);
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
		return true;
	}

}
