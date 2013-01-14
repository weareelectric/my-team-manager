package com.myteammanager.adapter;

import java.util.ArrayList;

import android.content.Context;

import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.ui.CheckboxListener;

public class SubstitutesAdapter extends PlayerListAdapterWithCheckbox {

	public SubstitutesAdapter(Context context, int layoutResourceId, ArrayList<BaseBean> objects,
			CheckboxListener playerCheckboxListeenr) {
		super(context, layoutResourceId, objects, playerCheckboxListeenr);
	}

	@Override
	protected boolean flagTheCheckbox(BaseBean bean) {
		if (!(bean instanceof PlayerBean))
			return false;
		else {
			return ((PlayerBean) bean).isOnTheBench();
		}

	}

}
