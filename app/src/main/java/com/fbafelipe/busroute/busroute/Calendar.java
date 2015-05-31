package com.fbafelipe.busroute.busroute;

import android.content.Context;

import com.fbafelipe.busroute.R;

import java.text.ParseException;

/**
 * Created by felipe on 5/31/15.
 */
public enum Calendar {
	WEEKDAY(R.string.weekday),
	SATURDAY(R.string.saturday),
	SUNDAY(R.string.sunday);
	
	public static Calendar parseCalendar(String calendar) {
		for (Calendar value : values()) {
			if (value.toString().equalsIgnoreCase(calendar)) {
				return value;
			}
		}
		
		throw new IllegalArgumentException("Invalid calendar: " + calendar);
	}
	
	private int mStringId;
	
	Calendar(int stringId) {
		mStringId = stringId;
	}
	
	public String getName(Context context) {
		return context.getString(mStringId);
	}
}
