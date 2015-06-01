package com.fbafelipe.busroute.busroute;

import java.util.List;

/**
 * Created by felipe on 5/31/15.
 */
public class RouteDetails {
	private List<RouteStop> mRouteStops;
	private List<Departure> mDepartures;
	
	public RouteDetails(List<RouteStop> stops, List<Departure> departures) {
		mRouteStops = stops;
		mDepartures = departures;
	}

	public List<RouteStop> getRouteStops() {
		return mRouteStops;
	}

	public List<Departure> getDepartures() {
		return mDepartures;
	}
}
