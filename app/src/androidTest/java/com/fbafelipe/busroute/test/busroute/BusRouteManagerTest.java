package com.fbafelipe.busroute.test.busroute;

import android.test.AndroidTestCase;

import com.fbafelipe.busroute.busroute.BusRouteManager;
import com.fbafelipe.busroute.busroute.Calendar;
import com.fbafelipe.busroute.busroute.Client;
import com.fbafelipe.busroute.busroute.ClientException;
import com.fbafelipe.busroute.busroute.ClientHeader;
import com.fbafelipe.busroute.busroute.Departure;
import com.fbafelipe.busroute.busroute.Route;
import com.fbafelipe.busroute.busroute.RouteStop;
import com.fbafelipe.busroute.test.TestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by felipe on 5/30/15.
 */
public class BusRouteManagerTest extends AndroidTestCase {
	private BusRouteManager mManager;
	private Client mClient;
	
	@Override
	public void setUp() {
		ClientHeader header = new ClientHeader("user", "pwd");
		
		mClient = mock(Client.class);
		mManager = new BusRouteManager(mClient, header);
	}
	
	public void testRequestRoutesByStopNameSuccess() throws Exception {
		setupClientResponse(BusRouteManager.URL_ROUTES_BY_STOP_NAME, TestUtils.getAssetContent(this, "response_routes1.txt"));
		
		List<Route> routes = mManager.requestRoutesByStopName("Test");
		
		assertEquals(2, routes.size());
		
		Route route = routes.get(0);
		assertEquals(1, route.getId());
		assertEquals("111", route.getShortName());
		assertEquals("Test Route", route.getLongName());
		
		route = routes.get(1);
		assertEquals(2, route.getId());
		assertEquals("222", route.getShortName());
		assertEquals("Test Route 2", route.getLongName());
	}

	public void testRequestRoutesByStopNameClientError() throws Exception {
		setupClientError(BusRouteManager.URL_ROUTES_BY_STOP_NAME);
		try {
			mManager.requestRoutesByStopName("Test");
			fail("ClientException not thrown");
		}
		catch (ClientException error) {}
	}

	public void testRequestRoutesByStopNameErrorResponse() throws Exception {
		setupClientResponse(BusRouteManager.URL_ROUTES_BY_STOP_NAME, TestUtils.getAssetContent(this, "response_error1.txt"));
		try {
			mManager.requestRoutesByStopName("Test");
			fail("ClientException not thrown");
		}
		catch (ClientException error) {}
	}

	public void testRequestRouteStopsSuccess() throws Exception {
		setupClientResponse(BusRouteManager.URL_ROUTE_STOPS, TestUtils.getAssetContent(this, "response_routeStops1.txt"));
		
		List<RouteStop> stops = mManager.requestRouteStops(1);
		
		assertEquals(3, stops.size());
		assertEquals("Stop 1", stops.get(0).getName());
		assertEquals("Stop 2", stops.get(1).getName());
		assertEquals("Stop 3", stops.get(2).getName());
	}

	public void testRequestRouteStopsSuccessSort() throws Exception {
		setupClientResponse(BusRouteManager.URL_ROUTE_STOPS, TestUtils.getAssetContent(this, "response_routeStops2.txt"));

		List<RouteStop> stops = mManager.requestRouteStops(1);

		assertEquals(3, stops.size());
		assertEquals("Stop 1", stops.get(0).getName());
		assertEquals("Stop 2", stops.get(1).getName());
		assertEquals("Stop 3", stops.get(2).getName());
	}

	public void testRequestRouteStopsClientError() throws Exception {
		setupClientError(BusRouteManager.URL_ROUTE_STOPS);
		try {
			mManager.requestRouteStops(42);
			fail("ClientException not thrown");
		}
		catch (ClientException error) {}
	}

	public void testRequestRouteStopsErrorResponse() throws Exception {
		setupClientResponse(BusRouteManager.URL_ROUTE_STOPS, TestUtils.getAssetContent(this, "response_error1.txt"));
		try {
			mManager.requestRouteStops(42);
			fail("ClientException not thrown");
		}
		catch (ClientException error) {}
	}

	public void testRequestRouteDeparturesSuccess() throws Exception {
		setupClientResponse(BusRouteManager.URL_ROUTE_DEPARTURES, TestUtils.getAssetContent(this, "response_routeDepartures1.txt"));

		List<Departure> departures = mManager.requestRouteDepartures(1);

		assertEquals(3, departures.size());
		
		Departure departure = departures.get(0);
		assertEquals(Calendar.WEEKDAY, departure.getCalendar());
		assertEquals("05:59", departure.getTime());

		departure = departures.get(1);
		assertEquals(Calendar.SATURDAY, departure.getCalendar());
		assertEquals("07:28", departure.getTime());

		departure = departures.get(2);
		assertEquals(Calendar.SUNDAY, departure.getCalendar());
		assertEquals("23:25", departure.getTime());
	}

	public void testRequestRouteDeparturesClientError() throws Exception {
		setupClientError(BusRouteManager.URL_ROUTE_DEPARTURES);
		try {
			mManager.requestRouteDepartures(42);
			fail("ClientException not thrown");
		}
		catch (ClientException error) {}
	}

	public void testRequestRouteDeparturesErrorResponse() throws Exception {
		setupClientResponse(BusRouteManager.URL_ROUTE_DEPARTURES, TestUtils.getAssetContent(this, "response_error1.txt"));
		try {
			mManager.requestRouteDepartures(42);
			fail("ClientException not thrown");
		}
		catch (ClientException error) {}
	}
	
	public void setupClientResponse(String url, String response) throws ClientException, JSONException {
		JSONObject reponseJson = new JSONObject(response);
		when(mClient.post(eq(url), any(ClientHeader.class), any(JSONObject.class))).thenReturn(reponseJson);
	}

	private void setupClientError(String url) throws ClientException {
		when(mClient.post(eq(url), any(ClientHeader.class), any(JSONObject.class))).thenThrow(new ClientException());
	}
}
