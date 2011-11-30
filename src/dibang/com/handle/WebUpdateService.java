package dibang.com.handle;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import dibang.com.MainActivity;
import dibang.com.OnWebTaskFinish;
import dibang.com.R;
import dibang.com.web.ImageDownloader;
import dibang.com.web.PartnerDecoder;
import dibang.com.web.TopGalleryDecoder;
import dibang.com.web.UrlParamList;
import dibang.com.web.UserProfileDecoder;
import dibang.com.web.WebBaseDecoder;
import dibang.com.web.WebDecodeTask;
import dibang.com.web.WebSite;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * This is an example of implementing an application service that runs locally
 * in the same process as the application. The {@link StepServiceController} and
 * {@link StepServiceBinding} classes show how to interact with the service.
 * 
 * <p>
 * Notice the use of the {@link NotificationManager} when interesting things
 * happen in the service. This is generally how background services should
 * interact with the user, rather than doing something more disruptive such as
 * calling startActivity().
 */
public class WebUpdateService extends Service implements OnWebTaskFinish {
	public static final int CALLBACK_ID_0 = 0;

	public static final int UPDATE_TASK_TOP_GALLERY = 0;
	public static final int UPDATE_TASK_PARTNER = 1;
	public static final int UPDATE_TASK_MAX = 2;

	private static final int UPDATE_MODE_ALL = 0;
	private static final int UPDATE_MODE_SINGLE = 1;

	private static final int UPDATE_STATE_IDLE = 0;
	private static final int UPDATE_STATE_UPDATING = 1;

	private int mUpdateMode;
	private int mUpdateState = UPDATE_STATE_IDLE;
	private int mCurTask;

	private static final String TAG = "WebUpdateService";
	private SharedPreferences mSettings;

	private SharedPreferences mState;
	private SharedPreferences.Editor mStateEditor;

	private HashMap<Integer, WebUpdateNotification> mNotifications = new HashMap<Integer, WebUpdateNotification>();

	private NotificationManager mNM;

	/**
	 * Receives messages from activity.
	 */
	private final IBinder mBinder = new UpdaterBinder();

	private Timer timer = new Timer();
	private TimerTask task;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Log.v(TAG, "handleMessage" + msg.what);
			switch (msg.what) {
			default:
				WebUpdateNotification notifier = mNotifications.get(msg.what);
				if (notifier != null) {
					notifier.onWebUpdateFinish(msg.what);
				}
				break;
			}
			// updateAll();
			super.handleMessage(msg);
		}

	};

	/**
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */
	public class UpdaterBinder extends Binder {
		public WebUpdateService getService() {
			return WebUpdateService.this;
		}
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "[SERVICE] onCreate");
		super.onCreate();

		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		showNotification();

		// Load settings
		mSettings = PreferenceManager.getDefaultSharedPreferences(this);
		mState = getSharedPreferences("state", 0);

		// Start voice
		reloadSettings();

		task = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = new Message();
				// message.what = UPDATE_TASK_AUTO;
				// handler.sendMessage(message);
			}
		};

		// Tell the user we started.
		Toast.makeText(this, getText(R.string.app_name), Toast.LENGTH_SHORT)
				.show();
		updateAll();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.i(TAG, "[SERVICE] onStart");
		// timer.schedule(task, 1000, 1000);

		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "[SERVICE] onDestroy");
		if (timer != null)
			timer.cancel();

		mStateEditor = mState.edit();

		mStateEditor.commit();

		mNM.cancel(R.string.app_name);

		super.onDestroy();

		// Stop detecting

		// Tell the user we stopped.
		Toast.makeText(this, getText(R.string.app_name), Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "[SERVICE] onBind");
		return mBinder;
	}

	public void reloadSettings() {
		mSettings = PreferenceManager.getDefaultSharedPreferences(this);

	}

	public void resetValues() {

	}

	/**
	 * Show a notification while this service is running.
	 */
	private void showNotification() {
		CharSequence text = getText(R.string.app_name);
		Notification notification = new Notification(
				R.drawable.ic_btn_actionmenu_attach_pressed, null,
				System.currentTimeMillis());
		notification.flags = Notification.FLAG_NO_CLEAR
				| Notification.FLAG_ONGOING_EVENT;
		Intent pedometerIntent = new Intent();
		pedometerIntent
				.setComponent(new ComponentName(this, MainActivity.class));
		pedometerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				pedometerIntent, 0);
		notification.setLatestEventInfo(this, text, getText(R.string.app_name),
				contentIntent);

		mNM.notify(R.string.app_name, notification);
	}

	private void update() {
		String url = null;
		WebBaseDecoder decoder = null;
		switch (mCurTask) {
		case UPDATE_TASK_TOP_GALLERY:
			url = WebSite.TOP_GALLERY_IMGS;
			decoder = new TopGalleryDecoder();
			break;
		case UPDATE_TASK_PARTNER:
			url = WebSite.PARTNER;
			decoder = new PartnerDecoder();
			break;
		}

		if (decoder != null) {
			decoder.init(WebBaseDecoder.DECODE_URL_GET, url, null);
			WebDecodeTask strTask = new WebDecodeTask(decoder, mCurTask, this);
			strTask.execute(null);
			mUpdateState = UPDATE_STATE_UPDATING;
		}

	}

	public boolean updateWeb(int type) {
		if (mUpdateState == UPDATE_STATE_UPDATING)
			return false;
		mUpdateMode = UPDATE_MODE_SINGLE;
		mCurTask = type;
		update();
		return true;
	}

	public boolean updateAll() {
		if (mUpdateState == UPDATE_STATE_UPDATING)
			return false;
		mUpdateMode = UPDATE_MODE_ALL;
		mCurTask = UPDATE_TASK_PARTNER;
		update();
		return true;
	}

	public void setNotifier(int type, WebUpdateNotification notifier) {
		// TODO Auto-generated method stub
		Log.v(TAG, "setNotifier " + type);
		mNotifications.put(type, notifier);
		mCurTask = type;
	}

	public void onWebTaskFinished(int id, LinkedList<Object> list,
			String errorText) {
			Log.v(TAG, "onWebTaskFinished");
		// TODO Auto-generated method stub
		Message message = new Message();
		message.what = id;
		handler.sendMessage(message);


		if (mUpdateMode == UPDATE_MODE_ALL) {
			mCurTask = mCurTask + 1;
			if (mCurTask < UPDATE_TASK_MAX) {
				update();
			} else {
				mUpdateState = UPDATE_STATE_IDLE;
			}

		} else {
			mUpdateState = UPDATE_STATE_IDLE;
		}
	}


}
