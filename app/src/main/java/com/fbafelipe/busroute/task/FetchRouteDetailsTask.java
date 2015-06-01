package com.fbafelipe.busroute.task;

import android.os.AsyncTask;
import android.util.Log;

import com.fbafelipe.busroute.busroute.BusRouteManager;
import com.fbafelipe.busroute.busroute.ClientException;
import com.fbafelipe.busroute.busroute.Departure;
import com.fbafelipe.busroute.busroute.RouteDetails;
import com.fbafelipe.busroute.busroute.RouteStop;

import java.util.List;

/**
 * Created by felipe on 5/31/15.
 */
public class FetchRouteDetailsTask extends AsyncTask<Integer, Void, RouteDetails> {
	private static final String TAG = FetchRouteDetailsTask.class.getSimpleName();

	private BusRouteManager mManager;
	private Callback mCallback;

	public FetchRouteDetailsTask(BusRouteManager manager, Callback callback) {
		mManager = manager;
		mCallback = callback;
	}

	@Override
	protected void onPreExecute() {
		if (mCallback != null)
			mCallback.onFetchRouteDetailsStart();
	}

	@Override
	protected RouteDetails doInBackground(Integer ... params) {
		try {
			int routeId = params[0];
			List<RouteStop> stops = mManager.requestRouteStops(routeId);
			List<Departure> departures = mManager.requestRouteDepartures(routeId);
			return new RouteDetails(stops, departures);
		}
		catch (ClientException error) {
			Log.e(TAG, "", error);
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(RouteDetails routeDetails) {
		if (mCallback != null) {
			if (routeDetails != null)
				mCallback.onFetchRouteDetailsSuccess(routeDetails);
			else
				mCallback.onFetchRouteDetailsError();
		}
	}
	
	@Override
	public void onCancelled() {
		mCallback = null;
	}
	
	public interface Callback {
		public void onFetchRouteDetailsStart();
		public void onFetchRouteDetailsSuccess(RouteDetails routeDetails);
		public void onFetchRouteDetailsError();
	}
}
