package com.myteammanager.adapter.sectionindexes;

public class SectionIndex {

	protected int m_position;
	protected String m_label;
	
	public SectionIndex(String sectionLabel, int position) {
		super();
		this.m_label = sectionLabel;
		this.m_position = position;
	}


	public int getPosition() {
		return m_position;
	}


	public String toString() {
		return this.m_label;
	}
	
	
}
