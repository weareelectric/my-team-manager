package com.myteammanager.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONException;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import com.myteammanager.util.Log;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;
import com.myteammanager.R;
import com.myteammanager.beans.PageBean;
import com.myteammanager.events.FacebookPageResponseEvent;
import com.myteammanager.events.FacebookResponseEvent;
import com.myteammanager.events.FacebookStatusPublishedEvent;
import com.myteammanager.storage.SettingsManager;
import com.myteammanager.ui.ListDialogFragment;
import com.myteammanager.ui.phone.HomePageActivity;
import com.myteammanager.util.StringUtil;

public class FacebookManager implements RequestListener {

	public static final String LOG_TAG = FacebookManager.class.getName();

	public static FacebookManager m_instance;
	private Context m_context;

	public static FacebookManager getInstance() {
		if (m_instance == null) {
			m_instance = new FacebookManager();
		}
		return m_instance;
	}

	public void postMessage(String message, String pageId, Context context, FacebookResponseEvent responseEvent) {
		Log.d(LOG_TAG, "Post message " + message + " pageId: " + pageId);
		String method = "POST";
		m_context = context;
		Bundle params = new Bundle();
		params.putString("permission", "publish_stream, manage_pages");

		params.putString("message", message);

		AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(HomePageActivity.getFacebook());

		if (StringUtil.isNotEmpty(SettingsManager.getInstance(context).getFacebookPageId())) {
			asyncRunner.request(pageId + "/feed", params, method, this, responseEvent);
		}

	}

	public void getPages(FacebookResponseEvent responseEvent) {
		String method = "GET";
		Bundle params = new Bundle();
		params.putString("permission", "manage_pages");

		AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(HomePageActivity.getFacebook());
		asyncRunner.request("/me/accounts", params, method, this, responseEvent);
	}
	
	public void removeApp(FacebookResponseEvent responseEvent) {
		String method = "GET";
		AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(HomePageActivity.getFacebook());
		asyncRunner.request("/me/permissions", null, method, this, responseEvent);
	}

	@Override
	public void onComplete(String response, Object state) {
		String error = null;
		try {
			error = FacebookParser.getError(response);
		} catch (JSONException e1) {
			e1.printStackTrace();
			error = "Json response not parsable";
		}

		if (error != null) {
			addErrorToTheResponseEventAndNotify(state, error);
			return;
		}

		if (state instanceof FacebookPageResponseEvent) {
			try {
				createFacebookPageResponse(response, state);
			} catch (JSONException e) {
				e.printStackTrace();
				postEmptyPagesList(state);
			}
		} else if (state instanceof FacebookStatusPublishedEvent) {
			FacebookStatusPublishedEvent responseEvent = (FacebookStatusPublishedEvent) state;
			if (responseEvent.getLiistener() != null) {
				responseEvent.getLiistener().postFBResponse(responseEvent);
			}
		}

	}

	public void addErrorToTheResponseEventAndNotify(Object state, String error) {
		FacebookResponseEvent responseEvent = (FacebookResponseEvent) state;
		FacebookError errorFB = new FacebookError(error, null, 0);
		responseEvent.setFacebookError(errorFB);

		if (responseEvent.getLiistener() != null) {
			responseEvent.getLiistener().postFBResponse(responseEvent);
		}
	}

	public void createFacebookPageResponse(String response, Object state) throws JSONException {
		FacebookPageResponseEvent pageEvent = (FacebookPageResponseEvent) state;

		ArrayList<PageBean> fbPageList = FacebookParser.getPages(response);
		if (fbPageList.size() > 0) {
			String[] pageNames = new String[fbPageList.size()];

			int k = 0;
			for (PageBean page : fbPageList) {
				pageNames[k] = page.getName();
				k++;
			}
		}

		if (pageEvent.getLiistener() != null) {
			pageEvent.setPages(fbPageList);
			pageEvent.getLiistener().postFBResponse(pageEvent);
		}
	}

	public void postEmptyPagesList(Object state) {
		FacebookPageResponseEvent pageEvent = (FacebookPageResponseEvent) state;
		if (pageEvent.getLiistener() != null) {
			pageEvent.setPages(new ArrayList<PageBean>());
			pageEvent.getLiistener().postFBResponse(pageEvent);
		}
	}

	@Override
	public void onIOException(IOException e, Object state) {
		addErrorToTheResponseEventAndNotify(state, "Connection error");
	}

	@Override
	public void onFileNotFoundException(FileNotFoundException e, Object state) {
		addErrorToTheResponseEventAndNotify(state, "File not found exception");
	}

	@Override
	public void onMalformedURLException(MalformedURLException e, Object state) {
		addErrorToTheResponseEventAndNotify(state, "Url Malformed");
	}

	@Override
	public void onFacebookError(FacebookError e, Object state) {
		addErrorToTheResponseEventAndNotify(state, "Facebook error");
	}

}
