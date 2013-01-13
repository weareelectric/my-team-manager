package com.myteammanager.storage.exceptions;

public class DBNotInitialized extends RuntimeException {

	public DBNotInitialized(String detailMessage) {
		super(detailMessage);
	}

	
}
