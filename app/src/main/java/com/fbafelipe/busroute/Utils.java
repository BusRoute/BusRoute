package com.fbafelipe.busroute;

import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by felipe on 5/30/15.
 */
public class Utils {
	private static final String TAG = Utils.class.getSimpleName();
	
	public static void safeClose(Closeable closeable) {
		try {
			if (closeable != null)
				closeable.close();
		}
		catch (IOException error) {
			Log.e(TAG, "", error);
		}
	}
}
