package com.example.questrush;

import com.parse.ParseUser;

public class User {
	public static User getInstance() {
		if (instance == null) {
			instance = new User();
		}
		return instance;
	}

	public static User instance = null;

	private User() {

	}

	private ParseUser mUser;

	public ParseUser getmUser() {
		return mUser;
	}

	public void setmUser(ParseUser mUser) {
		this.mUser = mUser;
	}
}
