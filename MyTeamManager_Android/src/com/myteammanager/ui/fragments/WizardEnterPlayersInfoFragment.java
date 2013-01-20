package com.myteammanager.ui.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;
import com.myteammanager.MyTeamManagerActivity;
import com.myteammanager.R;
import com.myteammanager.beans.ContactBean;
import com.myteammanager.contacts.PhonebookManager;
import com.myteammanager.exceptions.NoDataException;
import com.myteammanager.ui.phone.ChoosePlayerFromContactsActivity;
import com.myteammanager.ui.phone.WizardEnterPlayersInfoActivity;
import com.myteammanager.util.KeyConstants;

public class WizardEnterPlayersInfoFragment extends SherlockFragment {
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_enter_player_info, null, false);
		
		Button yesButton = (Button)root.findViewById(R.id.buttonYes);
		yesButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				getSherlockActivity().setResult(KeyConstants.RESULT_START_CONTACTS_SELECTION);
				getSherlockActivity().finish();
				
			}
		});
		
		Button noButton = (Button)root.findViewById(R.id.buttonNo);
		noButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				getSherlockActivity().setResult(MyTeamManagerActivity.RESULT_ENTER_PLAYERS_INFO_START);
				getSherlockActivity().finish();
				
			}
		});
		

		
		return root;
	}



	
	
}
