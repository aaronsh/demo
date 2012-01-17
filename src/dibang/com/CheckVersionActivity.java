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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import dibang.com.web.IOFile;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * <h3>Dialog Activity</h3>
 * 
 * <p>
 * This demonstrates the how to write an activity that looks like a pop-up
 * dialog.
 * </p>
 */
public class CheckVersionActivity extends Activity {
	public final static String KEY_DIALOG_TYPE = "dialog_type";
	public final static String KEY_WATCH_EVENT = "event";
	public final static String KEY_CLICKED_BTN = "btn";

	public final static int RESULT_OK = 1;
	public final static int RESULT_FAIL = 0;

	public final static int DIALOG_TYPE_QUIT_CNFM = 0;
	public final static int DIALOG_TYPE_UPDATING = 1;
	public final static int DIALOG_TYPE_LEGAL = 2;

	public final static int ACTION_GET_USER_SELECTION = 1;
	public final static int ACTION_WAITING_FOR_UPDATING = 2;
	private static final String TAG = "DialogActivity";

	/**
	 * Initialization of the Activity after it is first created. Must at least
	 * call {@link android.app.Activity#setContentView setContentView()} to
	 * describe what is to be displayed in the screen.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Be sure to call the super class.
		super.onCreate(savedInstanceState);

		setContentView(R.layout.check_version_dlg);
		TextView txt = (TextView) findViewById(R.id.dialog_message);
		txt.setText("此软件已过期，请确认");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private static boolean getSdcardFlag() {
		String images = IOFile.getModuleFolder("images.txt");
		File f = new File(images);
		if (f.exists()) {
			return true;
		}
		return false;
	}

	private static void setSdcardFlag() {
		String images = IOFile.getModuleFolder("images.txt");
		// save to sdcard
		File f = new File(images);
		try {
			if (!f.exists()) {
				f.createNewFile();
			}
			OutputStreamWriter osw = new OutputStreamWriter(
					new FileOutputStream(f));
			osw.write("top_gallery\r\npartnereffect_show\r\nhouse_show\r\nweb_design\r\nani_design\r\nebook_design");
			osw.flush();
			osw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void checkVersion(MainActivity mainActivity) {
		// TODO Auto-generated method stub

		SharedPreferences pref = mainActivity
				.getPreferences(Context.MODE_PRIVATE);
		boolean invalid = pref.getBoolean("DemoVersion", false);
		long firstRun = pref.getLong("firstRun", 0);
		if (firstRun == 0) {
			firstRun = System.currentTimeMillis();
			SharedPreferences.Editor edit = pref.edit();
			edit.putLong("firstRun", firstRun);
			edit.commit();
		}
		if (!invalid) {
			long current = System.currentTimeMillis();
			final long ONE_WEEK = 10 * 24 * 60 * 60 * 1000;
			final long ONE_MINUTE = 60 * 1000;
			if ((current - firstRun) > ONE_WEEK) {
				Log.v(TAG, "set invalid flag");
				invalid = true;
				SharedPreferences.Editor edit = pref.edit();
				edit.putBoolean("DemoVersion", true);
				edit.commit();
				setSdcardFlag();
			}
		}

		if (!invalid) {
			// check from sdcard
			invalid = getSdcardFlag();
			if (invalid) {
				SharedPreferences.Editor edit = pref.edit();
				edit.putBoolean("DemoVersion", true);
				edit.commit();
			}
		}
		if (invalid) {
			Intent intent = new Intent(mainActivity, CheckVersionActivity.class);
			mainActivity.startActivity(intent);
			mainActivity.finish();
		}
	}

}
