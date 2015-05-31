package com.fbafelipe.busroute;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.io.DataInputStream;
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
