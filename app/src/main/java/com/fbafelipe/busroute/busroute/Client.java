package com.fbafelipe.busroute.busroute;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by felipe on 5/30/15.
 */
public interface Client {
	public JSONObject post(String url, ClientHeader header, JSONObject params) throws ClientException;
}
