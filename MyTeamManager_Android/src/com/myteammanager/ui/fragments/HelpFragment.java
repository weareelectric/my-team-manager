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

public class HelpFragment extends BaseFragment {

	private View m_root;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		m_root = inflater.inflate(R.layout.fragment_help, null);
		
		return m_root;

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
