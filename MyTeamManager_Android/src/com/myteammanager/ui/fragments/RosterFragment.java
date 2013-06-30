package com.myteammanager.ui.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import com.myteammanager.util.Log;
import android.view.View;
import android.widget.AbsListView;
import org.holoeverywhere.widget.AdapterView;
import org.holoeverywhere.widget.ListView;

import android.widget.ArrayAdapter;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.myteammanager.R;
import com.myteammanager.adapter.RosterListAdapter;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.beans.SeparatorBean;
import com.myteammanager.beans.TeamBean;
import com.myteammanager.data.ParseObjectManager;
import com.myteammanager.specializedStorage.MyTeamManagerDBManager;
import com.myteammanager.storage.DBManager;
import com.myteammanager.storage.SettingsManager;
import com.myteammanager.ui.phone.AddPlayerInfoFromRosterActivity;
import com.myteammanager.ui.phone.EditPlayerInfoActivity;
import com.myteammanager.ui.quickaction.ActionItem;
import com.myteammanager.ui.quickaction.QuickAction;
import com.myteammanager.util.KeyConstants;
import com.myteammanager.util.PlayerAndroidUtil;
import com.parse.ParseException;

public class RosterFragment extends BaseListFragmentWithSectionHeaders {

	private static final int ID_DELETE_PLAYER = 1;

	@Override
	protected void init() {
		m_isFastScrolledEnabled = false;
		m_showNoDataMessage = true;

		m_quickAction = new QuickAction(getActivity());
		ActionItem deletePlayer = new ActionItem(ID_DELETE_PLAYER, getResources().getString(R.string.delete),
				getResources().getDrawable(android.R.drawable.ic_menu_delete));
		deletePlayer.setSticky(true);
		m_quickAction.addActionItem(deletePlayer);

		//setup the action item click listener
		m_quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(QuickAction quickAction, int pos, int actionId) {
				ActionItem actionItem = quickAction.getActionItem(pos);

				if (actionId == ID_DELETE_PLAYER) {
					m_quickAction.dismiss();
					showDeleteConfirmation();
				}
			}
		});
	}
	

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		PlayerBean player = (PlayerBean) m_itemsList.get(position);
		Intent intent = new Intent(getActivity(), EditPlayerInfoActivity.class);
		intent.putExtra(KeyConstants.KEY_BEANDATA, player);
		startActivityForResult(intent, KeyConstants.CODE_BEAN_CHANGE);
	}

	@Override
	protected ArrayAdapter<? extends BaseBean> initAdapter() {
		m_itemsList.clear();
		return new RosterListAdapter(getActivity(), R.layout.list_roster_item, m_itemsList);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	protected ArrayList<? extends BaseBean> getData() {
		return MyTeamManagerDBManager.getInstance().getListOfBeansWhere(new PlayerBean(), "isDeleted=0", true);
		// return MyTeamManagerDBManager.getInstance().getListOfBeans(new PlayerBean(), true);
	}
	
//	@Override
//	protected ArrayList<? extends BaseBean> getData() {
//		String userTeamName = SettingsManager.getInstance(getActivity()).getTeamName();
//		String userTeamParseId = SettingsManager.getInstance(getActivity()).getTeamParseId();
//		
//		ArrayList<PlayerBean> parseRosterList = null;
//		try {
//			parseRosterList = ParseObjectManager.getInstance().getParseRosterList(TeamBean.getParseObjectFor(userTeamParseId, userTeamName));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		return parseRosterList;
//	}


	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {

	}

	public void refreshList() {
		requestData();
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.roster, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_add_player:
			addPlayer();
			break;
		}
		return true;
	}

	protected void addPlayer() {
		Intent intent = new Intent(getActivity(), AddPlayerInfoFromRosterActivity.class);
		startActivityForResult(intent, KeyConstants.CODE_BEAN_CHANGE);
	}

	@Override
	protected void addSectionHeadersToItemsList() {
		ArrayList<BaseBean> list = new ArrayList<BaseBean>(m_itemsList);
		int size = list.size();
		int previousRole = -1;
		PlayerBean playerBean = null;
		int added = 0;
		Object obj = null;
		for (int k = 0; k < size; k++) {
			obj = list.get(k);
			if (obj instanceof PlayerBean) {
				playerBean = (PlayerBean) list.get(k);
				if (previousRole == -1 || previousRole != playerBean.getRole()) {
					m_itemsList.add(k + added,
							new SeparatorBean(getSeparatorString(playerBean)));
					added++;
				}
				previousRole = playerBean.getRole();
			}
		}
	}

	protected String getSeparatorString(PlayerBean playerBean) {
		return PlayerAndroidUtil.getRoleString(getActivity(), playerBean.getRole());
	}

	@Override
	public void button1Pressed(int alertId) {
		Log.d(LOG_TAG, "actionId: " + m_itemSelectedForContextMenu);
		PlayerBean player = (PlayerBean) m_itemsList.get(m_itemSelectedForContextMenu);
		// DBManager.getInstance().deleteBean(player);
		player.setIsDeleted(1);
		DBManager.getInstance().updateBean(player);
		deleteBeanInTheList(player);
		m_itemSelectedForContextMenu = -1;
	}

	@Override
	public void button2Pressed(int alertId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void button3Pressed(int alertId) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void noDataButtonAction() {
		addPlayer();
	}

	@Override
	protected String getMessageForNoData() {
		return getString(R.string.msg_no_players_in_roster);
	}

	@Override
	protected String getMessageForNoDataButton() {
		return getString(R.string.btn_add_player);
	}
	
	

}
