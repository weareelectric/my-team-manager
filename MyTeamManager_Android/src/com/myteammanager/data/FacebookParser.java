package com.myteammanager.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.myteammanager.beans.PageBean;

public class FacebookParser {

	public static ArrayList<PageBean> getPages(String json) throws JSONException {
		JSONObject obj = new JSONObject(json);
		JSONArray pages = obj.getJSONArray("data");

		ArrayList<PageBean> pagesList = new ArrayList<PageBean>();

		for (int k = 0; k < pages.length(); k++) {
			JSONObject pageObj = pages.getJSONObject(k);
			pagesList.add(new PageBean(pageObj.getString("id"), pageObj.getString("name")));
		}

		return pagesList;
	}

	public static String getError(String response) throws JSONException {
		JSONObject obj = new JSONObject(response);
		JSONObject error;
		try {
			error = obj.getJSONObject("error");
		} catch (JSONException e) {
			return null;
		}

		if (error != null) {
			return error.getString("message");
		}

		return null;
	}
}
