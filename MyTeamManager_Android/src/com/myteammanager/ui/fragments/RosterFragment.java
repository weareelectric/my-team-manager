package com.myteammanager.ui.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.myteammanager.R;
import com.myteammanager.adapter.RosterListAdapter;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.beans.SeparatorBean;
import com.myteammanager.specializedStorage.MyTeamManagerDBManager;
import com.myteammanager.storage.DBManager;
import com.myteammanager.ui.phone.AddPlayerInfoFromRosterActivity;
import com.myteammanager.ui.phone.EditPlayerInfoActivity;
import com.myteammanager.ui.quickaction.ActionItem;
import com.myteammanager.ui.quickaction.QuickAction;
import com.myteammanager.util.KeyConstants;
import com.myteammanager.util.PlayerAndroidUtil;

public class RosterFragment extends BaseListFragmentWithSectionHeaders {

	private static final int ID_DELETE_PLAYER = 1;

	@Override
	protected void init() {
		m_isFastScrolledEnabled = false;

		m_quickAction = new QuickAction(getSherlockActivity());
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
	protected ArrayAdapter<? extends BaseBean> initAdapter() {
		m_itemsList.clear();
		return new RosterListAdapter(getActivity(), R.layout.list_roster_item, m_itemsList);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		requestData();
	}

	@Override
	protected ArrayList<? extends BaseBean> getData() {
		return MyTeamManagerDBManager.getInstance().getListOfBeans(new PlayerBean(), true);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		PlayerBean player = (PlayerBean) m_itemsList.get(position);
		Intent intent = new Intent(getSherlockActivity(), EditPlayerInfoActivity.class);
		intent.putExtra(KeyConstants.KEY_BEANDATA, player);
		startActivityForResult(intent, KeyConstants.CODE_BEAN_CHANGE);
	}

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
			Intent intent = new Intent(getSherlockActivity(), AddPlayerInfoFromRosterActivity.class);
			startActivityForResult(intent, KeyConstants.CODE_BEAN_CHANGE);
			break;
		}
		return super.onOptionsItemSelected(item);
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
		return PlayerAndroidUtil.getRoleString(getSherlockActivity(), playerBean.getRole());
	}

	@Override
	public void button1Pressed(int alertId) {
		Log.d(LOG_TAG, "actionId: " + m_itemSelectedForContextMenu);
		PlayerBean player = (PlayerBean) m_itemsList.get(m_itemSelectedForContextMenu);
		DBManager.getInstance().deleteBean(player);
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

}
