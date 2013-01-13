package com.myteammanager.ui.fragments;

import java.util.ArrayList;

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
import com.myteammanager.R;
import com.myteammanager.adapter.PlayerListAdapterWithCheckbox;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.ConvocationBean;
import com.myteammanager.beans.MatchBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.beans.comparators.RosterComparator;
import com.myteammanager.storage.DBManager;
import com.myteammanager.ui.PlayerCheckboxListener;
import com.myteammanager.ui.phone.EditPlayerInfoActivity;
import com.myteammanager.util.KeyConstants;

public class PlayerListForTeamLineupFragment extends RosterFragment {

	private static final int MSG_SHOWMESSAGENOMOREPLAYER = 1;

	private static final String EDIT_CONVOCATION_TAG = "edit_team_lineup";
	private static final String CONVOCATED_TAG = "selected";

	private MatchBean m_match;

	private boolean m_convocations = false;
	private boolean m_definedLineUp = false;

	private ArrayList<PlayerBean> m_playersStillToChoose;
	private ArrayList<PlayerBean> m_playersAlreadyChosen;

	private View m_root;

	final Handler m_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MSG_SHOWMESSAGENOMOREPLAYER) {
				Log.d(LOG_TAG, "Msg no player received");
				TextView textViewNoMorePlayers = (TextView) m_root.findViewById(R.id.textViewMessageNoData);
				textViewNoMorePlayers.setText(R.string.msg_no_players_to_select_for_lineup);
				textViewNoMorePlayers.setVisibility(TextView.VISIBLE);

				m_listView.setVisibility(ListView.GONE);
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle bundle = getSherlockActivity().getIntent().getExtras();
		if (bundle != null) {
			m_playersStillToChoose = (ArrayList<PlayerBean>) bundle.get(KeyConstants.KEY_PLAYERS_LIST);
			m_match = (MatchBean) bundle.get(KeyConstants.KEY_MATCH);
			m_playersAlreadyChosen = (ArrayList<PlayerBean>) bundle.get(KeyConstants.KEY_PLAYERS_ALREADY_IN_THE_LINEUP);
		}

		m_root = inflater.inflate(R.layout.fragment_base_list, null);
		m_listView = (ListView) m_root.findViewById(R.id.list);
		registerForContextMenu(m_listView);
		setHasOptionsMenu(true);
		return m_root;

	}

	@Override
	protected void init() {
		Log.d(LOG_TAG, "init");


	}

	@Override
	protected ArrayList<? extends BaseBean> getData() {

		if (m_playersStillToChoose.size() == 0) {
			sendNoMorePlayersMessage();
		}

		return m_playersStillToChoose;

	}

	public void sendNoMorePlayersMessage() {
		Message msg = m_handler.obtainMessage();
		msg.what = MSG_SHOWMESSAGENOMOREPLAYER;
		m_handler.sendMessage(msg);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		PlayerBean player = (PlayerBean) m_itemsList.get(position);
		Intent intent = new Intent();

		intent.putExtra(KeyConstants.KEY_PLAYER, player);

		removeSpearatorBeanFromList();
		intent.putExtra(KeyConstants.KEY_PLAYERS_LIST, m_itemsList);

		getSherlockActivity().setResult(KeyConstants.RESULT_PLAYER_FOR_LINEUP_CHOSEN, intent);
		getSherlockActivity().finish();
	}

	protected void refreshItemsAfterLoadedNewData(ArrayList<BaseBean> newData) {
		m_itemsList.clear();
		m_itemsList.addAll(newData);

		addSectionHeadersToItemsList();

		sortList(new PlayerBean().getComparator());

	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	}

}
