package com.myteammanager.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.myteammanager.listener.DialogListListener;

public class ListDialogFragment extends DialogFragment {

	private Context m_context;
	private String m_title;
	private String m_message;
	private String[] m_labels;
	private DialogListListener m_listener;

	public ListDialogFragment(Context m_context, String m_title, String m_message, String[] m_labels) {
		super();
		this.m_context = m_context;
		this.m_title = m_title;
		this.m_message = m_message;
		this.m_labels = m_labels;
	}

	public void setListener(DialogListListener m_listener) {
		this.m_listener = m_listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog d = new Dialog(m_context);
		d.setTitle(m_title);

		final ListView modeList = new ListView(m_context);
		modeList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				m_listener.listItemClicked(position);

			}

		});

		ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(m_context, android.R.layout.simple_list_item_1,
				android.R.id.text1, m_labels) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);

				TextView textView = (TextView) view.findViewById(android.R.id.text1);

				/*YOUR CHOICE OF COLOR*/
				textView.setTextColor(Color.WHITE);

				return view;
			}
		};

		modeList.setAdapter(modeAdapter);

		d.setContentView(modeList);

		return d;

	}
}
