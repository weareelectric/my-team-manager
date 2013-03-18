package com.myteammanager.ui.fragments;

import java.util.ArrayList;
import java.util.logging.Logger;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.myteammanager.R;
import com.myteammanager.adapter.ConvocationAdapter;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.ConvocationBean;
import com.myteammanager.beans.MatchBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.data.FacebookManager;
import com.myteammanager.specializedStorage.MyTeamManagerDBManager;
import com.myteammanager.storage.DBManager;
import com.myteammanager.storage.SettingsManager;
import com.myteammanager.ui.CheckboxListener;
import com.myteammanager.ui.phone.EditPlayerInfoActivity;
import com.myteammanager.util.DateTimeUtil;
import com.myteammanager.util.KeyConstants;
import com.myteammanager.util.PlayerAndroidUtil;
import com.myteammanager.util.StringUtil;

public class EditConvocationFragment extends RosterFragment implements TabListener, CheckboxListener {

	private static final int MSG_UPDATETITLE = 1;

	private static final String EDIT_CONVOCATION_TAG = "edit_convocation";
	private static final String CONVOCATED_TAG = "convocated";

	private MatchBean m_match;
	private ArrayList<PlayerBean> m_convocatedPlayers;
	private boolean m_convocations = false;
	private View m_root;
	private int[] m_numbersOfConvocatedPlayersForRole;
	private ArrayList<ConvocationBean> m_finalConvocations;

	//	protected TextView m_buttonTotal;
	//	protected TextView m_buttonGk;
	//	protected TextView m_buttonDef;
	//	protected TextView m_buttonMid;
	//	protected TextView m_buttonAtt;
	protected LinearLayout m_child1;
	//	protected LinearLayout m_child2;
	//	protected LinearLayout m_child3;
	protected EditText m_hangoutPlaceAndTimeEditText;



