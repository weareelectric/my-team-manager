package com.myteammanager.ui.fragments;

import java.util.ArrayList;

import org.holoeverywhere.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import org.holoeverywhere.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.holoeverywhere.app.Fragment;
import com.myteammanager.R;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.storage.DBManager;
import com.myteammanager.ui.ButtonsAlertDialogListener;

public abstract class BaseFragment extends Fragment implements ButtonsAlertDialogListener {

	protected ProgressDialog pd;

	@Override
	public abstract void button1Pressed(int alertId);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public String getDeleteConfirmationMsg() {
		Resources res = getActivity().getResources();
		return res.getString(R.string.alert_general_confirmation_msg);
	}

	public String getDeleteConfirmationTitle() {
		Resources res = getActivity().getResources();
		return res.getString(R.string.alert_general_confirmation_title);
	}

	public void showDeleteConfirmation() {
		ButtonsAlertDialogFragment newFragment = new ButtonsAlertDialogFragment(getDeleteConfirmationTitle(),
				getDeleteConfirmationMsg(), this, R.string.label_yes, R.string.label_no);
		newFragment.show(getFragmentManager(), "");
	}

	public void showThreeButtonDialog(String title, String msg, int textBtn1, int textBtn2, int textBtn3) {
		ButtonsAlertDialogFragment newFragment = new ButtonsAlertDialogFragment(title, msg, this, textBtn1, textBtn2,
				textBtn3);
		newFragment.show(getFragmentManager(), "");
	}

	protected void showProgressDialog(final String text) {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				pd = ProgressDialog.show(getActivity(), "", text, true);
			}
		});

	}

	protected void cancelProgressDialog() {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				if (pd != null) {
					pd.cancel();
					pd = null;
				}
			}
		});

	}

	protected void insertBeans(final ArrayList<? extends BaseBean> beans, final boolean isUpdate) {
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
				doActionAfetrInsertBeans();
				getActivity().finish();
			}

		}.execute();

	}

	/**
	 * This method can be used to perform actions after the thread storing the beans has ended. Each subclass can
	 * specialize the method
	 */
	protected void doActionAfetrInsertBeans() {

	}

}
