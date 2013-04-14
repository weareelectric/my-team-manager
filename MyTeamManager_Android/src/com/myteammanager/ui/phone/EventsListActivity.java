package com.myteammanager.ui.phone;

import android.content.Context;
import org.holoeverywhere.app.Fragment;

import com.myteammanager.R;
import com.myteammanager.ui.fragments.EventsListFragment;
import com.myteammanager.ui.fragments.RosterFragment;

public class EventsListActivity extends BaseSinglePaneActivity {

	private final static String LOG_TAG = EventsListActivity.class.getName();

	private EventsListFragment m_eventsFragment;

	@Override
	protected Fragment onCreatePane() {
		setTitle(R.string.title_events);
		m_eventsFragment = new EventsListFragment();
		return m_eventsFragment;
	}

	@Override
	protected void init() {

	}


    

}
