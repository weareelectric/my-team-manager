package com.myteammanager;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.myteammanager.util.Log;

import com.myteammanager.beans.BaseBean;
import com.myteammanager.ui.fragments.BaseListFragment;
import com.myteammanager.util.KeyConstants;

public class BeanChangeBroadcastReceiver extends BroadcastReceiver {

	private BaseListFragment m_baseListFragment;

	public BeanChangeBroadcastReceiver(BaseListFragment m_baseListFragment) {
		super();
		this.m_baseListFragment = m_baseListFragment;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			BaseBean mainBean = (BaseBean) bundle.get(KeyConstants.KEY_BEANDATA);
			ArrayList<BaseBean> childBeans = (ArrayList<BaseBean>) bundle.get(KeyConstants.KEY_BEANDATA_LIST);
			if (intent.getAction().equals(KeyConstants.INTENT_BEAN_ADDED)) {
				m_baseListFragment.addBeanToTheListAndRefresh(mainBean, childBeans);
			} else if (intent.getAction().equals(KeyConstants.INTENT_BEAN_DELETED)) {
				m_baseListFragment.deleteBeanInTheList(mainBean, childBeans);
			} else if (intent.getAction().equals(KeyConstants.INTENT_BEAN_CHANGED)) {
				m_baseListFragment.editBeanInTheList(mainBean, childBeans);
			}

		}

		if (intent.getAction().equals(KeyConstants.INTENT_RELOAD_LIST)) {
			m_baseListFragment.requestData();
		}
	}

}
