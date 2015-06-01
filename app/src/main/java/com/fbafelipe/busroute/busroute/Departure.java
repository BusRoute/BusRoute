package com.fbafelipe.busroute.busroute;

/**
 * Created by felipe on 5/31/15.
 */
public class Departure {
	private Calendar mCalendar;
	private String mTime;
	
	public Departure(Calendar calendar, String time) {
		mCalendar = calendar;
		mTime = time;
	}

	public Calendar getCalendar() {
		return mCalendar;
	}

	public String getTime() {
		return mTime;
	}
}
