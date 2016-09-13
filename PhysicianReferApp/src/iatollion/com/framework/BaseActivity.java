package iatollion.com.framework;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import iatollion.com.tw.R;

/**
 * @description base abstract class
 * @author Wade.Chen
 * @date 2016.08.31
 */

public abstract class BaseActivity extends Activity {
	
	private static final String TAG = BaseActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.sliding_in_left, R.anim.sliding_out_left);
	}
	
	@Override
	public void finish() {
		super.finish();
		if (!isLastOneActivity()) {
			overridePendingTransition(R.anim.sliding_in_right, R.anim.sliding_out_right);
		}
	}
	
	protected void onFinish() {
		
	}
	
	@SuppressWarnings("deprecation")
	public boolean isLastOneActivity() {
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			Log.d(TAG , "isLastOneActivity tasks.get(0).numActivities " + tasks.get(0).numActivities);
			if (tasks.get(0).numActivities == 1) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK || event.getAction() == KeyEvent.KEYCODE_BACK) {
			backAction();
			if (backPress()) {
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void onError() {
		backPress();
	}
	
	protected boolean backPress() {
		finish();
		return true;
	}
	
	protected void backAction() {
		
	}
	
	
	
}
