package com.fbafelipe.busroute.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fbafelipe.busroute.R;
import com.fbafelipe.busroute.busroute.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by felipe on 5/31/15.
 */
public class RouteAdapter extends BaseAdapter {
	private List<Route> mRoutes;

	public RouteAdapter() {
		mRoutes = new ArrayList<>();
	}

	public void setRoutes(List<Route> routes) {
		mRoutes = routes;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mRoutes.size();
	}

	@Override
	public Object getItem(int position) {
		return mRoutes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = getOrCreateHolder(parent.getContext(), convertView);
		holder.routeName.setText(mRoutes.get(position).getDescription(parent.getContext()));
		return holder.convertView;
	}
	
	private Holder getOrCreateHolder(Context context, View convertView) {
		Holder holder;
		
		if (convertView == null) {
			holder = new Holder();
			holder.convertView = LayoutInflater.from(context).inflate(R.layout.item_route, null);
			holder.routeName = (TextView) holder.convertView.findViewById(R.id.route_name);
			holder.convertView.setTag(holder);
		}
		else
			holder = (Holder) convertView.getTag();
		
		return holder;
	}
	
	private static class Holder {
		public View convertView;
		public TextView routeName;
	}
}