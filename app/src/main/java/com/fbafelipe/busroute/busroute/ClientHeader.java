package com.fbafelipe.busroute.busroute;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by felipe on 5/30/15.
 */
public class ClientHeader {
	private String mUsername;
	private String mPassword;
	private Map<String, String> mExtraParams;
	
	public ClientHeader(String username, String password) {
		mUsername = username;
		mPassword = password;
		mExtraParams = new HashMap<String, String>();
	}
	
	public void addExtraParam(String key, String value) {
		mExtraParams.put(key, value);
	}
	
	public String getUsername() {
		return mUsername;
	}

	public Map<String, String> getExtraParams() {
		return mExtraParams;
	}

	public String getPassword() {
		return mPassword;
	}
}
