package iatollion.com.Dialog;

import iatollion.com.tw.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

/**
 * @description Pop up message to notice any object
 * @author Wade.Chen
 * @version 1.0.0
 */
public class Notice {
	private static final String TAG = Notice.class.getSimpleName();
	private static Context mContext;
	
	protected Notice() {
		
	}
	
	public static void setHandler(Context ctx) {
		mContext = ctx;
	}	
	
	public static void popAuthErrorMessage() {
		Log.i(TAG, "[Error message]");
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(R.string.authorize_error);
		builder.setMessage(R.string.authorize_error_msg);
		builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO
				dialog.dismiss();
			}		
		});
		builder.create();
		builder.show();
		builder = null;
	}
	
	public static void popAuthWarningMessage() {
		Log.i(TAG, "[Warning message]");
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(R.string.authorize_warning);
		builder.setMessage(R.string.authorize_warning_msg);
		builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO
				dialog.dismiss();
			}		
		});
		builder.create();
		builder.show();
		builder = null;
	}

	public static void popAuthFormatWarningMessage() {
		Log.i(TAG, "[Warning message]");
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(R.string.authorize_warning);
		builder.setMessage(R.string.authorize_formatwarning_msg);
		builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO
				dialog.dismiss();
			}
		});
		builder.create();
		builder.show();
		builder = null;
	}
	
	public static void popAuthFailMessage() {
		Log.i(TAG, "[Fail message]");
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(R.string.authorize_fail);
		builder.setMessage(R.string.authorize_fail_msg);
		builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO
				dialog.dismiss();
			}		
		});
		builder.create();
		builder.show();
		builder = null;
	}
}
