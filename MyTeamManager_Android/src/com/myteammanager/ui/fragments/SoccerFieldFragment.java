package com.myteammanager.ui.fragments;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.myteammanager.MyTeamManagerActivity;
import com.myteammanager.R;
import com.myteammanager.beans.ConvocationBean;
import com.myteammanager.beans.LineupBean;
import com.myteammanager.beans.MatchBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.events.TeamLineupSelectionEvent;
import com.myteammanager.specializedStorage.MyTeamManagerDBManager;
import com.myteammanager.storage.DBManager;
import com.myteammanager.ui.phone.EditSubstitutesActivity;
import com.myteammanager.ui.phone.EditTeamLineUpActivity;
import com.myteammanager.ui.phone.PlayerListForTeamLineupActivity;
import com.myteammanager.ui.quickaction.ActionItem;
import com.myteammanager.ui.quickaction.QuickAction;
import com.myteammanager.util.KeyConstants;
import com.myteammanager.util.PlayerUtil;

public class SoccerFieldFragment extends SherlockFragment implements OnClickListener, OnLongClickListener {

	private static final String LOG_TAG = SoccerFieldFragment.class.getName();

	private static final int ID_REMOVE_PLAYERLINEUP = 1;

	protected FrameLayout m_root;
	private RelativeLayout m_playersLayout;

	private ArrayList<PlayerBean> m_playersConvocatedOrInTheRoster = null;
	private ArrayList<PlayerBean> m_chosenPlayers = new ArrayList<PlayerBean>();
	private ArrayList<PlayerBean> m_substitutePlayers = new ArrayList<PlayerBean>();

	private static int widthOfPlaceHolder = 0;
	private static int heightOfPlaceHolder = 0;

	private int m_idOfPLayerIconBeingEdited = -1;

	private float m_ratioHeight;
	private int m_finalHeightPitch;

	private MatchBean m_match;
	private QuickAction m_quickAction;
	private PlayerBean m_playerToDelete;
	private View m_playerViewBeingDelete;

	private EditTeamLineUpActivity m_chooseLineUpActivity;

	public SoccerFieldFragment() {
		super();
	}

	public void setMatch(MatchBean m_match) {
		this.m_match = m_match;
	}

