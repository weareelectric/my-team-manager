package com.myteammanager.util;

import java.util.Calendar;
import java.util.Date;

import android.text.InputType;
import com.myteammanager.util.Log;
import android.view.View;
import org.holoeverywhere.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.Spinner;

import com.myteammanager.ui.views.DatePickerEasy;
import com.myteammanager.ui.views.TimePickerEasy;

public class CheckboxWithViewGroupHelper implements OnCheckedChangeListener {

	private static final String LOG_TAG = CheckboxWithViewGroupHelper.class.getName();

	private CheckBox m_checkbox;
	private View m_view;
	private View m_linkedView;

	public CheckboxWithViewGroupHelper(CheckBox checkbox, View view) {
		this(checkbox, view, null);
	}

	public CheckboxWithViewGroupHelper(CheckBox checkbox, View view, View linkedView) {
		m_checkbox = checkbox;
		m_view = view;
		m_linkedView = linkedView;
		m_checkbox.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (m_checkbox.isChecked()) {
			m_view.setVisibility(View.VISIBLE);
		} else {
			m_view.setVisibility(View.GONE);
		}

		// Linked view will have same visibility of the main view
		if (m_linkedView != null) {
			m_linkedView.setVisibility(m_view.getVisibility());
		}

	}

	public void setStringValue(String string) {
		if (StringUtil.isNotEmpty(string)) {
			m_checkbox.setChecked(true);
			((EditText) m_view).setText(string);
		} else {
			m_checkbox.setChecked(false);
			((EditText) m_view).setText("");
		}
	}

	public void setIntValue(int integer) {
		if (integer >= 0) {
			m_checkbox.setChecked(true);
			((Spinner) m_view).setSelection(integer);
		} else {
			m_checkbox.setChecked(false);
			((Spinner) m_view).setSelection(0);
		}
	}

	public void setDateValue(Date date) {
		if (date != null) {
			m_checkbox.setChecked(true);
			((DatePickerEasy) m_view).setDateValue(date);
		} else {
			m_checkbox.setChecked(false);
			((DatePickerEasy) m_view).setDateValue(new Date());
		}
	}

	public void setTimeValue(long time) {
		if (time >= 0) {
			m_checkbox.setChecked(true);
			((TimePickerEasy) m_view).setTimeValue(time);
		} else {
			m_checkbox.setChecked(false);
			((TimePickerEasy) m_view).setTimeValue(new Date().getTime());
		}
	}

	private Object getValueFromView() {
		if (m_checkbox.isChecked()) {
			if (m_view instanceof EditText) {
				EditText localEditText = (EditText) m_view;

				return localEditText.getText().toString();
			} else if (m_view instanceof Spinner) {
				Spinner localSpinner = (Spinner) m_view;
				return new Integer(localSpinner.getSelectedItemPosition());
			} else if (m_view instanceof DatePickerEasy) {
				DatePickerEasy localPicker = (DatePickerEasy) m_view;
				return localPicker.getDateValue();
			} else if (m_view instanceof TimePickerEasy) {
				TimePickerEasy localPicker = (TimePickerEasy) m_view;
				return new Long(localPicker.getTimeValue());
			} else
				return null;
		} else {
			return null;
		}
	}

	public int getIntValueFromView() {
		Object valueFromView = getValueFromView();
		if (valueFromView == null) {
			return -1;
		}
		return ((Integer) valueFromView).intValue();
	}

	public String getStringValueFromView() {
		Object valueFromView = getValueFromView();
		if (valueFromView == null) {
			return "";
		}
		return ((String) valueFromView);
	}

	public Date getDateValueFromView() {
		Object valueFromView = getValueFromView();
		if (valueFromView == null) {
			return null;
		}
		return ((Date) valueFromView);
	}

	public long getTimeValueFromView() {
		Object valueFromView = getValueFromView();
		if (valueFromView == null) {
			return -1;
		}
		return ((Long) valueFromView).longValue();
	}

	public void setCalendarForTimePicker(Calendar calendar) {

	}
}