	final Handler m_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MSG_UPDATETITLE) {

				getSherlockActivity().setTitle(getString(R.string.title_convocations, m_numbersOfConvocatedPlayersForRole[0]));

				m_child1.setVisibility(View.VISIBLE);

			}
			super.handleMessage(msg);
		}
	};


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		updateTotalNumberAndNumberOfConvocatedForRole();
		m_root = inflater.inflate(R.layout.fragment_edit_convocations, null);
		m_listView = (ListView) m_root.findViewById(R.id.list);
		m_hangoutPlaceAndTimeEditText = (EditText) m_root.findViewById(R.id.textFieldHangoutPlaceAndTime);
		registerForContextMenu(m_listView);
		setHasOptionsMenu(true);
		return m_root;

	}

	@Override
	protected void init() {
		Log.d(LOG_TAG, "init");


		Bundle extra = getSherlockActivity().getIntent().getExtras();
		m_match = (MatchBean) extra.get(KeyConstants.KEY_MATCH);

		if (m_match.getNumberOfPlayerConvocated() > 0) {
			m_convocations = true;
		}

		m_hangoutPlaceAndTimeEditText.setText(m_match.getAppointmentPlaceAndTime());
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(LOG_TAG, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		if (m_root != null) {

			m_child1 = (LinearLayout) m_root.findViewById(R.id.child1);

		}
	}

	@Override
	protected ArrayAdapter<? extends BaseBean> initAdapter() {
		m_itemsList.clear();
		return new ConvocationAdapter(getActivity(), R.layout.list_with_checkbox, m_itemsList, this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.d(LOG_TAG, "onItemClicked");
		// Do nothing. Avoid that the click opens the activity to modify the player
		
	}

	@Override
	protected void refreshItemsAfterLoadedNewData(ArrayList<BaseBean> newData) {
		super.refreshItemsAfterLoadedNewData(newData);

		// now update the player object checking if they are convocated
		recalculateConvocatedPlayers();

		updateCountersForConvocations();

	}

	public void updateCountersForConvocations() {
		updateTotalNumberAndNumberOfConvocatedForRole();
		Message msg = m_handler.obtainMessage();
		msg.what = MSG_UPDATETITLE;
		m_handler.sendMessage(msg);
	}

	public void recalculateConvocatedPlayers() {
		int playerSize = m_itemsList.size();

		Object obj = null;
		PlayerBean toAnalyse = null;
		if (m_convocatedPlayers != null) {
			for (int k = 0; k < playerSize; k++) {
				obj = m_itemsList.get(k);
				if (obj instanceof PlayerBean) {
					toAnalyse = (PlayerBean) obj;

					Log.d(LOG_TAG, "ToAnalyze: " + toAnalyse.getId());

					if (m_convocatedPlayers.contains(toAnalyse)) {
						Log.d(LOG_TAG, "player id " + toAnalyse.getId() + " is Convocated");
						toAnalyse.setConvocated(true);
					} else {
						toAnalyse.setConvocated(false);
					}
				}
			}
		}
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.edit_convocations, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_save_convocations:
			m_finalConvocations = new ArrayList<ConvocationBean>();
			PlayerBean player = null;
			ConvocationBean convocation = null;
			Object obj = null;
			int size = m_itemsList.size();
			int convocatedPlayer = 0;

			for (int i = 0; i < size; i++) {
				obj = m_itemsList.get(i);
				if (obj instanceof PlayerBean) {
					player = (PlayerBean) obj;

					if (player.isConvocated()) {
						convocation = new ConvocationBean();
						Log.d(LOG_TAG, "Id of match: " + m_match.getId());
						convocation.setMatch(m_match);
						convocation.setPlayer(player);
						m_finalConvocations.add(convocation);
						convocatedPlayer++;
					}
				}

			}

			m_match.setNumberOfPlayerConvocated(convocatedPlayer);

			m_match.setAppointmentPlaceAndTime(m_hangoutPlaceAndTimeEditText.getText().toString());

			if (convocatedPlayer > 0) {
				m_convocations = true;
			} else {
				m_convocations = false;
			}

			if (SettingsManager.getInstance(getSherlockActivity()).isFacebookActivated()) {
				Resources resources = getResources();
				showTwoButtonDialog(null, resources.getString(R.string.dialog_facebook_post_convocation), R.string.label_yes,
						R.string.label_no);
			} else {
				saveData();
			}

			break;
		}
		return true;
	}

	protected void saveData() {
		// First, delete the previous entries in the db
		DBManager.getInstance().deleteBeanWithWhere(new ConvocationBean(), "match=" + m_match.getId());

		// Store the new convocations and the number of convocated players in the match object
		Intent intent = new Intent();
		intent.putExtra(KeyConstants.KEY_BEANDATA, m_match);
		getSherlockActivity().sendBroadcast(intent);

		getSherlockActivity().setResult(KeyConstants.RESULT_BEAN_EDITED, intent);
		
		DBManager.getInstance().updateBean(m_match);
		
		insertBeans(m_finalConvocations, false, true);

	}


	public void postOnFacebook(ArrayList<ConvocationBean> convocations) {
		StringBuffer sb = new StringBuffer();
		sb.append(m_match.getMatchString(getSherlockActivity()));

		String timestamp = "";
		if (m_match.getTimestamp() != -1) {
			timestamp += "\n\n";
			timestamp += getString(R.string.label_date_semicoloumn,
					DateTimeUtil.getDateFrom(m_match.getTimestamp(), getSherlockActivity()));
			timestamp += " ";
		}

		if (m_match.getTimestamp() != -1) {
			timestamp += " - ";
			timestamp += DateTimeUtil.getTimeStringFrom(m_match.getTimestamp());
		}

		sb.append(timestamp);
		sb.append("\n");

		if (StringUtil.isNotEmpty(m_match.getLocation())) {
			sb.append(getString(R.string.label_location_semicoloumn, m_match.getLocation()));
		}

		if (StringUtil.isNotEmpty(m_match.getAppointmentPlaceAndTime())) {
			sb.append(getString(R.string.label_hangout_place_and_time, m_match.getAppointmentPlaceAndTime()));
		}

		sb.append("\n\n");
		sb.append(getString(R.string.label_string_conovcated_semicoloumn));
		sb.append("\n");

		for (int k = 0; k < convocations.size(); k++) {
			sb.append("\n");
			sb.append(convocations.get(k).getPlayer().getSurnameAndName(false));
		}

		if (SettingsManager.getInstance(getSherlockActivity()).isFacebookActivated()) {
			FacebookManager.getInstance().postMessage(sb.toString(),
					SettingsManager.getInstance(getSherlockActivity()).getFacebookPageId(), getSherlockActivity(), null);
		}

	}

	@Override
	public void button1Pressed(int alertId) {
		// Ok. Post on Facebook
		Log.d(LOG_TAG, "button1Pressed");
		postOnFacebook(m_finalConvocations);
		saveData();
	}

	@Override
	public void button2Pressed(int alertId) {

	}

	@Override
	public void button3Pressed(int alertId) {
		Log.d(LOG_TAG, "button3Pressed");
		saveData();
	}

	@Override
	protected ArrayList<? extends BaseBean> getData() {
		// Load also the configured convocations for this match
		if (m_convocations) {
			ArrayList<ConvocationBean> convocations = (ArrayList<ConvocationBean>) DBManager.getInstance().getListOfBeansWhere(new ConvocationBean(), "match=" + m_match.getId(), false);
			if (convocations != null) {
				m_convocatedPlayers = new ArrayList<PlayerBean>();

				int size = convocations.size();

				Log.d(LOG_TAG, "Number of convocated player: " + size);

				ConvocationBean convocation = null;
				for (int k = 0; k < size; k++) {
					convocation = (ConvocationBean) convocations.get(k);

					m_convocatedPlayers.add(convocation.getPlayer());

					Log.d(LOG_TAG, "Convocated player: " + convocation.getPlayer().getId());
				}
			}

		}

		return MyTeamManagerDBManager.getInstance().getListOfBeans(new PlayerBean(), true);
	}

	private void updateTotalNumberAndNumberOfConvocatedForRole() {
		if (m_numbersOfConvocatedPlayersForRole == null) {
			m_numbersOfConvocatedPlayersForRole = new int[] { 0, 0, 0, 0, 0 };
		}

		if (m_convocatedPlayers == null) {
			return;
		}

		int size = m_convocatedPlayers.size();

		m_numbersOfConvocatedPlayersForRole = new int[5];

		m_numbersOfConvocatedPlayersForRole[1] = 0;
		m_numbersOfConvocatedPlayersForRole[2] = 0;
		m_numbersOfConvocatedPlayersForRole[3] = 0;
		m_numbersOfConvocatedPlayersForRole[4] = 0;
		m_numbersOfConvocatedPlayersForRole[0] = size;

		PlayerBean player = null;
		for (int k = 0; k < size; k++) {
			player = (PlayerBean) m_convocatedPlayers.get(k);

			Log.d(LOG_TAG, "Role of the convocated player: " + player.getRole());
			if (player.getRole() == PlayerBean.ROLE_GK) {
				m_numbersOfConvocatedPlayersForRole[1]++;
			} else if (player.getRole() == PlayerBean.ROLE_DEF) {
				m_numbersOfConvocatedPlayersForRole[2]++;
			} else if (player.getRole() == PlayerBean.ROLE_MID) {
				m_numbersOfConvocatedPlayersForRole[3]++;
			} else if (player.getRole() == PlayerBean.ROLE_ATT) {
				m_numbersOfConvocatedPlayersForRole[4]++;
			}

		}

	}

	private int getNumberOfConvocatedPlayersFor(int playerRole) {
		if (m_convocatedPlayers == null) {
			m_numbersOfConvocatedPlayersForRole = new int[] { 0, 0, 0, 0, 0 };
		}

		int result = 0;
		switch (playerRole) {
		case PlayerBean.ROLE_GK:
			result = m_numbersOfConvocatedPlayersForRole[1];
			break;

		case PlayerBean.ROLE_DEF:
			result = m_numbersOfConvocatedPlayersForRole[2];
			break;

		case PlayerBean.ROLE_MID:
			result = m_numbersOfConvocatedPlayersForRole[3];
			break;

		case PlayerBean.ROLE_ATT:
			result = m_numbersOfConvocatedPlayersForRole[4];
			break;
		}

		return result;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void checkboxChanged(boolean isSelectAll) {
		Log.d(EDIT_CONVOCATION_TAG, "convocationChanged");

		int size = m_itemsList.size();
		PlayerBean player;
		Object obj;
		if (m_convocatedPlayers != null) {
			m_convocatedPlayers.clear();
		} else {
			m_convocatedPlayers = new ArrayList<PlayerBean>();
		}

		for (int i = 0; i < size; i++) {
			obj = m_itemsList.get(i);
			if (obj instanceof PlayerBean) {
				player = (PlayerBean) obj;

				if (player.isConvocated()) {
					m_convocatedPlayers.add(player);
				}
			}
		}

		updateCountersForConvocations();
	}

}
