package com.myteammanager.adapter;

import java.util.ArrayList;

import android.content.Context;

import com.myteammanager.adapter.holders.BaseHolder;
import com.myteammanager.adapter.holders.TextWithCheckboxItemRowHolder;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.ui.CheckboxListener;

public abstract class BaseAdapterWithCheckbox extends BaseAdapterWithSectionHeaders {
	
	protected CheckboxListener m_checkboxListener;
	
	public BaseAdapterWithCheckbox(Context context, int layoutResourceId, ArrayList<BaseBean> objects,
			CheckboxListener playerCheckboxListeenr) {
		super(context, layoutResourceId, objects);
		m_checkboxListener = playerCheckboxListeenr;
	}
	
	@Override
	public boolean isEnabled(int position) {
		return false;
	}
	

	@Override
	protected void populateHolder(BaseHolder holder, BaseBean baseBean) {
		TextWithCheckboxItemRowHolder realHolder = (TextWithCheckboxItemRowHolder) holder;
		realHolder.setCheckboxListener(m_checkboxListener);
		
		realHolder.getTextView().setText(getText(baseBean));

		if (flagTheCheckbox(baseBean)) {
			realHolder.getCheckbox().setChecked(true);
		} else {
			realHolder.getCheckbox().setChecked(false);
		}
	}
	
	protected abstract String getText(BaseBean bean);
	
	protected abstract boolean flagTheCheckbox(BaseBean bean);

	@Override
	protected abstract BaseHolder getHolder();

	@Override
	protected BaseBean getBean(int i) {
		return getItem(i);
	}


}
