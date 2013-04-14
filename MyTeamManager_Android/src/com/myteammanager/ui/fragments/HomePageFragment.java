package com.myteammanager.ui.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import org.holoeverywhere.widget.AdapterView;
import org.holoeverywhere.widget.ListView;

import android.widget.ArrayAdapter;

import com.myteammanager.R;
import com.myteammanager.adapter.HomePageListAdapter;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.MenuBean;
import com.myteammanager.ui.phone.AddEventInfoActivity;
import com.myteammanager.ui.phone.EventsListActivity;
import com.myteammanager.ui.phone.MatchesListActivity;
import com.myteammanager.ui.phone.RosterActivity;

public class HomePageFragment extends BaseListFragment {
	
	private static final int HOME_PAGE_ROSTER_INDEX = 0;
	private static final int HOME_PAGE_FIXTURES_INDEX = 1;
	private static final int HOME_PAGE_EVENTS_INDEX = 2;
	
	@Override
	protected void init() {
		
	}

	@Override
	protected ArrayAdapter<? extends BaseBean> initAdapter() {
		m_itemsList.clear();
		m_itemsList.addAll(getData());
		return new HomePageListAdapter(getActivity(), R.layout.list_homepage_item, m_itemsList );
	}




    @Override
	protected ArrayList<? extends BaseBean> getData() {
    	String[] homePageList = getActivity().getResources().getStringArray(R.array.home_page_list);
		MenuBean menu1 = new MenuBean(homePageList[HOME_PAGE_ROSTER_INDEX]);
		MenuBean menu2 = new MenuBean(homePageList[HOME_PAGE_FIXTURES_INDEX]);
		MenuBean menu3 = new MenuBean(homePageList[HOME_PAGE_EVENTS_INDEX]);
		ArrayList<MenuBean> homepageList = new ArrayList<MenuBean>();
		homepageList.add(menu1);
		homepageList.add(menu2);
		homepageList.add(menu3);
		return homepageList;
	}

    @Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.d(LOG_TAG, "Clicked item position: " + position);
		switch (position) {
		case HOME_PAGE_ROSTER_INDEX:
			Intent intent = new Intent(getActivity(),
					RosterActivity.class);
			startActivity(intent);
			break;

		case HOME_PAGE_FIXTURES_INDEX:
//			intent = new Intent(getActivity(),
//					AddMatchInfoActivity.class);
//			startActivity(intent);
			
			intent = new Intent(getActivity(),
					MatchesListActivity.class);
			startActivity(intent);

			break;
			
		case HOME_PAGE_EVENTS_INDEX:
//			intent = new Intent(getActivity(),
//					AddMatchInfoActivity.class);
//			startActivity(intent);
			
			intent = new Intent(getActivity(),
					EventsListActivity.class);
			startActivity(intent);
			break;			
		}

	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		
	}

	@Override
	public void button1Pressed(int alertId) {
		
	}

	@Override
	public void button2Pressed(int alertId) {
		
	}

	@Override
	public void button3Pressed(int alertId) {
		
	}
    
    

}
