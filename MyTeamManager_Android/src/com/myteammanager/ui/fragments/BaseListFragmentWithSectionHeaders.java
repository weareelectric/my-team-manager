package com.myteammanager.ui.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.support.v4.app.DialogFragment;

import com.myteammanager.R;
import com.myteammanager.beans.BaseBean;

public abstract class BaseListFragmentWithSectionHeaders extends BaseListFragment {

	public static final int DIALOG_DELETE = 1;

	abstract protected void addSectionHeadersToItemsList();

	@Override
	protected void refreshItemsAfterLoadedNewData(ArrayList<BaseBean> newData) {
		super.refreshItemsAfterLoadedNewData(newData);
		addSectionHeadersToItemsList();
	}

	@Override
	protected void sortList(Comparator<BaseBean> comparator) {
		super.sortList(comparator);
		addSectionHeadersToItemsList();
	}

}
