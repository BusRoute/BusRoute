package com.fbafelipe.busroute.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.fbafelipe.busroute.MainActivity;
import com.fbafelipe.busroute.R;
import com.fbafelipe.busroute.Utils;
import com.fbafelipe.busroute.adapter.RouteAdapter;
import com.fbafelipe.busroute.busroute.BusRouteManager;
import com.fbafelipe.busroute.busroute.Route;
import com.fbafelipe.busroute.task.FetchRoutesTask;

import java.util.List;

/**
 * Created by felipe on 5/31/15.
 */
public class SearchRouteFragment extends Fragment implements SearchView.OnQueryTextListener,
		FetchRoutesTask.Callback, ListView.OnItemClickListener {
	
	private SearchView mSearchView;
	private ListView mListView;
	private RouteAdapter mAdapter;
	
	private FetchRoutesTask mTask;
	private ProgressDialog mProgressDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search_route, container, false);

		mSearchView = (SearchView) rootView.findViewById(R.id.search_route);
		mListView = (ListView) rootView.findViewById(R.id.route_list);
		
		mAdapter = new RouteAdapter();
		mListView.setAdapter(mAdapter);

		mSearchView.setOnQueryTextListener(this);
		mListView.setOnItemClickListener(this);
		
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();

		ActionBarActivity activity = (ActionBarActivity) getActivity();
		activity.getSupportActionBar().setTitle(R.string.search_route);
	}
	
	@Override
	public void onPause() {
		super.onPause();

		cancelTask();
	}
	
	private void cancelTask() {
		if (mTask != null)
			mTask.cancel(true);

		taskFinished();
	}
	
	private void taskFinished() {
		mTask = null;
		
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		cancelTask();

		BusRouteManager manager = ((MainActivity) getActivity()).getBusRouteManager();
		
		mTask = new FetchRoutesTask(manager, this);
		mTask.execute(query);
		
		mSearchView.clearFocus();
		
		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}
	
	@Override
	public void onFetchRoutesStart() {
		mProgressDialog = Utils.createLoadingDialog(getActivity(), new Runnable() {
			@Override
			public void run() {
				mProgressDialog = null;
				cancelTask();
			}
		});
		mProgressDialog.show();
	}

	@Override
	public void onFetchRoutesSuccess(List<Route> routes) {
		taskFinished();
		mAdapter.setRoutes(routes);
	}

	@Override
	public void onFetchRoutesError() {
		taskFinished();
		Utils.showAlert(getActivity(), R.string.error, R.string.search_route_error, R.string.ok);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Route route = (Route) mAdapter.getItem(position);
		((MainActivity) getActivity()).showRouteDetails(route);
	}
}
