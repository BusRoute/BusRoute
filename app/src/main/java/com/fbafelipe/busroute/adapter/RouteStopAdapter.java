package com.fbafelipe.busroute.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fbafelipe.busroute.R;
import com.fbafelipe.busroute.busroute.RouteStop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by felipe on 5/31/15.
 */
public class RouteStopAdapter extends BaseAdapter {
	private List<RouteStop> mRouteStops = new ArrayList<>();

	public void setRouteStops(List<RouteStop> stops) {
		mRouteStops = stops;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mRouteStops.size();
	}

	@Override
	public Object getItem(int position) {
		return mRouteStops.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = getOrCreateHolder(parent.getContext(), convertView);
		
		RouteStop routeStop = mRouteStops.get(position);
		holder.routeStop.setText(parent.getContext().getString(R.string.route_stop, routeStop.getSequence(), routeStop.getName()));
		
		return holder.convertView;
	}

	private Holder getOrCreateHolder(Context context, View convertView) {
		Holder holder;

		if (convertView == null) {
			holder = new Holder();
			holder.convertView = LayoutInflater.from(context).inflate(R.layout.item_route_stop, null);
			holder.routeStop = (TextView) holder.convertView.findViewById(R.id.route_stop);
			holder.convertView.setTag(holder);
		}
		else
			holder = (Holder) convertView.getTag();

		return holder;
	}

	private static class Holder {
		public View convertView;
		public TextView routeStop;
	}
}
