package com.myteammanager.ui.fragments;

import java.util.ArrayList;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import org.holoeverywhere.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.TextView;

import com.myteammanager.R;
import com.myteammanager.adapter.RecipientListAdapterWithCheckbox;
import com.myteammanager.beans.BaseBean;
import com.myteammanager.beans.PlayerBean;
import com.myteammanager.specializedStorage.MyTeamManagerDBManager;
import com.myteammanager.storage.SettingsManager;
import com.myteammanager.util.KeyConstants;
import com.myteammanager.util.StringUtil;

public class SendMessageFragment extends BaseFragment {
	
	private String LOG_TAG = SendMessageFragment.class.getName();
	
	protected ArrayList<String> m_playersWithEmail = new ArrayList<String>();
	protected ArrayList<String> m_playersWithPhone = new ArrayList<String>();
	protected ArrayList<PlayerBean> m_playersWithNoAddress = new ArrayList<PlayerBean>();
	protected ArrayList<PlayerBean> m_chosenRecipients = new ArrayList<PlayerBean>();
	protected Button m_buttonRecipientsNoAddress;
	protected String m_msgText;
	
	protected View m_root;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		m_root = inflater.inflate(R.layout.fragment_send_message, null);
		
		TextView textViewSMSRecipient = (TextView)m_root.findViewById(R.id.textViewSMSRecipient);
		TextView textViewEmailRecipient = (TextView)m_root.findViewById(R.id.textViewEmailRecipient);
		
		if ( m_playersWithPhone.size() > 1 ) {
			textViewSMSRecipient.setText(getResources().getString(R.string.label_number_of_recipient_plural, m_playersWithPhone.size()));
		}
		else {
			textViewSMSRecipient.setText(getResources().getString(R.string.label_number_of_recipient_single, m_playersWithPhone.size()));
		}
		
		if ( m_playersWithEmail.size() > 1 ) {
			textViewEmailRecipient.setText(getResources().getString(R.string.label_number_of_recipient_plural, m_playersWithEmail.size()));
		}
		else {
			textViewEmailRecipient.setText(getResources().getString(R.string.label_number_of_recipient_single, m_playersWithEmail.size()));
		}
		
		m_buttonRecipientsNoAddress = (Button)m_root.findViewById(R.id.buttonRecipientNoAdresse);
		
		if ( m_playersWithNoAddress.size() > 1 ) {
			m_buttonRecipientsNoAddress.setText(getResources().getString(R.string.message_recipient_with_no_address_plural, m_playersWithNoAddress.size()));
		}
		else {
			m_buttonRecipientsNoAddress.setText(getResources().getString(R.string.message_recipient_with_no_address_singular, m_playersWithNoAddress.size()));
		}
		
		m_buttonRecipientsNoAddress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());

				String[] playersNoAdress = new String[m_playersWithNoAddress.size()];
				
				int k = 0;
				for ( PlayerBean player : m_playersWithNoAddress ) {
					playersNoAdress[k] = player.getSurnameAndName(false, getActivity());
					k++;
				}

				builder.setItems(playersNoAdress, null);
				AlertDialog alert = builder.create();
				alert.setTitle(getResources().getString(R.string.title_players_no_address));
				alert.show();
			}
		});
		
		final EditText editTextMessage = (EditText)m_root.findViewById(R.id.editTextMessage);
		if (StringUtil.isNotEmpty(m_msgText)) {
			editTextMessage.setText(m_msgText);
		}
		
		Button sendSMSButton = (Button)m_root.findViewById(R.id.buttonSMS);
		sendSMSButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				StringBuffer sb = new StringBuffer();
				int size = m_playersWithPhone.size();
				int k = 0;
				for ( String phone : m_playersWithPhone ) {
					sb.append(phone);
					if ( k != size - 1) {
						sb.append(";");
					}
					k++;
				}
				
				
				 Uri uri = Uri.parse("smsto:" + sb.toString());
				    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
				    intent.putExtra("sms_body", editTextMessage.getText().toString());  
				    startActivity(intent);
				
			}
		});
		
		Button sendEmailButton = (Button)m_root.findViewById(R.id.buttonEmail);
		sendEmailButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				int size = m_playersWithEmail.size();
				String[] emails = new String[size];
				int k = 0;
				for ( String email : m_playersWithEmail ) {
					emails[k] = email;
					k++;
				}
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_EMAIL, emails);
				intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.title_subject_free_text_email, SettingsManager.getInstance(getActivity()).getTeamName()));
				intent.putExtra(Intent.EXTRA_TEXT, editTextMessage.getText().toString());
				startActivity(Intent.createChooser(intent, ""));
				
			}
		});
		
		
		return m_root;
	}
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		Bundle extra = getActivity().getIntent().getExtras();
		m_chosenRecipients = (ArrayList<PlayerBean>)extra.get(KeyConstants.KEY_SELECTED_RECIPIENT);
		m_playersWithNoAddress= (ArrayList<PlayerBean>) MyTeamManagerDBManager.getInstance().getListOfBeansWhere(new PlayerBean(), "(email is null or email = '') and (phone is null or phone = '')", true);
		m_msgText = (String)extra.get(KeyConstants.KEY_MSG_TEXT);
		
		for ( PlayerBean player : m_chosenRecipients ) {
			if ( StringUtil.isNotEmpty(player.getEmail())) {
				m_playersWithEmail.add(player.getEmail());
			}
			
			if ( StringUtil.isNotEmpty(player.getPhone())) {
				m_playersWithPhone.add(player.getPhone());
			}
		}
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
