package com.myteammanager.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import org.holoeverywhere.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.EditText;

import org.holoeverywhere.app.Fragment;
import com.myteammanager.MyTeamManagerActivity;
import com.myteammanager.R;
import com.myteammanager.beans.TeamBean;
import com.myteammanager.storage.SettingsManager;
import com.myteammanager.util.KeyConstants;
import com.myteammanager.util.StringUtil;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class WizardEnterTeamNameFragment extends Fragment implements OnClickListener {
	
	private Button m_nextButton;
	private EditText m_teamName;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_wizard_enter_team, null, false);
		
		m_nextButton = (Button)root.findViewById(R.id.buttonNext);
		m_teamName = (EditText)root.findViewById(R.id.teamName);

		
		m_nextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				storeTeamAndDone();
				
			}
		});
		


		return root;
	}
	
	


	@Override
	public void onClick(View v) {

		

	}

	private void storeTeamAndDone() {
		if( !StringUtil.isNotEmpty(m_teamName.getText().toString()) ) {
			m_teamName.setError( getString(R.string.msg_team_name_is_mandatory) );
			return;
		}
		SettingsManager.getInstance(getActivity()).setTeamName(m_teamName.getText().toString());
		
		ParseObject myTeam = new TeamBean(-1, SettingsManager.getInstance(getActivity()).getTeamName() ).getMyTeamParseObject();
		myTeam.put(KeyConstants.FIELD_MYTEAM_USER, ParseUser.getCurrentUser());
		myTeam.saveEventually();
		
		getActivity().setResult(MyTeamManagerActivity.RESULT_WIZARD_TEAM_NAME_ENTERED);
		getActivity().finish();
	}
	


	
	
}
