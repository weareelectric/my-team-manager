package com.myteammanager.util;

import android.content.Intent;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.myteammanager.R;
import com.myteammanager.beans.EventBean;
import com.myteammanager.storage.DBManager;
import com.myteammanager.ui.fragments.BaseFragment;
import com.myteammanager.ui.fragments.BaseListFragment;

public class EventDeleteConfirmationManager {

	public static void askEventConfirmation(EventBean event, BaseFragment fragment) {
		if (event.isEventLinkedToOthers()) {
			fragment.showThreeButtonDialog(fragment.getDeleteConfirmationTitle(), fragment.getDeleteConfirmationMsg(),
					R.string.alert_delete_event_conf_btn1, R.string.alert_delete_event_conf_btn2,
					R.string.alert_delete_event_conf_btn3);
		} else {
			fragment.showDeleteConfirmation();
		}
	}

	public static void askEventConfirmation(EventBean event, BaseListFragment fragment) {
		if (event.isEventLinkedToOthers()) {
			fragment.showThreeButtonDialog(fragment.getDeleteConfirmationTitle(), fragment.getDeleteConfirmationMsg(),
					R.string.alert_delete_event_conf_btn1, R.string.alert_delete_event_conf_btn2,
					R.string.alert_delete_event_conf_btn3);
		} else {
			fragment.showDeleteConfirmation();
		}
	}

	public static void askDeleteAllEventsConfirmation(BaseListFragment fragment) {

	}

	public static void deleteAllLinkedEvents(EventBean event, SherlockFragmentActivity sherlockFragmentActivity,
			boolean sendRefreshListIntent) {
		String whereClause = "id == " + event.getId() + " or parentEvent == " + event.getId();

		// Delete all the linked events....parent and children
		if (event.getParentEvent() != null) {
			whereClause = "id == " + event.getId() + " or id == " + event.getParentEvent().getId() + " or parentEvent == "
					+ event.getParentEvent().getId();
		}

		DBManager.getInstance().deleteBeanWithWhere(event, whereClause);

		if (sendRefreshListIntent) {
			Intent intent = new Intent(KeyConstants.INTENT_RELOAD_LIST);
			sherlockFragmentActivity.sendBroadcast(intent);
		}

	}
}
