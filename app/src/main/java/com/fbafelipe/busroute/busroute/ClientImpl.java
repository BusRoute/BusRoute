package com.fbafelipe.busroute.busroute;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

/**
 * Created by felipe on 5/30/15.
 */
public class ClientImpl implements Client {
	private HttpClient mClient;
	
	public ClientImpl() {
		mClient = new DefaultHttpClient();
	}

	public ClientImpl(HttpClient client) {
		mClient = client;
	}
	
	@Override
	public JSONObject post(String url, ClientHeader header, JSONObject params) throws ClientException {
		// TODO
		return null;
	}
}
