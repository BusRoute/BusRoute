package com.fbafelipe.busroute.busroute;

import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.jar.JarOutputStream;

/**
 * Created by felipe on 5/30/15.
 */
public class BusRouteManager {
	private static final String USERNAME = "WKD4N7YMA1uiM8V";
	private static final String PASSWORD = "DtdTtzMLQlA0hk2C1Yi5pLyVIlAQ68";
	private static final String ENVIRONMENT_KEY = "X-AppGlu-Environment";
	private static final String ENVIRONMENT_VALUE = "staging";
	
	public static final String URL_ROUTES_BY_STOP_NAME = "https://api.appglu.com/v1/queries/findRoutesByStopName/run";
	public static final String URL_ROUTE_STOPS = "https://api.appglu.com/v1/queries/findStopsByRouteId/run";
	public static final String URL_ROUTE_DEPARTURES = "https://api.appglu.com/v1/queries/findDeparturesByRouteId/run";
	
	private Client mClient;
	private ClientHeader mHeader;
	
	public BusRouteManager() {
		mClient = new ClientImpl();
		mHeader = createClientHeader();
	}
	
	public BusRouteManager(Client client, ClientHeader header) {
		mClient = client;
		mHeader = header;
	}
	
	private ClientHeader createClientHeader() {
		ClientHeader clientHeader = new ClientHeader(USERNAME, PASSWORD);
		clientHeader.addExtraParam(ENVIRONMENT_KEY, ENVIRONMENT_VALUE);
		return clientHeader;
	}
	
	public List<Route> requestRoutesByStopName(String nameQuery) throws ClientException {
		try {
			JSONObject params = createParams(new Pair<>("stopName", "%" + nameQuery + "%"));
			JSONObject response = mClient.post(URL_ROUTES_BY_STOP_NAME, mHeader, params);
			checkForError(response);
			
			List<Route> result = new ArrayList<>();
			
			JSONArray rows = response.getJSONArray("rows");
			for (int i = 0; i < rows.length(); ++i) {
				JSONObject route = rows.getJSONObject(i);
				result.add(new Route(route.getInt("id"), route.getString("shortName"), route.getString("longName")));
			}
			
			return result;
		}
		catch (Exception error) {
			throw new ClientException(error);
		}
	}

	public List<RouteStop> requestRouteStops(int routeId) throws ClientException {
		try {
			JSONObject params = createParams(new Pair<>("routeId", String.valueOf(routeId)));
			JSONObject response = mClient.post(URL_ROUTE_STOPS, mHeader, params);
			checkForError(response);

			List<RouteStop> result = new ArrayList<>();

			JSONArray rows = response.getJSONArray("rows");
			for (int i = 0; i < rows.length(); ++i) {
				JSONObject routeStop = rows.getJSONObject(i);
				result.add(new RouteStop(routeStop.getString("name"), routeStop.getInt("sequence")));
			}

			Collections.sort(result, new Comparator<RouteStop>() {
				@Override
				public int compare(RouteStop a, RouteStop b) {
					return a.getSequence() - b.getSequence();
				}
			});

			return result;
		}
		catch (Exception error) {
			throw new ClientException(error);
		}
	}

	public List<Departure> requestRouteDepartures(int routeId) throws ClientException {
		try {
			JSONObject params = createParams(new Pair<>("routeId", String.valueOf(routeId)));
			JSONObject response = mClient.post(URL_ROUTE_DEPARTURES, mHeader, params);
			checkForError(response);
			
			List<Departure> result = new ArrayList<>();

			JSONArray rows = response.getJSONArray("rows");
			for (int i = 0; i < rows.length(); ++i) {
				JSONObject departure = rows.getJSONObject(i);
				
				Calendar calendar = Calendar.parseCalendar(departure.getString("calendar"));
				result.add(new Departure(calendar, departure.getString("time")));
			}
			
			return result;
		}
		catch (Exception error) {
			throw new ClientException(error);
		}
	}
	
	private void checkForError(JSONObject response) throws ClientException {
		if (response.has("error")) {
			JSONObject error = response.optJSONObject("error");
			if (error != null && error.optString("message") != null) {
				throw new ClientException(error.optString("message"));
			}
			throw new ClientException();
		}
	}
	
	private JSONObject createParams(Pair<String, String>... inputParams) throws JSONException {
		JSONObject requestParams = new JSONObject();
		JSONObject params = new JSONObject();
		
		for (Pair<String, String> param : inputParams) {
			params.put(param.first, param.second);
		}
		requestParams.put("params", params);
		
		return requestParams;
	}
}
