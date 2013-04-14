package com.myteammanager.ui.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import org.holoeverywhere.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;

import com.myteammanager.R;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.ui.phone.EditTeamLineUpActivity;
import com.myteammanager.util.PlayerUtil;

public class SubstitutesFragment extends BaseFragment {

	private View m_root;

	private TextView m_notSelectedTextView;
	private TextView m_substitutesTextView;
	private Button m_substituteButton;

	private EditTeamLineUpActivity m_chooseLineUpActivity;

	public void setMainActivity(EditTeamLineUpActivity m_mainActivity) {
		this.m_chooseLineUpActivity = m_mainActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		m_root = inflater.inflate(R.layout.fragment_player_for_team_lineup, null);
		m_notSelectedTextView = (TextView) m_root.findViewById(R.id.textViewNotSelectedPlayers);
		m_substitutesTextView = (TextView) m_root.findViewById(R.id.textViewBenchPlayers);
		m_substituteButton = (Button) m_root.findViewById(R.id.btnConfigureSubstitutes);
		m_substituteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				m_chooseLineUpActivity.startActivityToSelectSubstitutes();
			}
		});

		setHasOptionsMenu(true);
		return m_root;

	}

	public void updateNotSelectedTextView(ArrayList<PlayerBean> notSelectedPlayers) {
		m_notSelectedTextView.setText(PlayerUtil.getListOfPlayersFromList(getActivity(), notSelectedPlayers, true));
	}

	public void updateSubstitutesTextView(ArrayList<PlayerBean> players) {
		m_substitutesTextView.setText(PlayerUtil.getListOfPlayersFromList(getActivity(), players, true));
	}

	@Override
	public void button2Pressed(int alertId) {

	}

	@Override
	public void button3Pressed(int alertId) {

	}

	@Override
	public void button1Pressed(int alertId) {

	}

}
