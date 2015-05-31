package com.fbafelipe.busroute.test;

import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

import com.fbafelipe.busroute.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

/**
 * Created by felipe on 5/31/15.
 */
public class TestUtils {
	// getTestContext is hidden API, but we need it to get assets from the test project
	public static Context getTestContext(AndroidTestCase testCase) {
		try {
			Method method = AndroidTestCase.class.getMethod("getTestContext");
			return (Context) method.invoke(testCase);
		}
		catch (Exception error) {
			throw new RuntimeException("Error getting test context", error);
		}
	}
	
	public static String getAssetContent(AndroidTestCase testCase, String asset) {
		return getAssetContent(getTestContext(testCase), asset);
	}
	
	public static String getAssetContent(Context context, String asset) {
		BufferedReader input = null;
		
		try {
			input = new BufferedReader(new InputStreamReader(context.getAssets().open(asset)));
			StringBuilder buf = new StringBuilder();
			String line;
			while ((line = input.readLine()) != null) {
				buf.append(line);
				buf.append('\n');
			}
			
			return buf.toString();
		}
		catch (IOException error) {
			throw new RuntimeException("Error reading test asset", error);
		}
		finally {
			Utils.safeClose(input);
		}
	}
}
