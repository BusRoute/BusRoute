package com.fbafelipe.busroute.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableLayout;

import com.fbafelipe.busroute.DepartureTableFiller;
import com.fbafelipe.busroute.MainActivity;
import com.fbafelipe.busroute.R;
import com.fbafelipe.busroute.Utils;
import com.fbafelipe.busroute.adapter.RouteStopAdapter;
import com.fbafelipe.busroute.busroute.BusRouteManager;
import com.fbafelipe.busroute.busroute.Route;
import com.fbafelipe.busroute.busroute.RouteDetails;
import com.fbafelipe.busroute.task.FetchRouteDetailsTask;

/**
 * Created by felipe on 5/31/15.
 */
public class RouteDetailsFragment extends Fragment implements FetchRouteDetailsTask.Callback {
	public static String EXTRA_ROUTE = "route";
	
	private Route mRoute;
	
	private View mRootView;
	private ListView mStopsListView;
	private TableLayout mDeparturesTable;
	
	private RouteStopAdapter mRouteStopAdapter;

	private FetchRouteDetailsTask mTask;
	private ProgressDialog mProgressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mRoute = getArguments().getParcelable(EXTRA_ROUTE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_route_details, container, false);

		mStopsListView = (ListView) mRootView.findViewById(R.id.stops_list);
		mDeparturesTable = (TableLayout) mRootView.findViewById(R.id.departures_table);
		
		mRouteStopAdapter = new RouteStopAdapter();
		mStopsListView.setAdapter(mRouteStopAdapter);

		return mRootView;
	}

	@Override
	public void onResume() {
		super.onResume();

		ActionBarActivity activity = (ActionBarActivity) getActivity();
		activity.getSupportActionBar().setTitle(mRoute.getDescription(activity));

		fetchDetails();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		if (mTask != null) {
			mTask.cancel(true);
			mTask = null;
		}
	}

	private void taskFinished() {
		mTask = null;

		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}
	
	private void fetchDetails() {
		BusRouteManager manager = ((MainActivity) getActivity()).getBusRouteManager();
		mTask = new FetchRouteDetailsTask(manager, this);
		mTask.execute(mRoute.getId());
	}

	@Override
	public void onFetchRouteDetailsStart() {
		mProgressDialog = Utils.createLoadingDialog(getActivity(), new Runnable() {
			@Override
			public void run() {
				mProgressDialog = null;
				Activity activity = getActivity();
				if (activity != null)
					activity.onBackPressed();
			}
		});
		mProgressDialog.show();
	}

	@Override
	public void onFetchRouteDetailsSuccess(RouteDetails routeDetails) {
		taskFinished();
		
		// do not update the UI the user already leave this fragment
		if (getActivity() == null)
			return;

		mRouteStopAdapter.setRouteStops(routeDetails.getRouteStops());
		new DepartureTableFiller(getActivity(), mDeparturesTable).populateTable(routeDetails.getDepartures());

		mRootView.requestLayout();
	}

	@Override
	public void onFetchRouteDetailsError() {
		taskFinished();

		Utils.showAlert(getActivity(), R.string.error, R.string.search_route_error, R.string.ok, new Runnable() {
			@Override
			public void run() {
				Activity activity = getActivity();
				if (activity != null)
					activity.onBackPressed();
			}
		});
	}
}
