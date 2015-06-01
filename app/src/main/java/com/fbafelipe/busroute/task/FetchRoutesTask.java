package com.fbafelipe.busroute.task;

import android.os.AsyncTask;
import android.util.Log;

import com.fbafelipe.busroute.busroute.BusRouteManager;
import com.fbafelipe.busroute.busroute.ClientException;
import com.fbafelipe.busroute.busroute.Route;

import java.util.List;

/**
 * Created by felipe on 5/31/15.
 */
public class FetchRoutesTask extends AsyncTask<String, Void, List<Route> > {
	private static final String TAG = FetchRoutesTask.class.getSimpleName();
	
	private BusRouteManager mManager;
	private Callback mCallback;

	public FetchRoutesTask(BusRouteManager manager, Callback callback) {
		mManager = manager;
		mCallback = callback;
	}

	@Override
	protected void onPreExecute() {
		if (mCallback != null)
			mCallback.onFetchRoutesStart();
	}

	@Override
	protected List<Route> doInBackground(String ... params) {
		try {
			return mManager.requestRoutesByStopName(params[0]);
		}
		catch (ClientException error) {
			Log.e(TAG, "", error);
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(List<Route> routes) {
		if (mCallback != null) {
			if (routes != null)
				mCallback.onFetchRoutesSuccess(routes);
			else
				mCallback.onFetchRoutesError();
		}
	}
	
	@Override
	public void onCancelled() {
		mCallback = null;
	}
	
	public interface Callback {
		public void onFetchRoutesStart();
		public void onFetchRoutesSuccess(List<Route> routes);
		public void onFetchRoutesError();
	}
}