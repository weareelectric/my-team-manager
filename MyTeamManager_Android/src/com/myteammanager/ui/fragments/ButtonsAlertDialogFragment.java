package com.myteammanager.ui.fragments;

import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import org.holoeverywhere.app.DialogFragment;

import com.myteammanager.R;
import com.myteammanager.ui.ButtonsAlertDialogListener;

public class ButtonsAlertDialogFragment extends DialogFragment {

	private ButtonsAlertDialogListener m_listener;
	private int m_button1Text = -1;
	private int m_button2Text = -1;
	private int m_button3Text = -1;

	private int m_idAlert = -1;

	public ButtonsAlertDialogFragment(int id, String title, String msg, ButtonsAlertDialogListener dialogListener,
			int button1Text, int button3Text) {
		m_idAlert = id;
		m_listener = dialogListener;
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("msg", msg);
		setArguments(args);
		m_button1Text = button1Text;
		m_button3Text = button3Text;
	}

	public ButtonsAlertDialogFragment(String title, String msg, ButtonsAlertDialogListener dialogListener,
			int button1Text, int button3Text) {
		this(-1, title, msg, dialogListener, button1Text, button3Text);
	}

	public ButtonsAlertDialogFragment(String title, String msg, ButtonsAlertDialogListener dialogListener,
			int button1Text, int button2Text, int button3Text) {
		this(-1, title, msg, dialogListener, button1Text, button2Text, button3Text);
	}

	public ButtonsAlertDialogFragment(int id, String title, String msg, ButtonsAlertDialogListener dialogListener,
			int button1Text, int button2Text, int button3Text) {
		this(title, msg, dialogListener, button1Text, button3Text);
		m_button2Text = button2Text;
		this.m_idAlert = id;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String title = getArguments().getString("title");
		String msg = getArguments().getString("msg");

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(msg);

		builder.setPositiveButton(m_button1Text, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				m_listener.button1Pressed(m_idAlert);
				dismiss();
			}
		});
		if (m_button2Text != -1) {
			builder.setNeutralButton(m_button2Text, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					m_listener.button2Pressed(m_idAlert);
					dismiss();
				}
			});
		}

		builder.setNegativeButton(m_button3Text, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				m_listener.button3Pressed(m_idAlert);
				dismiss();
			}

		});

		return builder.create();
	}
}
