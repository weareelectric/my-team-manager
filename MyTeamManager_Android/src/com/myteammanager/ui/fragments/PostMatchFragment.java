package com.myteammanager.ui.fragments;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;
import com.myteammanager.R;
import com.myteammanager.data.FacebookManager;
import com.myteammanager.data.FacebookParser;
import com.myteammanager.events.FacebookResponseEvent;
import com.myteammanager.events.FacebookStatusPublishedEvent;
import com.myteammanager.listener.FacebookResponseListener;
import com.myteammanager.storage.SettingsManager;
import com.myteammanager.util.KeyConstants;

public class PostMatchFragment extends SendMessageFacebookFragment {

	private String LOG_TAG = PostMatchFragment.class.getName();

	protected LinearLayout m_root;
	private MenuItem m_menuItem1;


	@Override
	public void button2Pressed(int alertId) {

	}

	@Override
	public void button3Pressed(int alertId) {

	}

	@Override
	public void button1Pressed(int alertId) {

	}

	
}
