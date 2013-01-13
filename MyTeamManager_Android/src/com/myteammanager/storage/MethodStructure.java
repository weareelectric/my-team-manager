package com.myteammanager.storage;

public class MethodStructure {

	public static final int NO_UNIQUE = 0;
	public static final int UNIQUE = 1;

	private String m_className;
	private String m_fieldName;
	private String m_type;
	private int m_unique = NO_UNIQUE;
	private int m_columnIndex = 0;

	public MethodStructure(String className, String fieldName, String m_type, int columnIndex) {
		super();
		this.m_className = className;
		this.m_fieldName = fieldName;
		this.m_type = m_type;
		this.m_columnIndex = columnIndex;
	}

	public String getClassName() {
		return m_className;
	}

	public String getFieldName() {
		return m_fieldName;
	}

	public String getType() {
		return m_type;
	}

	public int getUnique() {
		return m_unique;
	}

	public void setUnique(int unique) {
		this.m_unique = unique;
	}

	public int getColumnIndex() {
		return m_columnIndex;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof MethodStructure) {
			MethodStructure toCompare = (MethodStructure) o;
			if (toCompare.getClassName().equals(getClassName()) && toCompare.getFieldName().equals(getFieldName())
					&& toCompare.getType().equals(getType()) && toCompare.getUnique() == getUnique()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "className: " + getClassName() + " fieldName: " + getFieldName() + " type: " + getType() + " indexType: "
				+ getUnique();
	}

}
