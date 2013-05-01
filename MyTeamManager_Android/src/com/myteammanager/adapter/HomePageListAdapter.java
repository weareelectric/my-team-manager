package com.myteammanager.adapter;

import java.util.ArrayList;

import android.content.Context;
import com.myteammanager.util.Log;
import org.holoeverywhere.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import org.holoeverywhere.widget.TextView;

import com.myteammanager.R;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.MenuBean;

public class HomePageListAdapter extends ArrayAdapter<BaseBean> {
	
	private String LOG_TAG = HomePageListAdapter.class.getName();
	
	private LayoutInflater m_inflater;
	private ArrayList<BaseBean> m_items;
	private int m_layoutResourceId;
	private Context m_context;

	public HomePageListAdapter(Context context, int layoutResourceId,
			ArrayList<BaseBean> objects) {
		super(context, layoutResourceId, objects);
		this.m_context = context;
		m_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_layoutResourceId = layoutResourceId;
		m_items = objects;
	}
	
	@Override
	public MenuBean getItem(int position) {
		return (MenuBean)m_items.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HomepageItemRowView holder;
		
		final MenuBean menuitem = getItem(position);
		
		
		if (menuitem == null) {
			Log.d(LOG_TAG, "Could not find element at position " + position);
			return null;
		}
		
		if ( convertView == null ) {
			holder = new HomepageItemRowView();
			
			convertView = m_inflater.inflate(m_layoutResourceId, null);
			
			holder.labelView = (TextView)convertView.findViewById(R.id.menuLabel);
			
			convertView.setTag(holder);
		}
		else {
			holder = (HomepageItemRowView)convertView.getTag();
		}
		
		holder.labelView.setText(menuitem.getLabel());
		
		return convertView;
	}
	
	public static class HomepageItemRowView {
		private TextView labelView;
	}
	

}
