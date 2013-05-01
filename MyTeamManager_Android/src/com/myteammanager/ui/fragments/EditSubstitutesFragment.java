package com.myteammanager.ui.fragments;

import java.util.ArrayList;

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
import android.widget.ArrayAdapter;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.ListView;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.myteammanager.R;
import com.myteammanager.adapter.ConvocatedPlayerListAdapterWithCheckbox;
import com.myteammanager.adapter.SubstitutesAdapter;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.ConvocationBean;
import com.myteammanager.beans.LineupBean;
import com.myteammanager.beans.MatchBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.storage.DBManager;
import com.myteammanager.ui.CheckboxListener;
import com.myteammanager.util.KeyConstants;

public class EditSubstitutesFragment extends EditConvocationFragment {

	private View m_root;
	private ArrayList<PlayerBean> m_playersStillToChoose;
	private ArrayList<PlayerBean> m_playersAlreadyChosen;

	private MatchBean m_match;

	public EditSubstitutesFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle bundle = getActivity().getIntent().getExtras();
		if (bundle != null) {
			m_playersStillToChoose = (ArrayList<PlayerBean>) bundle.get(KeyConstants.KEY_PLAYERS_LIST);
			m_match = (MatchBean) bundle.get(KeyConstants.KEY_MATCH);
			m_playersAlreadyChosen = (ArrayList<PlayerBean>) bundle.get(KeyConstants.KEY_PLAYERS_ALREADY_IN_THE_LINEUP);
		}

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected ArrayAdapter<? extends BaseBean> initAdapter() {
		m_itemsList.clear();
		return new SubstitutesAdapter(getActivity(), R.layout.list_with_checkbox, m_itemsList, this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (m_root != null) {
			m_child1.setVisibility(View.GONE);
			// m_child2.setVisibility(View.GONE);

		}

	}

	public void updateCountersForConvocations() {
		// Keep the empty method to avoid the counters from parent class appears
	}
	

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.edit_substitutions, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_save_substitutions:
			ArrayList<LineupBean> substitutes = new ArrayList<LineupBean>();
			ArrayList<PlayerBean> substitutePlayers = new ArrayList<PlayerBean>();
			ArrayList<PlayerBean> notChosenPlayers = new ArrayList<PlayerBean>();
			PlayerBean player = null;
			LineupBean lineupBean = null;
			Object obj = null;
			int size = m_itemsList.size();

			for (int i = 0; i < size; i++) {
				obj = m_itemsList.get(i);
				if (obj instanceof PlayerBean) {
					player = (PlayerBean) obj;

					if (player.isConvocated()) {
						lineupBean = new LineupBean();
						lineupBean.setMatch(m_match);
						lineupBean.setPlayer(player);
						player.setOnTheBench(true);
						substitutePlayers.add(player);
						lineupBean.setOnTheBench(LineupBean.ON_THE_BENCH);
						substitutes.add(lineupBean);
					} else {
						notChosenPlayers.add(player);
					}
				}

			}

			// First, delete the previous entries in the db
			DBManager.getInstance().deleteBeanWithWhere(new LineupBean(),
					"match=" + m_match.getId() + " and onTheBench=1");

			// Store the new subs and the number of convocated players in the match object
			insertBeans(substitutes, false, true);

			Intent intent = new Intent();
			intent.putExtra(KeyConstants.KEY_PLAYERS_LIST, substitutePlayers);
			intent.putExtra(KeyConstants.KEY_NOT_CHOSEN_PLAYERS, notChosenPlayers);

			// finish of activity is called automatically after the writing operations in the database
			getActivity().setResult(KeyConstants.RESULT_PLAYERS_ONTHEBENCH_CHOSEN, intent);
			break;
		}
		return true;
	}

	@Override
	protected ArrayList<? extends BaseBean> getData() {

		return m_playersStillToChoose;
	}

}
