package com.fbafelipe.busroute;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.fbafelipe.busroute.busroute.BusRouteManager;
import com.fbafelipe.busroute.busroute.Route;
import com.fbafelipe.busroute.fragment.RouteDetailsFragment;
import com.fbafelipe.busroute.fragment.SearchRouteFragment;


public class MainActivity extends ActionBarActivity implements FragmentManager.OnBackStackChangedListener {
	private BusRouteManager mManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mManager = new BusRouteManager();
		
		getFragmentManager().addOnBackStackChangedListener(this);
		
		if (savedInstanceState == null)
			setupSearchRouteFragment();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onBackPressed() {
		FragmentManager fragmentManager = getFragmentManager();
		if (fragmentManager.getBackStackEntryCount() > 0) {
			fragmentManager.popBackStack();
			return;
		}
		
		super.onBackPressed();
	}
	
	@Override
	public void onBackStackChanged() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(getFragmentManager().getBackStackEntryCount() > 0);
	}
	
	public BusRouteManager getBusRouteManager() {
		return mManager;
	}
	
	private void setupSearchRouteFragment() {
		Fragment fragment = new SearchRouteFragment();
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.fragment_container, fragment);
		fragmentTransaction.commit();
	}
	
	public void showRouteDetails(Route route) {
		Fragment fragment = new RouteDetailsFragment();
		
		Bundle arguments = new Bundle();
		arguments.putInt(RouteDetailsFragment.EXTRA_ROUTE_ID, route.getId());
		arguments.putString(RouteDetailsFragment.EXTRA_ROUTE_DESCRIPTION, route.getDescription(this));
		fragment.setArguments(arguments);

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_container, fragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commitAllowingStateLoss();
	}
}
