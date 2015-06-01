package com.fbafelipe.busroute;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
	
	public static ProgressDialog createLoadingDialog(Activity activity, final Runnable cancelAction) {
		final ProgressDialog progressDialog = new ProgressDialog(activity);
		progressDialog.setMessage(activity.getString(R.string.loading));
		progressDialog.setCancelable(true);
		progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				progressDialog.cancel();
			}
		});
		progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if (cancelAction != null)
					cancelAction.run();
			}
		});
		
		return progressDialog;
	}

	public static void showAlert(Activity activity, int titleId, int messageId, int buttonId) {
		showAlert(activity, titleId, messageId, buttonId, null);
	}
	
	public static void showAlert(Activity activity, int titleId, int messageId, int buttonId, final Runnable dismissAction) {
		DialogInterface.OnClickListener clickAction = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
				if (dismissAction != null)
					dismissAction.run();
			}
		};

		DialogInterface.OnCancelListener cancelAction = new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if (dismissAction != null)
					dismissAction.run();
			}
		};
		
		new AlertDialog.Builder(activity)
				.setTitle(activity.getString(titleId))
				.setMessage(activity.getString(messageId))
				.setPositiveButton(buttonId, clickAction)
				.setOnCancelListener(cancelAction)
				.show();
	}
}
