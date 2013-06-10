package com.myteammanager.ui.fragments;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.EditText;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.myteammanager.MyTeamManagerActivity;
import com.myteammanager.R;
import com.myteammanager.util.KeyConstants;
import com.myteammanager.util.StringUtil;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupFragment extends BaseFragment {
	
	private String LOG_TAG = SignupFragment.class.getName();
	
	protected View m_root;
	
	private EditText m_fullNameEditText;
	private EditText m_emailEditText;
	private EditText m_passwordEditText;
	
	private Button m_registerNewAccountBtn;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		m_root = inflater.inflate(R.layout.fragment_signup, null);
		m_fullNameEditText = (EditText)m_root.findViewById(R.id.editTextFullname);
		m_emailEditText = (EditText)m_root.findViewById(R.id.editTextEmail);
		m_passwordEditText = (EditText)m_root.findViewById(R.id.editTextPassword);
		
		m_registerNewAccountBtn = (Button)m_root.findViewById(R.id.btnRegister);
		m_registerNewAccountBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				signup();
			}
		});
		
		
		return m_root;
	}
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
	}

	private void signup() {
		String fullName = m_fullNameEditText.getText().toString();
		String email= m_emailEditText.getText().toString();
		String password= m_passwordEditText.getText().toString();
		
		if ( !StringUtil.isNotEmpty(fullName) || ( fullName != null && fullName.length() < 3 ) ) {
			m_fullNameEditText.setError(getString(R.string.msg_full_name_is_mandatory));
			return;
		}
		
		if ( !StringUtil.isNotEmpty(email) ) {
			m_emailEditText.setError(getString(R.string.msg_email_is_mandatory));
			return;
		}
		
		if ( !StringUtil.validEmail(email)) {
			m_emailEditText.setError(getString(R.string.msg_email_is_mandatory));
			return;
		}
		
		if ( !StringUtil.isNotEmpty(password) || ( password != null && password.length() < 4 ) ) {
			m_passwordEditText.setError(getString(R.string.msg_password_is_mandatory));
			return;
		}
		
		showProgressDialog(getString(R.string.dialog_waiting_sending_data));
		ParseUser user = new ParseUser();
		user.setUsername(email);
		user.setPassword(password);
		user.setEmail(email);
		
		user.put("fullname", fullName);
		
		user.signUpInBackground(new SignUpCallback() {
			  public void done(ParseException e) {
				  cancelProgressDialog();
			    if (e == null) {
			    	getActivity().setResult(KeyConstants.RESULT_SIGNUP_DONE);
			    	getActivity().finish();
			    } else {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage(e.getMessage());
					builder.show();
			    }
			  }
			});
		
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
