package com.fbafelipe.busroute.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fbafelipe.busroute.R;

/**
 * Created by felipe on 5/31/15.
 */
public class RouteDetailsFragment extends Fragment {
	public static String EXTRA_ROUTE = "route";
	
	private int mRouteId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mRouteId = getArguments().getInt(EXTRA_ROUTE);
	}
	
}
