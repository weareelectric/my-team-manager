package com.myteammanager.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;

import com.myteammanager.R;
import com.myteammanager.adapter.holders.BaseHolder;
import com.myteammanager.adapter.holders.SeparatorHolder;
import com.myteammanager.adapter.sectionindexes.SectionIndex;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.SeparatorBean;

public abstract class BaseAdapterWithSectionHeaders extends ArrayAdapter<BaseBean> implements SectionIndexer {

	private String LOG_TAG = BaseAdapterWithSectionHeaders.class.getName();

	private static final int SEPERATOR_TAG_INDEX = 922568940;
	private static final int OTHER_TAG_INDEX = 922568932;

	protected SectionIndex[] m_sections;

	protected LayoutInflater m_inflater;
	protected ArrayList<BaseBean> m_items;
	protected int m_layoutResourceId;
	protected Context m_context;

	protected HashMap<String, Integer> m_sectionIndexer;

	public BaseAdapterWithSectionHeaders(Context context, int layoutResourceId, ArrayList<BaseBean> objects) {
		super(context, layoutResourceId, objects);
		this.m_context = context;
		m_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_layoutResourceId = layoutResourceId;
		m_items = objects;
	}

	@Override
	public boolean isEnabled(int position) {
		BaseBean bean = getBean(position);
		if (bean instanceof SeparatorBean) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public BaseBean getItem(int position) {
		return m_items.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseHolder holder;

		Log.d(LOG_TAG, "Check bean at position " + position + " in items list of size " + m_items.size());

		BaseBean bean = getBean(position);

		if (bean == null) {
			Log.d(LOG_TAG, "Could not find element at position " + position);
			return null;
		}

		if (convertView == null) {
			if (bean instanceof SeparatorBean) {
				holder = new SeparatorHolder();
				convertView = m_inflater.inflate(R.layout.header_list, null);
				holder.configureViews(convertView, bean);
				convertView.setTag(SEPERATOR_TAG_INDEX, holder);
			} else {
				holder = getHolder();
				convertView = m_inflater.inflate(m_layoutResourceId, null);
				holder.configureViews(convertView, bean);
				convertView.setTag(OTHER_TAG_INDEX, holder);
			}
		} else {

			if (bean instanceof SeparatorBean) {
				holder = (SeparatorHolder) convertView.getTag(SEPERATOR_TAG_INDEX);
				if (holder == null) {
					holder = new SeparatorHolder();
					convertView = m_inflater.inflate(R.layout.header_list, null);
					holder.configureViews(convertView, bean);
					convertView.setTag(SEPERATOR_TAG_INDEX, holder);
				}
			} else {
				holder = (BaseHolder) convertView.getTag(OTHER_TAG_INDEX);
				if (holder == null) {
					holder = getHolder();
					convertView = m_inflater.inflate(m_layoutResourceId, null);
					holder.configureViews(convertView, bean);
					convertView.setTag(OTHER_TAG_INDEX, holder);
				}
			}

		}

		if (bean instanceof SeparatorBean) {
			((SeparatorHolder) holder).getSeparatorLabelTextView().setText(((SeparatorBean) bean).getSeparatorString());
		} else {
			populateHolder(holder, bean);
		}

		return convertView;
	}

	@Override
	public int getPositionForSection(int index) {
		Log.d(LOG_TAG, "getPositionForSection");
		if (m_sections != null) {
			return m_sections[index].getPosition();
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		Log.d(LOG_TAG, "getSectionForPosition");
		for (int i = 0; i < m_sections.length; i++) {
			if (m_sections[i].getPosition() >= position)
				return i;
		}
		return 0;
	}

	@Override
	public Object[] getSections() {
		Log.d(LOG_TAG, "getSections");
		if (m_sectionIndexer == null) {
			m_sectionIndexer = new HashMap<String, Integer>();
		} else {
			m_sectionIndexer.clear();
		}

		if (m_items != null) {

			int size = m_items.size();

			BaseBean bean;
			for (int k = 0; k < size; k++) {
				bean = m_items.get(k);

				if (bean instanceof SeparatorBean) {
					m_sectionIndexer.put(((SeparatorBean) bean).getSeparatorString(), k);
				}
			}

			Vector<SectionIndex> sectionsVector = new Vector<SectionIndex>();
			Iterator<String> roles = m_sectionIndexer.keySet().iterator();

			String key = null;
			while (roles.hasNext()) {
				key = roles.next();
				sectionsVector.add(new SectionIndex(key, m_sectionIndexer.get(key)));
			}

			sectionsVector.toArray(m_sections = new SectionIndex[sectionsVector.size()]);
			Log.d(LOG_TAG, "m_sections.size(): " + m_sections.length);
			return m_sections;

		} else
			return (m_sections = null);

		// No need to sort the list at this moment -> everything must be sorted in the query SQL 

	}

	protected BaseBean getBean(int i) {
		return getItem(i);
	}

	protected abstract BaseHolder getHolder();

	protected abstract void populateHolder(BaseHolder holder, BaseBean bean);

}
