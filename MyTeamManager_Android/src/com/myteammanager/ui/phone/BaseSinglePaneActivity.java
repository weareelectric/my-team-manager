package com.myteammanager.ui.phone;

import org.holoeverywhere.app.ProgressDialog;
import android.os.Bundle;
import org.holoeverywhere.app.Fragment;

import com.myteammanager.BaseActivity;
import com.myteammanager.R;

public abstract class BaseSinglePaneActivity extends BaseActivity {
	private Fragment mFragment;
	protected ProgressDialog pd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_singlepane_empty);

		if (savedInstanceState == null) {
			mFragment = onCreatePane();
			mFragment.setArguments(intentToFragmentArguments(getIntent()));

			getSupportFragmentManager().beginTransaction().add(R.id.root_container, mFragment).commit();
		}

		init();
	}

	protected void showProgressDialog(final String text) {
		runOnUiThread(new Runnable() {
			public void run() {
				pd = ProgressDialog.show(BaseSinglePaneActivity.this, "", text, true);
			}
		});

	}

	protected void cancelProgressDialog() {
		runOnUiThread(new Runnable() {
			public void run() {
				if (pd != null) {
					pd.cancel();
				}
			}
		});

	}

	/**
	 * Called in <code>onCreate</code> when the fragment constituting this activity is needed. The returned fragment's
	 * arguments will be set to the intent used to invoke this activity.
	 */
	protected abstract Fragment onCreatePane();

	protected abstract void init();
}