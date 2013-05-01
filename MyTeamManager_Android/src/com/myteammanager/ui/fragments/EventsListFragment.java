package com.myteammanager.ui.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import com.myteammanager.util.Log;
import android.view.View;
import org.holoeverywhere.widget.AdapterView;
import org.holoeverywhere.widget.ListView;

import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.myteammanager.MyTeamManagerActivity;
import com.myteammanager.R;
import com.myteammanager.adapter.EventListAdapter;
import com.myteammanager.adapter.RosterListAdapter;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.EventBean;
import com.myteammanager.beans.MatchBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.events.EventsListChanged;
import com.myteammanager.events.MatchListChanged;
import com.myteammanager.storage.DBManager;
import com.myteammanager.ui.phone.EditPlayerInfoActivity;
import com.myteammanager.ui.phone.EventDetailActivity;
import com.myteammanager.ui.phone.EventsListActivity;
import com.myteammanager.ui.phone.AddEventInfoActivity;
import com.myteammanager.ui.quickaction.ActionItem;
import com.myteammanager.ui.quickaction.QuickAction;
import com.myteammanager.util.EventDeleteConfirmationManager;
import com.myteammanager.util.KeyConstants;

public class EventsListFragment extends BaseListFragmentWithSectionHeaders {

	private static final int ID_DELETE_EVENT = 1;

	private static final int ALERT_DELETE_ALL_EVENT_ID = 1;

	@Override
	protected void init() {
		m_showNoDataMessage = true;
		m_quickAction = new QuickAction(getActivity());
		ActionItem deleteEvent = new ActionItem(ID_DELETE_EVENT, getResources().getString(R.string.delete), getResources()
				.getDrawable(android.R.drawable.ic_menu_delete));
		deleteEvent.setSticky(true);
		m_quickAction.addActionItem(deleteEvent);

		//setup the action item click listener
		m_quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(QuickAction quickAction, int pos, int actionId) {
				ActionItem actionItem = quickAction.getActionItem(pos);

				if (actionId == ID_DELETE_EVENT) {
					m_quickAction.dismiss();
					EventBean event = (EventBean) m_itemsList.get(m_itemSelectedForContextMenu);
					if (event.isEventLinkedToOthers()) {
						EventDeleteConfirmationManager.askEventConfirmation(event, EventsListFragment.this);
					} else {
						deleteEvent();
					}
				}
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		registerBroadcasrReceiverForBeanEvents();
	}

	@Override
	public String getDeleteConfirmationMsg() {
		Resources res = getActivity().getResources();
		return res.getString(R.string.alert_event_confirmation_msg);
	}

	@Override
	protected void addSectionHeadersToItemsList() {

	}

	@Override
	protected ArrayAdapter<? extends BaseBean> initAdapter() {
		m_itemsList.clear();
		return new EventListAdapter(getActivity(), R.layout.list_events_item, m_itemsList);
	}

	@Override
	protected ArrayList<? extends BaseBean> getData() {
		return DBManager.getInstance().getListOfBeans(new EventBean(), true);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		//		EventBean event = (EventBean) m_itemsList.get(position);
		//		Intent intent = new Intent(getActivity(),
		//				AddEventInfoActivity.class);
		//		intent.putExtra(KEY_BEANDATA, event);
		//		intent.putExtra(MatchesListFragment.KEY_EVENT_OR_MATCH, true);
		//		startActivityForResult(intent, CODE_BEAN_CHANGE);

		EventBean event = (EventBean) m_itemsList.get(position);
		Intent intent = new Intent(getActivity(), EventDetailActivity.class);
		intent.putExtra(KeyConstants.KEY_BEANDATA, event);
		startActivityForResult(intent, KeyConstants.CODE_BEAN_CHANGE);
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.events_list, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_add_event:
			addEvent();
			break;

		case R.id.menu_delete_all_events:
			showDeleteConfirmation(ALERT_DELETE_ALL_EVENT_ID, getString(R.string.alert_delete_all_events_confirmation_msg));

			break;
		}
		return true;
	}

	protected void addEvent() {
		Intent intent = new Intent(getActivity(), AddEventInfoActivity.class);
		intent.putExtra(KeyConstants.KEY_EVENT_OR_MATCH, true);
		startActivity(intent);
	}

	@Override
	public void button1Pressed(int alertId) {
		switch (alertId) {
		case ALERT_DELETE_ALL_EVENT_ID:
			DBManager.getInstance().deleteAllBeansFor(new EventBean());
			m_itemsList.clear();
			m_adapter.notifyDataSetChanged();
			manageViewDependingOnNbOfItems();
			MyTeamManagerActivity.getBus().post(new EventsListChanged());
			break;

		default:
			deleteEvent();
			break;
		}

	}

	public void deleteEvent() {
		Log.d(LOG_TAG, "actionId: " + m_itemSelectedForContextMenu);
		EventBean event = (EventBean) m_itemsList.get(m_itemSelectedForContextMenu);
		DBManager.getInstance().deleteBean(event);
		deleteBeanInTheList(event);
		m_itemSelectedForContextMenu = -1;
	}

	@Override
	public void button2Pressed(int alertId) {

		switch (alertId) {
		case ALERT_DELETE_ALL_EVENT_ID:
			// Not used at the moment
			break;

		default:
			Log.d(LOG_TAG, "actionId: " + m_itemSelectedForContextMenu);
			EventBean event = (EventBean) m_itemsList.get(m_itemSelectedForContextMenu);
			EventDeleteConfirmationManager.deleteAllLinkedEvents(event, getActivity(), false);
			// here we reload the list...we don't have all the objects to send the broadcast
			requestData();
			m_itemSelectedForContextMenu = -1;
			break;
		}

	}

	@Override
	public void button3Pressed(int alertId) {

	}

	@Override
	public void addBeanToTheListAndRefresh(BaseBean mainBean) {
		super.addBeanToTheListAndRefresh(mainBean);
		MyTeamManagerActivity.getBus().post(new EventsListChanged());
	}

	@Override
	public void addBeanToTheListAndRefresh(BaseBean mainBean, ArrayList<BaseBean> beans) {
		super.addBeanToTheListAndRefresh(mainBean, beans);
		MyTeamManagerActivity.getBus().post(new EventsListChanged());
	}

	@Override
	public void editBeanInTheList(BaseBean mainBean) {
		super.editBeanInTheList(mainBean);
		MyTeamManagerActivity.getBus().post(new EventsListChanged());
	}

	@Override
	public void editBeanInTheList(BaseBean mainBean, ArrayList<BaseBean> beans) {
		super.editBeanInTheList(mainBean, beans);
		MyTeamManagerActivity.getBus().post(new EventsListChanged());
	}

	@Override
	public void deleteBeanInTheList(BaseBean mainBean) {
		super.deleteBeanInTheList(mainBean);
		MyTeamManagerActivity.getBus().post(new EventsListChanged());
	}

	@Override
	public void deleteBeanInTheList(BaseBean mainBean, ArrayList<BaseBean> beans) {
		super.deleteBeanInTheList(mainBean, beans);
		MyTeamManagerActivity.getBus().post(new EventsListChanged());
	}
	

	@Override
	protected void noDataButtonAction() {
		addEvent();
	}

	@Override
	protected String getMessageForNoData() {
		return getString(R.string.msg_no_events_in_list);
	}

	@Override
	protected String getMessageForNoDataButton() {
		return getString(R.string.btn_add_event);
	}

}
