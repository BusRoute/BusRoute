package com.fbafelipe.busroute.busroute;

import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * Created by felipe on 5/30/15.
 */
public class ClientImpl implements Client {
	private static final String DEFAULT_ENCODING = "UTF-8";
	
	private HttpClient mClient;
	
	public ClientImpl() {
		mClient = new DefaultHttpClient();
	}

	public ClientImpl(HttpClient client) {
		mClient = client;
	}
	
	@Override
	public JSONObject post(String url, ClientHeader header, JSONObject params) throws ClientException {
		try {
			HttpPost request = new HttpPost(url);
			addHeaders(request, header);
			request.setEntity(new StringEntity(params.toString(), DEFAULT_ENCODING));

			HttpResponse response = mClient.execute(request);
			
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
				throw new ClientException("Response status " + response.getStatusLine().getStatusCode());
			
			String responseStr = EntityUtils.toString(response.getEntity(), DEFAULT_ENCODING);
			
			return new JSONObject(responseStr);
		}
		catch (Exception error) {
			throw new ClientException(error);
		}
	}
	
	private void addHeaders(HttpPost request, ClientHeader header) {
		String rawToken = header.getUsername() + ":" + header.getPassword();
		request.addHeader("Authorization", "Basic " + Base64.encodeToString(rawToken.getBytes(), Base64.NO_WRAP));
		request.addHeader("Content-Type", "application/json");

		for (Map.Entry<String, String> param : header.getExtraParams().entrySet()) {
			request.addHeader(param.getKey(), param.getValue());
		}
	}
}
