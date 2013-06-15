package com.myteammanager.ui.phone;

import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.Fragment;

import android.content.Intent;
import android.util.Log;

import com.myteammanager.MyTeamManagerActivity;
import com.myteammanager.R;
import com.myteammanager.ui.fragments.LoginFragment;
import com.myteammanager.util.KeyConstants;
import com.myteammanager.util.StringUtil;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class LoginActivity extends BaseSinglePaneActivity {
	
	protected static final String LOG_TAG = LoginActivity.class.getName();
	
	public static final String EXTRA_SHOW_MESSAGE_FOR_OLD_USERS_AND_START_SIGNUP = "startSignup";
	

	@Override
	protected Fragment onCreatePane() {
		return new LoginFragment();
	}

	@Override
	protected void init() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("LoginActivity", "reauestCode: " + requestCode + " resultCode: "+resultCode);
		switch (requestCode) {
		case KeyConstants.CODE_SIGNUP_ACTIVITY:
			if (resultCode == KeyConstants.RESULT_SIGNUP_DONE) {
				setResultAndEnd();
			}
			break;

		case KeyConstants.CODE_FACEBOOK_ACTIVITY:
			ParseFacebookUtils.finishAuthentication(requestCode, resultCode,
					data);
			break;

		}

	}

	protected void setResultAndEnd() {
		setResult(MyTeamManagerActivity.RESULT_LOGIN_DONE);
		finish();

	}
	
	public void startSignup(boolean showOldUserMessage) {
		Intent intent = new Intent(this, SignupActivity.class);
		intent.putExtra(SignupActivity.EXTRA_SHOW_MESSAGE_FOR_OLD_USERS, showOldUserMessage);
		startActivityForResult(intent, KeyConstants.CODE_SIGNUP_ACTIVITY);
	}
	
	public void facebookLogin() {
		ParseFacebookUtils.logIn( this, KeyConstants.CODE_FACEBOOK_ACTIVITY, new LogInCallback() {
			  @Override
			  public void done(ParseUser user, ParseException err) {
				  Log.d(LOG_TAG, "Facebook auth done: " + user);
			    if (user == null) {
			      Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
			    } else if (user.isNew()) {
			    	Log.d("MyApp", "user.isNew(): " + user.isNew());
			    	Log.d("MyApp", "user.user.getEmail(): " + user.getEmail());
			    	Log.d("MyApp", "user.getUsername(): " + user.getUsername());
			    	if ( !StringUtil.isNotEmpty(user.getUsername())) {
			    		AlertDialog.Builder builder = new AlertDialog.Builder(
								LoginActivity.this).setMessage(getString(R.string.msg_login_facebook_error));
			    		builder.setPositiveButton(getString(R.string.label_ok), null);
						builder.show();
			    		return;
			    	}
			      setResultAndEnd();
			    } else {
			    	Log.d("MyApp", "user.isNew(): " + user.isNew());
			    	Log.d("MyApp", "user.user.getEmail(): " + user.getEmail());
			    	Log.d("MyApp", "user.getUsername(): " + user.getUsername());
			    	if ( !StringUtil.isNotEmpty(user.getUsername())) {
			    		AlertDialog.Builder builder = new AlertDialog.Builder(
								LoginActivity.this).setMessage(getString(R.string.msg_login_facebook_error));
			    		builder.setPositiveButton(getString(R.string.label_ok), null);
						builder.show();
			    		return;
			    	}
			      setResultAndEnd();
			    }
			  }
			});
	}
}
