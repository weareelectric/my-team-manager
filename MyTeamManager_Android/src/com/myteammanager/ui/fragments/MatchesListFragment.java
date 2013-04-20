package com.myteammanager.ui.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import org.holoeverywhere.widget.AdapterView;
import org.holoeverywhere.widget.ListView;

import android.widget.ArrayAdapter;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.myteammanager.MyTeamManagerActivity;
import com.myteammanager.R;
import com.myteammanager.adapter.MatchListAdapter;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.MatchBean;
import com.myteammanager.events.EventOrMatchChanged;
import com.myteammanager.events.EventsListChanged;
import com.myteammanager.events.MatchListChanged;
import com.myteammanager.events.ResultEnteredEvent;
import com.myteammanager.storage.DBManager;
import com.myteammanager.ui.phone.AddEventInfoActivity;
import com.myteammanager.ui.phone.MatchDetailActivity;
import com.myteammanager.ui.quickaction.ActionItem;
import com.myteammanager.ui.quickaction.QuickAction;
import com.myteammanager.util.DeleteMatchManager;
import com.myteammanager.util.KeyConstants;
import com.squareup.otto.Subscribe;

public class MatchesListFragment extends BaseListFragmentWithSectionHeaders {

	private static final int ID_DELETE_MATCH = 1;

	@Override
	protected void init() {
		m_showNoDataMessage = true;
		m_quickAction = new QuickAction(getActivity());
		ActionItem deleteEvent = new ActionItem(ID_DELETE_MATCH, getResources().getString(R.string.delete), getResources()
				.getDrawable(android.R.drawable.ic_menu_delete));
		deleteEvent.setSticky(true);
		m_quickAction.addActionItem(deleteEvent);

		//setup the action item click listener
		m_quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(QuickAction quickAction, int pos, int actionId) {
				ActionItem actionItem = quickAction.getActionItem(pos);

				if (actionId == ID_DELETE_MATCH) {
					m_quickAction.dismiss();
					showDeleteConfirmation();
				}
			}
		});

		MyTeamManagerActivity.getBus().register(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		registerBroadcasrReceiverForBeanEvents();
	}

	@Override
	protected void addSectionHeadersToItemsList() {

	}

	@Override
	protected ArrayAdapter<? extends BaseBean> initAdapter() {
		m_itemsList.clear();
		return new MatchListAdapter(getActivity(), R.layout.list_match_item, m_itemsList);
	}

	@Override
	protected ArrayList<? extends BaseBean> getData() {
		return DBManager.getInstance().getListOfBeans(new MatchBean(), false);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		//		MatchBean match = (MatchBean) m_itemsList.get(position);
		//		Intent intent = new Intent(getActivity(),
		//				AddEventInfoActivity.class);
		//		intent.putExtra(KEY_BEANDATA, match);
		//		intent.putExtra(MatchesListFragment.KEY_EVENT_OR_MATCH, false);
		//		startActivityForResult(intent, CODE_BEAN_CHANGE);

		MatchBean match = (MatchBean) m_itemsList.get(position);
		Intent intent = new Intent(getActivity(), MatchDetailActivity.class);
		intent.putExtra(KeyConstants.KEY_BEANDATA, match);
		startActivityForResult(intent, KeyConstants.CODE_BEAN_CHANGE);
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.matches_list, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_add_match:
			addMatch();
			break;
		}
		return true;
	}

	protected void addMatch() {
		Intent intent = new Intent(getActivity(), AddEventInfoActivity.class);
		intent.putExtra(KeyConstants.KEY_EVENT_OR_MATCH, false);
		startActivity(intent);
	}

	@Override
	public void button1Pressed(int alertId) {
		Log.d(LOG_TAG, "actionId: " + m_itemSelectedForContextMenu);
		MatchBean match = (MatchBean) m_itemsList.get(m_itemSelectedForContextMenu);
		DeleteMatchManager.deleteMatch(match, getActivity());
		deleteBeanInTheList(match);
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
	public void addBeanToTheListAndRefresh(BaseBean mainBean) {
		super.addBeanToTheListAndRefresh(mainBean);
		MyTeamManagerActivity.getBus().post(new MatchListChanged());
	}

	@Override
	public void addBeanToTheListAndRefresh(BaseBean mainBean, ArrayList<BaseBean> beans) {
		super.addBeanToTheListAndRefresh(mainBean, beans);
		MyTeamManagerActivity.getBus().post(new MatchListChanged());
	}

	@Override
	public void editBeanInTheList(BaseBean mainBean) {
		super.editBeanInTheList(mainBean);
		MyTeamManagerActivity.getBus().post(new MatchListChanged());
	}

	@Override
	public void editBeanInTheList(BaseBean mainBean, ArrayList<BaseBean> beans) {
		super.editBeanInTheList(mainBean, beans);
		MyTeamManagerActivity.getBus().post(new MatchListChanged());
	}

	@Override
	public void deleteBeanInTheList(BaseBean mainBean) {
		super.deleteBeanInTheList(mainBean);
		MyTeamManagerActivity.getBus().post(new MatchListChanged());
	}

	@Override
	public void deleteBeanInTheList(BaseBean mainBean, ArrayList<BaseBean> beans) {
		super.deleteBeanInTheList(mainBean, beans);
		MyTeamManagerActivity.getBus().post(new MatchListChanged());
	}

	@Subscribe
	public void resultChanged(ResultEnteredEvent event) {
		super.editBeanInTheList(event.getMatch());
	}
	
	@Subscribe
	public void resultChanged(EventOrMatchChanged event) {
		super.editBeanInTheList(event.getMatch());
	}
	

	@Override
	protected void noDataButtonAction() {
		addMatch();
	}

	@Override
	protected String getMessageForNoData() {
		return getString(R.string.msg_no_match_in_list);
	}

	@Override
	protected String getMessageForNoDataButton() {
		return getString(R.string.btn_add_match);
	}

}
