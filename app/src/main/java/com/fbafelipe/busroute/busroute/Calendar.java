package com.fbafelipe.busroute.busroute;

import java.text.ParseException;

/**
 * Created by felipe on 5/31/15.
 */
public enum Calendar {
	WEEKDAY,
	SATURDAY,
	SUNDAY;
	
	public static Calendar parseCalendar(String calendar) {
		for (Calendar value : values()) {
			if (value.toString().equalsIgnoreCase(calendar)) {
				return value;
			}
		}
		
		throw new IllegalArgumentException("Invalid calendar: " + calendar);
	}
}
