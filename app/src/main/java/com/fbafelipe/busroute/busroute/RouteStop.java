package com.fbafelipe.busroute.busroute;

/**
 * Created by felipe on 5/30/15.
 */
public class RouteStop {
	private String mName;
	private int mSequence;
	
	public RouteStop(String name, int sequence) {
		mName = name;
		mSequence = sequence;
	}
	
	public String getName() {
		return mName;
	}
	
	public int getSequence() {
		return mSequence;
	}
}
