package com.myteammanager.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import org.holoeverywhere.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.holoeverywhere.widget.LinearLayout;
import android.widget.ScrollView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.myteammanager.R;

public abstract class BaseTwoButtonActionsFormFragment extends BaseFragment  {

	
	protected MenuItem m_menuItem1;
	protected MenuItem m_menuItem2;
	
	protected int m_layoutId;
	
	protected ScrollView m_root;
	
	public BaseTwoButtonActionsFormFragment(int layoutId) {
		super();
		m_layoutId = layoutId;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		m_root = (ScrollView) inflater.inflate(R.layout.fragment_base_form_two_buttons, null, false);
		
		LinearLayout linear = (LinearLayout)m_root.getChildAt(0);
		
		
		View linearLayoutDefinedForm = inflater.inflate(m_layoutId, null, false);
		Log.d(BaseTwoButtonActionsFormFragment.class.getName(), "linearLayoutDefinedForm: " + linearLayoutDefinedForm);
		
		linear.addView(linearLayoutDefinedForm, 0);
		
		customizeMenuItem1(m_root);
		customizeMenuItem2(m_root);
		
		
		return m_root;
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.default_form_menu, menu);
		m_menuItem1 = menu.findItem(R.id.menu_item1);
		m_menuItem2 = menu.findItem(R.id.menu_item2);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_item1:
			clickOnMenuItem1();
			break;

		case R.id.menu_item2:
			clickOnMenuItem2();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected abstract void clickOnMenuItem1();
	
	protected abstract void clickOnMenuItem2();
	
	protected abstract void customizeMenuItem1(View root);
	
	protected abstract void customizeMenuItem2(View root);
	
}
