package com.myteammanager.ui.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.android.FacebookError;
import com.myteammanager.R;
import com.myteammanager.adapter.RecipientListAdapterWithCheckbox;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.data.FacebookManager;
import com.myteammanager.events.FacebookResponseEvent;
import com.myteammanager.events.FacebookStatusPublishedEvent;
import com.myteammanager.listener.FacebookResponseListener;
import com.myteammanager.specializedStorage.MyTeamManagerDBManager;
import com.myteammanager.storage.SettingsManager;
import com.myteammanager.ui.phone.AddEventInfoActivity;
import com.myteammanager.ui.phone.PostMatchDetailActivity;
import com.myteammanager.util.KeyConstants;
import com.myteammanager.util.StringUtil;

public class SendMessageFacebookFragment extends BaseFragment implements FacebookResponseListener {
	
	private String LOG_TAG = SendMessageFacebookFragment.class.getName();
	
	protected String m_msgText;
	protected View m_root;

	private EditText m_editTextMessage;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		m_root = inflater.inflate(R.layout.fragment_send_facebook_free, null);
		
		m_editTextMessage = (EditText)m_root.findViewById(R.id.editTextMessage);
		if (StringUtil.isNotEmpty(m_msgText)) {
			m_editTextMessage.setText(m_msgText);
		}
		
		setHasOptionsMenu(true);
		
		return m_root;
	}
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		Bundle extra = getSherlockActivity().getIntent().getExtras();
		if ( extra != null ) {
			m_msgText = (String)extra.get(KeyConstants.KEY_MSG_TEXT);
		}
		
		
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.post_message_on_facebook, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

			case R.id.menu_post_on_facebook:
				showProgressDialog(getResources().getString(R.string.dialog_waiting_sending_data));
				FacebookManager.getInstance().postMessage(m_editTextMessage.getText().toString(),
						SettingsManager.getInstance(getSherlockActivity()).getFacebookPageId(), getSherlockActivity(),
						new FacebookStatusPublishedEvent(this));
				break;
		}
		return super.onOptionsItemSelected(item);
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