	public void setMainActivity(EditTeamLineUpActivity m_mainActivity) {
		this.m_chooseLineUpActivity = m_mainActivity;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		m_root = (FrameLayout) inflater.inflate(R.layout.fragment_soccer_field, null, false);

		m_quickAction = new QuickAction(getSherlockActivity());
		ActionItem deletePlayer = new ActionItem(ID_REMOVE_PLAYERLINEUP, getResources().getString(R.string.delete),
				getResources().getDrawable(android.R.drawable.ic_menu_delete));
		deletePlayer.setSticky(true);
		m_quickAction.addActionItem(deletePlayer);

		// setup the action item click listener
		m_quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(QuickAction quickAction, int pos, int actionId) {
				ActionItem actionItem = quickAction.getActionItem(pos);

				if (actionId == ID_REMOVE_PLAYERLINEUP) {
					m_quickAction.dismiss();
					removePlayer(m_playerToDelete);
					m_playerViewBeingDelete.setTag(null);

				}
			}
		});

		final ImageView iv = (ImageView) m_root.findViewById(R.id.imageField);

		// Draw placeholder for players
		ViewTreeObserver vto = iv.getViewTreeObserver();

		//gets screen dimensions
		final DisplayMetrics metrics = new DisplayMetrics();
		getSherlockActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

		//this happens before the layout is visible
		vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				int newWidth, newHeight, oldHeight, oldWidth;

				//the new width will fit the screen
				newWidth = metrics.widthPixels;

				//so we can scale not proportionally
				oldHeight = iv.getDrawable().getIntrinsicHeight();
				oldWidth = iv.getDrawable().getIntrinsicWidth();
				newHeight = (int) Math.floor((oldHeight * newWidth) / oldWidth);

				iv.setLayoutParams(new FrameLayout.LayoutParams(newWidth, newHeight));
				iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

				//so this only happens once
				iv.getViewTreeObserver().removeGlobalOnLayoutListener(this);

				m_finalHeightPitch = newHeight;
				Log.d(LOG_TAG, "m_finalHeightPitch: " + m_finalHeightPitch);

				m_playersLayout = (RelativeLayout) m_root.findViewById(R.id.playersLayout);

				Drawable placeholder = getResources().getDrawable(R.drawable.player_placeholder);
				int imgWidth = placeholder.getIntrinsicWidth();
				int imgHeight = placeholder.getIntrinsicHeight();

				int marginWidth = (metrics.widthPixels - 4 * imgWidth) / 5;
				int marginHeight = (m_finalHeightPitch - 3 * imgHeight) / 4;

				// Place players on the field. 3 rows. 2 rows of 4 players and 1 rows of 3 players
				for (int k = 1; k <= 11; k++) {
					LayoutInflater myInflater = getSherlockActivity().getLayoutInflater();
					View playerView = myInflater.inflate(R.layout.lineup_player, m_root, false);
					playerView.setOnClickListener(SoccerFieldFragment.this);
					playerView.setOnLongClickListener(SoccerFieldFragment.this);
					playerView.setId(k);
					RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					if (k % 4 != 1) {
						layoutParams.addRule(RelativeLayout.RIGHT_OF, k - 1);
					}

					if (k > 4) {
						layoutParams.addRule(RelativeLayout.BELOW, k - 4);
					}

					TextView textViewName = (TextView) playerView.findViewById(R.id.playerLastName);
					textViewName.getLayoutParams().width = imgWidth;

					layoutParams.setMargins(marginWidth, marginHeight, 0, 0);

					playerView.setLayoutParams(layoutParams);
					m_playersLayout.addView(playerView);

				}

				loadPreviousChoicesIfPresent();
			}
		});

		return m_root;
	}

	public float getRatioHeight() {
		return m_ratioHeight;
	}

	private void addPlayer(PlayerBean player) {

		LineupBean lineupBean = addPlayerOnTheScreen(player);

		DBManager.getInstance().storeBean(lineupBean);
	}

	public LineupBean addPlayerOnTheScreen(PlayerBean player) {

		m_chosenPlayers.add(player);

		View playerView = getSherlockActivity().findViewById(m_idOfPLayerIconBeingEdited);
		TextView lastNameTextView = (TextView) playerView.findViewById(R.id.playerLastName);
		lastNameTextView.setText(player.getSurnameAndName(true, getSherlockActivity()));

		TextView roleAbbrTextView = (TextView) playerView.findViewById(R.id.roleAbbreviation);
		roleAbbrTextView.setText(player.getAbbreviatedRole(getSherlockActivity()));

		if (playerView.getTag() != null) {
			// this player has been replaced, back to the list of possible choices
			m_playersConvocatedOrInTheRoster.add((PlayerBean) playerView.getTag());
			
			// remove the old player from the database
			removeLineupObjectForPlayer((PlayerBean)playerView.getTag());
		}

		if (m_match.getLineupConfigured() == 0) {
			m_match.setLineupConfigured(1);
			DBManager.getInstance().updateBean(m_match);
			Log.d(LOG_TAG, "Post to disable convocation button");
			MyTeamManagerActivity.getBus().post(new TeamLineupSelectionEvent(true));
		}

		
		playerView.setTag(player);

		LineupBean lineupBean = new LineupBean();
		lineupBean.setMatch(m_match);
		lineupBean.setPlayer(player);
		lineupBean.setIdOfCorrespondentView(m_idOfPLayerIconBeingEdited);
		lineupBean.setOnTheBench(LineupBean.NOT_ON_THE_BENCH);
		return lineupBean;
	}

	private void removePlayer(PlayerBean player) {
		m_chosenPlayers.remove(player);

		removeLineupObjectForPlayer(player);

		TextView textViewName = (TextView) m_playerViewBeingDelete.findViewById(R.id.playerLastName);
		textViewName.setText(R.string.btn_click_to_choose_player);

		textViewName.setTag(null);

		TextView roleAbbrTextView = (TextView) m_playerViewBeingDelete.findViewById(R.id.roleAbbreviation);
		roleAbbrTextView.setText("");

		if (m_chosenPlayers.size() == 0) {
			if (m_match.getLineupConfigured() == 1) {
				m_match.setLineupConfigured(0);
				DBManager.getInstance().updateBean(m_match);
				Log.d(LOG_TAG, "Post to enable convocation button");
				MyTeamManagerActivity.getBus().post(new TeamLineupSelectionEvent(false));
			}
		}

		// Put back the player as available for other selection
		if (m_playersConvocatedOrInTheRoster != null) {
			m_playersConvocatedOrInTheRoster.add(player);
			Collections.sort(m_playersConvocatedOrInTheRoster, player.getComparator());
		}

		m_chooseLineUpActivity.updateNotSelectedTextView(m_playersConvocatedOrInTheRoster);
	}

	protected void removeLineupObjectForPlayer(PlayerBean player) {
		DBManager.getInstance().deleteBeanWithWhere(new LineupBean(),
				"player = " + player.getId() + " AND match = " + m_match.getId());
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getSherlockActivity(), PlayerListForTeamLineupActivity.class);
		intent.putExtra(KeyConstants.KEY_MATCH, m_match);
		intent.putExtra(KeyConstants.KEY_PLAYERS_LIST, m_playersConvocatedOrInTheRoster);
		intent.putExtra(KeyConstants.KEY_PLAYERS_ALREADY_IN_THE_LINEUP, m_chosenPlayers);
		m_idOfPLayerIconBeingEdited = v.getId();
		startActivityForResult(intent, KeyConstants.CODE_PLAYER_FOR_LINEUP_CHOSEN);
	}

	public void startSubstitutesSelection() {
		Intent intent = new Intent(getSherlockActivity(), EditSubstitutesActivity.class);
		intent.putExtra(KeyConstants.KEY_MATCH, m_match);

		ArrayList<PlayerBean> substitutesAndNotChosenPlayers = new ArrayList<PlayerBean>(m_playersConvocatedOrInTheRoster);
		substitutesAndNotChosenPlayers.addAll(m_substitutePlayers);
		Collections.sort(substitutesAndNotChosenPlayers, new PlayerBean().getComparator());

		intent.putExtra(KeyConstants.KEY_PLAYERS_LIST, substitutesAndNotChosenPlayers);
		intent.putExtra(KeyConstants.KEY_PLAYERS_ALREADY_IN_THE_LINEUP, m_chosenPlayers);
		startActivityForResult(intent, KeyConstants.CODE_PLAYERS_FOR_BENCH_CHOSEN);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case KeyConstants.CODE_PLAYER_FOR_LINEUP_CHOSEN:
			playerForLineupChosen(resultCode, data);
			break;

		case KeyConstants.CODE_PLAYERS_FOR_BENCH_CHOSEN:

			switch (resultCode) {
			case KeyConstants.RESULT_PLAYERS_ONTHEBENCH_CHOSEN:
				if (data.getExtras() != null) {
					m_substitutePlayers = (ArrayList<PlayerBean>) data.getExtras().get(KeyConstants.KEY_PLAYERS_LIST);
					ArrayList<PlayerBean> notChosenPlayer = (ArrayList<PlayerBean>) data.getExtras().get(
							KeyConstants.KEY_NOT_CHOSEN_PLAYERS);

					for (PlayerBean player : m_substitutePlayers) {
						m_playersConvocatedOrInTheRoster.remove(player);
					}

					// Add the not chosen players back to the global list
					for (PlayerBean player : notChosenPlayer) {
						if ( !m_playersConvocatedOrInTheRoster.contains(player)) {
							m_playersConvocatedOrInTheRoster.add(player);
						}
						
					}

					Collections.sort(m_substitutePlayers, new PlayerBean().getComparator());
					Collections.sort(m_playersConvocatedOrInTheRoster, new PlayerBean().getComparator());

					m_chooseLineUpActivity.updateNotSelectedTextView(m_playersConvocatedOrInTheRoster);
					m_chooseLineUpActivity.updateSubstituteTextView(m_substitutePlayers);
				}

				break;
			}

			break;
		}
	}

	public void playerForLineupChosen(int resultCode, Intent data) {
		switch (resultCode) {
		case KeyConstants.RESULT_PLAYER_FOR_LINEUP_CHOSEN:
			if (m_playersConvocatedOrInTheRoster == null) {
				m_playersConvocatedOrInTheRoster = (ArrayList<PlayerBean>) data.getExtras().get(KeyConstants.KEY_PLAYERS_LIST);
			}

			PlayerBean player = (PlayerBean) data.getExtras().get(KeyConstants.KEY_PLAYER);
			addPlayer(player);

			// Remove the player from the list because already chosen
			m_playersConvocatedOrInTheRoster.remove(player);
			Collections.sort(m_playersConvocatedOrInTheRoster, player.getComparator());

			m_chooseLineUpActivity.updateNotSelectedTextView(m_playersConvocatedOrInTheRoster);
			break;
		}
	}

	public void loadPreviousChoicesIfPresent() {
		
		ArrayList<LineupBean> alreadyStoredLineupPlayers = (ArrayList<LineupBean>) DBManager.getInstance().getListOfBeansWhere(new LineupBean(), "match = " + m_match.getId(), false);

		if (m_match.getNumberOfPlayerConvocated() > 0) {
			ArrayList<ConvocationBean> convocations = (ArrayList<ConvocationBean>) DBManager.getInstance().getListOfBeansWhere(new ConvocationBean(), "match = " + m_match.getId(), false);

			int size = convocations.size();

			Log.d(LOG_TAG, "Number of convocated player: " + size);
			m_playersConvocatedOrInTheRoster = new ArrayList<PlayerBean>();

			ConvocationBean convocation = null;
			for (int k = 0; k < size; k++) {
				convocation = (ConvocationBean) convocations.get(k);

				m_playersConvocatedOrInTheRoster.add(convocation.getPlayer());
				Collections.sort(m_playersConvocatedOrInTheRoster, convocation.getPlayer().getComparator());

				Log.d(LOG_TAG, "Convocated player: " + convocation.getPlayer().getId());

			}
		} else {
			if (alreadyStoredLineupPlayers != null && (alreadyStoredLineupPlayers.size()) > 0) {
				m_playersConvocatedOrInTheRoster = (ArrayList<PlayerBean>)MyTeamManagerDBManager.getInstance().getListOfBeans(new PlayerBean(), true);
			}
			else {
				m_playersConvocatedOrInTheRoster = (ArrayList<PlayerBean>)MyTeamManagerDBManager.getInstance().getListOfBeansWhere(new PlayerBean(), "isDeleted=0", true);
			}
			Collections.sort(m_playersConvocatedOrInTheRoster, new PlayerBean().getComparator());

		}

		
		int size = 0;
		if (alreadyStoredLineupPlayers != null && (size = alreadyStoredLineupPlayers.size()) > 0) {
			LineupBean lineupPlayer = null;
			Log.d(LOG_TAG, "Size of chosen player: " + size);
			for (int k = 0; k < size; k++) {
				lineupPlayer = alreadyStoredLineupPlayers.get(k);
				m_idOfPLayerIconBeingEdited = lineupPlayer.getIdOfCorrespondentView();

				Log.d(LOG_TAG, "Lineup player: " + lineupPlayer.getPlayer().getSurnameAndName(true, getSherlockActivity()));
				
				if (lineupPlayer.getOnTheBench() == LineupBean.ON_THE_BENCH) {
					lineupPlayer.getPlayer().setOnTheBench(true);
					m_substitutePlayers.add(lineupPlayer.getPlayer());
				} else {
					addPlayerOnTheScreen(lineupPlayer.getPlayer());
				}

				m_playersConvocatedOrInTheRoster.remove(lineupPlayer.getPlayer());

			}

			Collections.sort(m_substitutePlayers, new PlayerBean().getComparator());
		}

		m_chooseLineUpActivity.updateNotSelectedTextView(m_playersConvocatedOrInTheRoster);
		m_chooseLineUpActivity.updateSubstituteTextView(m_substitutePlayers);
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	}

	@Override
	public boolean onLongClick(View v) {

		if (v.getTag() != null) {
			m_playerViewBeingDelete = v;
			m_playerToDelete = (PlayerBean) v.getTag();

			if (m_quickAction != null) {
				m_quickAction.show(v);
			}
		}

		return false;
	}

}
