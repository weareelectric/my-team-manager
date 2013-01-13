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

public class PostMatchFragment extends BaseFragment implements FacebookResponseListener {

	private String LOG_TAG = PostMatchFragment.class.getName();

	protected LinearLayout m_root;
	private MenuItem m_menuItem1;
	private EditText m_msgEditText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		m_root = (LinearLayout) inflater.inflate(R.layout.fragment_post_match_details, null, false);

		Bundle extra = getSherlockActivity().getIntent().getExtras();
		String msg = extra.getString(KeyConstants.KEY_MATCH_DETAIL_TO_POST);

		m_msgEditText = (EditText) m_root.findViewById(R.id.editTextMatchInfo);
		m_msgEditText.setText(msg);

		setHasOptionsMenu(true);

		return m_root;
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.post_match, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_post_match:
			showProgressDialog(getResources().getString(R.string.dialog_waiting_sending_data));
			FacebookManager.getInstance().postMessage(m_msgEditText.getText().toString(),
					SettingsManager.getInstance(getSherlockActivity()).getFacebookPageId(), getSherlockActivity(),
					new FacebookStatusPublishedEvent(this));
			break;
		}

		return true;
	}

	@Override
	public void button2Pressed(int alertId) {

	}

	@Override
	public void button3Pressed(int alertId) {

	}

	@Override
	public void button1Pressed(int alertId) {

	}

	@Override
	public void postFBResponse(FacebookResponseEvent event) {
		FacebookStatusPublishedEvent postEvent = (FacebookStatusPublishedEvent) event;

		FacebookError error = postEvent.getFacebookError();
		cancelProgressDialog();
		if (error != null) {
			getSherlockActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(getSherlockActivity(), R.string.toast_facebook_error_post_status, Toast.LENGTH_LONG).show();
				}
			});

		} else {
			getSherlockActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(getSherlockActivity(), R.string.toast_facebook_status_posted, Toast.LENGTH_LONG).show();
					getSherlockActivity().finish();
				}
			});

		}
	}
}
