package com.myteammanager.adapter;

import java.util.ArrayList;

import android.content.Context;

import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.ui.PlayerCheckboxListener;

public class ConvocationAdapter extends PlayerListAdapterWithCheckbox {

	public ConvocationAdapter(Context context, int layoutResourceId, ArrayList<BaseBean> objects,
			PlayerCheckboxListener playerCheckboxListeenr) {
		super(context, layoutResourceId, objects, playerCheckboxListeenr);
	}

	@Override
	protected boolean flagTheCheckbox(BaseBean bean) {
		if (!(bean instanceof PlayerBean))
			return false;
		else {
			return ((PlayerBean) bean).isConvocated();
		}

	}

}
