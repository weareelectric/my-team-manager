package com.myteammanager.ui.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.myteammanager.BeanChangeBroadcastReceiver;
import com.myteammanager.R;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.SeparatorBean;
import com.myteammanager.storage.DBManager;
import com.myteammanager.ui.ButtonsAlertDialogListener;
import com.myteammanager.ui.quickaction.QuickAction;
import com.myteammanager.util.KeyConstants;

public abstract class BaseListFragment extends SherlockFragment implements OnItemClickListener, OnScrollListener,
		ButtonsAlertDialogListener {

	public static final String LOG_TAG = BaseListFragment.class.getName();

	protected ArrayAdapter<? extends BaseBean> m_adapter;
	protected ArrayList<BaseBean> m_itemsList;
	protected ArrayList<BaseBean> m_separatorBean;
	protected ListView m_listView;
	protected boolean m_isGettingData = false;

	protected boolean m_isFastScrolledEnabled = false;
	protected QuickAction m_quickAction;
	protected int m_itemSelectedForContextMenu = -1;

	protected BeanChangeBroadcastReceiver m_beanChangeReceiver;

	protected abstract void init();

	protected abstract ArrayAdapter<? extends BaseBean> initAdapter();

	protected abstract ArrayList<? extends BaseBean> getData();

	protected ProgressDialog pd;

	public abstract void button1Pressed(int alertId);

	public abstract void button2Pressed(int alertId);

	public abstract void button3Pressed(int alertId);

	public String getDeleteConfirmationMsg() {
		Resources res = getSherlockActivity().getResources();
		return res.getString(R.string.alert_general_confirmation_msg);
	}

	public String getDeleteConfirmationTitle() {
		Resources res = getSherlockActivity().getResources();
		return res.getString(R.string.alert_general_confirmation_title);
	}

	public void showDeleteConfirmation() {
		showDeleteConfirmation(getDeleteConfirmationMsg());
	}

	public void showDeleteConfirmation(String msg) {
		ButtonsAlertDialogFragment newFragment = new ButtonsAlertDialogFragment(getDeleteConfirmationTitle(), msg, this,
				R.string.label_yes, R.string.label_no);
		newFragment.show(getFragmentManager(), "");
	}

	public void showDeleteConfirmation(int alertId, String msg) {
		ButtonsAlertDialogFragment newFragment = new ButtonsAlertDialogFragment(alertId, getDeleteConfirmationTitle(), msg,
				this, R.string.label_yes, R.string.label_no);
		newFragment.show(getFragmentManager(), "");
	}

	protected void registerBroadcasrReceiverForBeanEvents() {
		m_beanChangeReceiver = new BeanChangeBroadcastReceiver(this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(KeyConstants.INTENT_BEAN_ADDED);
		filter.addAction(KeyConstants.INTENT_BEAN_CHANGED);
		filter.addAction(KeyConstants.INTENT_BEAN_DELETED);
		filter.addAction(KeyConstants.INTENT_RELOAD_LIST);
		getSherlockActivity().registerReceiver(m_beanChangeReceiver, filter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_base_list, null);
		m_listView = (ListView) root.findViewById(R.id.list);
		registerForContextMenu(m_listView);
		setHasOptionsMenu(true);
		return root;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (m_itemsList == null) {
			m_itemsList = new ArrayList<BaseBean>();
		}
		m_adapter = initAdapter();
		init();

		m_listView.setAdapter(m_adapter);
		m_listView.setOnItemClickListener(this);
		
		requestData();
	}

	protected void showProgressDialog(final String text) {
		getSherlockActivity().runOnUiThread(new Runnable() {
			public void run() {
				pd = ProgressDialog.show(getSherlockActivity(), "", text, true);
			}
		});

	}

	protected void cancelProgressDialog() {
		getSherlockActivity().runOnUiThread(new Runnable() {
			public void run() {
				if (pd != null) {
					pd.cancel();
				}
			}
		});

	}

	/**
	 * This method can be called to refresh the data shown in the list
	 */
	public void requestData() {

		new AsyncTask<Void, Void, String[]>() {

			@Override
			protected String[] doInBackground(Void... params) {
				showProgressDialog(getResources().getString(R.string.dialog_please_wait_loading_data));
				m_isGettingData = true;
				ArrayList<BaseBean> newData = (ArrayList<BaseBean>) getData();
				refreshItemsAfterLoadedNewData(newData);
				Log.d(LOG_TAG, "Size of list: " + m_itemsList.size());
				return null;
			}

			@Override
			protected void onPostExecute(String[] result) {
				m_adapter.notifyDataSetChanged();
				m_listView.setFastScrollEnabled(m_isFastScrolledEnabled);
				m_isGettingData = false;
				cancelProgressDialog();

			}

		}.execute();
	}

	protected void refreshItemsAfterLoadedNewData(ArrayList<BaseBean> newData) {
		m_itemsList.clear();
		m_itemsList.addAll(newData);

	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		Log.d(LOG_TAG, "contextMenu");

		if (v.getId() == R.id.list) {
			if (m_quickAction != null) {
				AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
				ListView listView = (ListView) v;
				Log.d(LOG_TAG, "info.position: " + info.position);
				Log.d(LOG_TAG, "Count of childs in list: " + m_listView.getCount());
				m_itemSelectedForContextMenu = info.position;
				m_quickAction.show(info.targetView);
			}
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == KeyConstants.CODE_BEAN_CHANGE) {
			if (resultCode > 0) {
				BaseBean mainBean = (BaseBean) data.getExtras().get(KeyConstants.KEY_BEANDATA);
				ArrayList<BaseBean> beansList = (ArrayList<BaseBean>) data.getExtras().get(KeyConstants.KEY_BEANDATA_LIST);
				if (resultCode == KeyConstants.RESULT_BEAN_EDITED) {
					// requestData();
					editBeanInTheList(mainBean, beansList);
				} else if (resultCode == KeyConstants.RESULT_BEAN_ADDED) {
					addBeanToTheListAndRefresh(mainBean, beansList);
				} else if (resultCode == KeyConstants.RESULT_BEAN_DELETED) {
					deleteBeanInTheList(mainBean, beansList);
				}
			}

		}
	}

	public void addBeanToTheListAndRefresh(BaseBean mainBean) {
		addBeanToTheListAndRefresh(mainBean, null);
	}

	public void addBeanToTheListAndRefresh(BaseBean mainBean, ArrayList<BaseBean> beans) {
		Log.d(LOG_TAG, "Add beans list and refresh");
		m_itemsList.add(mainBean);
		if (beans != null)
			m_itemsList.addAll(beans);

		if (mainBean != null) {
			sortList(mainBean.getComparator());
		}

		m_adapter.notifyDataSetChanged();
	}

	protected void sortList(Comparator<BaseBean> comparator) {
		removeSpearatorBeanFromList();
		Collections.sort(m_itemsList, comparator);
	}

	public void editBeanInTheList(BaseBean mainBean) {
		editBeanInTheList(mainBean, null);
	}

	public void editBeanInTheList(BaseBean mainBean, ArrayList<BaseBean> beans) {

		int index = m_itemsList.indexOf(mainBean);
		if (index != -1) {
			m_itemsList.set(index, mainBean);
		}

		if (beans != null) {
			for (BaseBean bean : beans) {
				index = m_itemsList.indexOf(bean);
				if (index != -1) {
					m_itemsList.set(index, bean);
				}
			}

		}

		sortList(mainBean.getComparator());

		m_adapter.notifyDataSetChanged();
	}

	public void deleteBeanInTheList(BaseBean mainBean) {
		deleteBeanInTheList(mainBean, null);
	}

	public void deleteBeanInTheList(BaseBean mainBean, ArrayList<BaseBean> beans) {
		m_itemsList.remove(mainBean);

		if (beans != null) {
			for (BaseBean bean : beans) {
				m_itemsList.remove(bean);
			}
		}

		sortList(mainBean.getComparator());

		m_adapter.notifyDataSetChanged();
	}

	protected void removeSpearatorBeanFromList() {
		ArrayList<BaseBean> newList = new ArrayList<BaseBean>();
		for (BaseBean bean : m_itemsList) {
			if (!(bean instanceof SeparatorBean)) {
				newList.add(bean);
			}
		}

		m_itemsList.clear();
		m_itemsList.addAll(newList);
	}

	@Override
	public void onDestroy() {
		if (m_beanChangeReceiver != null) {
			getSherlockActivity().unregisterReceiver(m_beanChangeReceiver);
		}

		super.onDestroy();
	}

	public void showThreeButtonDialog(String title, String msg, int textBtn1, int textBtn2, int textBtn3) {
		ButtonsAlertDialogFragment newFragment = new ButtonsAlertDialogFragment(title, msg, this, textBtn1, textBtn2,
				textBtn3);
		newFragment.show(getFragmentManager(), "");
	}

	public void showTwoButtonDialog(String title, String msg, int textBtn1, int textBtn2) {
		ButtonsAlertDialogFragment newFragment = new ButtonsAlertDialogFragment(title, msg, this, textBtn1, textBtn2);
		newFragment.show(getFragmentManager(), "");
	}

	protected void insertBeans(final ArrayList<? extends BaseBean> beans, final boolean isUpdate,
			final boolean exitFromActivityAtTheEnd) {
		new AsyncTask<Void, Void, String[]>() {

			@Override
			protected String[] doInBackground(Void... params) {
				showProgressDialog(getResources().getString(R.string.dialog_please_wait_writing_data));
				if (isUpdate) {
					DBManager.getInstance().updateBeans(beans);
				} else {
					DBManager.getInstance().insertBeans(beans);
				}

				return null;
			}

			@Override
			protected void onPostExecute(String[] result) {
				cancelProgressDialog();
				if (exitFromActivityAtTheEnd) {
					getSherlockActivity().finish();
				}

			}

		}.execute();

	}

}
