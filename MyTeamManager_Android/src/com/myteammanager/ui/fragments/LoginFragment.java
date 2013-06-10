package com.myteammanager.ui.fragments;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.EditText;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.myteammanager.MyTeamManagerActivity;
import com.myteammanager.R;
import com.myteammanager.ui.phone.SignupActivity;
import com.myteammanager.util.KeyConstants;
import com.myteammanager.util.StringUtil;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class LoginFragment extends BaseFragment {

	private String LOG_TAG = LoginFragment.class.getName();
	
	protected View m_root;

	private EditText m_emailEditText;
	private EditText m_passwordEditText;
	private Button m_signupBtn;
	private Button m_facebookBtn;

	private Button m_logintBtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		m_root = inflater.inflate(R.layout.fragment_login, null);
		m_emailEditText = (EditText) m_root.findViewById(R.id.editTextEmail);
		m_passwordEditText = (EditText) m_root
				.findViewById(R.id.editTextPassword);

		m_logintBtn = (Button) m_root.findViewById(R.id.btnLogin);
		m_logintBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				login();
			}
		});
		
		m_signupBtn = (Button)m_root.findViewById(R.id.btnRegister);
		m_signupBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), SignupActivity.class);
				startActivityForResult(intent, KeyConstants.CODE_SIGNUP_ACTIVITY);
			}
		});
		
		m_facebookBtn = (Button)m_root.findViewById(R.id.btnLoginFacebook);
		m_facebookBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ParseFacebookUtils.logIn(getActivity(), KeyConstants.CODE_FACEBOOK_ACTIVITY, new LogInCallback() {
					  @Override
					  public void done(ParseUser user, ParseException err) {
						  Log.d(LOG_TAG, "Facebook auth done: " + user);
					    if (user == null) {
					      Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
					    } else if (user.isNew()) {
					      setResultAndEnd();
					    } else {
					      setResultAndEnd();
					    }
					  }
					});
			}
		});

		return m_root;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	private void login() {
		String email = m_emailEditText.getText().toString();
		String password = m_passwordEditText.getText().toString();
		Log.d(LOG_TAG, "email: " + email);

		if (!StringUtil.isNotEmpty(email)) {
			m_emailEditText
					.setError(getString(R.string.msg_email_is_mandatory));
			return;
		}

		if (!StringUtil.validEmail(email)) {
			m_emailEditText
					.setError(getString(R.string.msg_email_is_mandatory));
			return;
		}

		if (!StringUtil.isNotEmpty(password)
				|| (password != null && password.length() < 4)) {
			m_passwordEditText
					.setError(getString(R.string.msg_password_is_mandatory));
			return;
		}

		showProgressDialog(getString(R.string.dialog_waiting_sending_data));

		ParseUser.logInInBackground(email, password, new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				cancelProgressDialog();
				if (user != null) {
					setResultAndEnd();

				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity()).setMessage(e.getMessage());
					builder.show();
				}
			}
		});


	}
	
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.d(LOG_TAG, "Facebook onActivityResult called: " +requestCode);
		
		switch (requestCode) {
		case KeyConstants.CODE_SIGNUP_ACTIVITY:
			if (resultCode == KeyConstants.RESULT_SIGNUP_DONE) {
				setResultAndEnd();
			}
			break;
			
			
		}

	}

	protected void setResultAndEnd() {
		getActivity()
				.setResult(MyTeamManagerActivity.RESULT_LOGIN_DONE);
		getActivity().finish();
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
