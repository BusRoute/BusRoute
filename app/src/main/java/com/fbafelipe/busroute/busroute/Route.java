package com.fbafelipe.busroute.busroute;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.fbafelipe.busroute.R;

/**
 * Created by felipe on 5/30/15.
 */
public class Route implements Parcelable {
	public static Parcelable.Creator<Route> CREATOR = new Parcelable.Creator<Route>() {
		@Override
		public Route createFromParcel(Parcel source) {
			Route route = new Route();
			route.readFromParcel(source);
			return route;
		}

		@Override
		public Route[] newArray(int size) {
			return new Route[size];
		}
	};
	
	private int mId;
	private String mShortName;
	private String mLongName;

	public Route() {}
	
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
	
	public String getDescription(Context context) {
		return context.getString(R.string.route_description, mShortName, mLongName);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(mId);
		parcel.writeString(mShortName);
		parcel.writeString(mLongName);
	}
	
	private void readFromParcel(Parcel parcel) {
		mId = parcel.readInt();
		mShortName = parcel.readString();
		mLongName = parcel.readString();
	}
}
