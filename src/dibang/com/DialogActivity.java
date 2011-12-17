/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dibang.com;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import dibang.com.handle.BaseActivity;
import dibang.com.handle.WebUpdateNotification;
import dibang.com.handle.WebUpdateService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

/**
 * <h3>Dialog Activity</h3>
 * 
 * <p>
 * This demonstrates the how to write an activity that looks like a pop-up
 * dialog.
 * </p>
 */
public class DialogActivity extends BaseActivity implements OnClickListener, WebUpdateNotification {
	public final static String KEY_DIALOG_TYPE = "dialog_type";
	public final static String KEY_WATCH_EVENT = "event";
	public final static String KEY_CLICKED_BTN = "btn";
	
	public final static int RESULT_OK = 1;
	public final static int RESULT_FAIL = 0;

	public final static int DIALOG_TYPE_QUIT_CNFM = 0;
	public final static int DIALOG_TYPE_UPDATING = 1;
	
	public final static int ACTION_GET_USER_SELECTION = 1;
	public final static int ACTION_WAITING_FOR_UPDATING = 2;
	private static final String TAG = "DialogActivity";

	private int mDialogType = DIALOG_TYPE_QUIT_CNFM;
	private int mWatchEvent;

	/**
	 * Initialization of the Activity after it is first created. Must at least
	 * call {@link android.app.Activity#setContentView setContentView()} to
	 * describe what is to be displayed in the screen.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Be sure to call the super class.
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();

		mDialogType = intent
				.getIntExtra(KEY_DIALOG_TYPE, DIALOG_TYPE_QUIT_CNFM);
		if (mDialogType == DIALOG_TYPE_QUIT_CNFM) {
			setContentView(R.layout.quit_confirm_dlg_body);
			Button btn = (Button) findViewById(R.id.button_ok);
			btn.setOnClickListener(this);
			btn = (Button) findViewById(R.id.button_cancel);
			btn.setOnClickListener(this);
		} else {
			setContentView(R.layout.updating_dlg);
			mWatchEvent = intent.getIntExtra(KEY_WATCH_EVENT, 0xff);
			mSM.registerEvent(mWatchEvent, this);
			boolean work =  mSM.updateWeb(mWatchEvent);
			if( work == false ){
				Toast.makeText(this, "正在更新，无法追加新的更新任务", Toast.LENGTH_LONG)
				.show();
				setResult(RESULT_FAIL, null);
				finish();
			}
		}

	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		if (v.getId() == R.id.button_ok) {
			intent = new Intent();
			intent.putExtra(KEY_CLICKED_BTN, R.id.button_ok);
			setResult(RESULT_OK, intent);
			finish();
		} else if (v.getId() == R.id.button_cancel) {
			intent = new Intent();
			intent.putExtra(KEY_CLICKED_BTN, R.id.button_cancel);
			setResult(RESULT_OK, intent);
			finish();
		}
	}
	@Override
	public void onWebUpdateFinish(int UpdateType) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onWebUpdateFinish");
		Intent intent = new Intent();
		intent.putExtra(KEY_WATCH_EVENT, mWatchEvent);
		setResult(RESULT_OK, intent);
		finish();
	}
	@Override
	protected void onSyncFinished(int UpdateType) {
		// TODO Auto-generated method stub
		
	}

	public void onBackPressed(){
		//super.onBackPressed();
	}
}
