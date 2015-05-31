package com.fbafelipe.busroute.busroute;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by felipe on 5/30/15.
 */
public class Route {
	private int mId;
	private String mShortName;
	private String mLongName;
	
	public Route(int id, String shortName, String longName) {
		mId = id;
		mShortName = shortName;
		mLongName = longName;
	}
	
	public int getId() {
		return mId;
	}
	
	public String getShortName() {
		return mShortName;
	}

	public String getLongName() {
		return mLongName;
	}
}
