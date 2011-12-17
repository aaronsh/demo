package dibang.com.handle;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import dibang.com.MainActivity;
import dibang.com.OnWebTaskFinish;
import dibang.com.R;
import dibang.com.web.AniDesignDecoder;
import dibang.com.web.EbookDecoder;
import dibang.com.web.ImageDownloader;
import dibang.com.web.PartnerDecoder;
import dibang.com.web.RenderingDecoder;
import dibang.com.web.TopGalleryDecoder;
import dibang.com.web.UpdateMode;
import dibang.com.web.UrlParamList;
import dibang.com.web.UserProfileDecoder;
import dibang.com.web.WebBaseDecoder;
import dibang.com.web.WebDecodeTask;
import dibang.com.web.WebDesignDecoder;
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
import android.content.SharedPreferences.Editor;
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
	public static final int UPDATE_TASK_EFFECT = 6;
	public static final int UPDATE_TASK_HOUSE = 3;
	public static final int UPDATE_TASK_WEB_DESIGN = 4;
	public static final int UPDATE_TASK_ANI_DESIGN = 5;
	public static final int UPDATE_TASK_EBOOK_DESIGN = 2;
	public static final int UPDATE_TASK_MAX = 7;

	private static final int UPDATE_MODE_ALL = 0;
	private static final int UPDATE_MODE_SINGLE = 1;

	private static final int UPDATE_STATE_IDLE = 0;
	private static final int UPDATE_STATE_UPDATING = 1;

	private int mUpdateMode;
	private int mUpdateState = UPDATE_STATE_IDLE;
	private int mCurTask;

	private static final String TAG = "WebUpdateService";

	private static final String PREFERENCE_KEY_TIME_STAMP = "last_udpate_time";

	private static final long TWO_DAYS = 60*1000;//2*24*60*60*1000;

	private static final int MSG_TIMER = 0xFFFF;

	private SharedPreferences mSettings;

	private SharedPreferences mState;
	private SharedPreferences.Editor mStateEditor;

	private HashMap<Integer, WebUpdateNotification> mNotifications = new HashMap<Integer, WebUpdateNotification>();

	private NotificationManager mNM;
	private long mTimeStamp;

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
			case MSG_TIMER:
				schedule();
				break;
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

		// Load settings
		mSettings = PreferenceManager.getDefaultSharedPreferences(this);
		mState = getSharedPreferences("state", 0);

		// Start voice
		reloadSettings();

		// Tell the user we started.
		Toast.makeText(this, getText(R.string.app_name), Toast.LENGTH_SHORT)
				.show();
		
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.i(TAG, "[SERVICE] onStart");
		// timer.schedule(task, 1000, 1000);
		schedule();
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
		mTimeStamp = mSettings.getLong(PREFERENCE_KEY_TIME_STAMP, 0);

	}
	private void saveSettings(){
		Editor editor = mSettings.edit();
		editor.putLong(PREFERENCE_KEY_TIME_STAMP, mTimeStamp);
		editor.commit();
	}
	
	private void schedule(){
		long diff = System.currentTimeMillis() - mTimeStamp;
		if( diff >= TWO_DAYS ){
			//Run update task
			if( updateAll() == false){
				handler.sendEmptyMessageDelayed(MSG_TIMER, TWO_DAYS);
			}
		}
		else{
			handler.sendEmptyMessageDelayed(MSG_TIMER, (TWO_DAYS-diff));
		}
	}

	public void resetValues() {

	}

	/**
	 * Show a notification while this service is running.
	 */
	private void showNotification() {
		CharSequence text = getText(R.string.app_name);
		Notification notification = new Notification(
				R.drawable.icon, null,
				System.currentTimeMillis());
		notification.flags = Notification.FLAG_NO_CLEAR
				| Notification.FLAG_ONGOING_EVENT;
		Intent pedometerIntent = new Intent();
		pedometerIntent
				.setComponent(new ComponentName(this, MainActivity.class));
		pedometerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				pedometerIntent, 0);
		notification.setLatestEventInfo(this, text, getText(R.string.updating_from_web),
				contentIntent);

		mNM.notify(R.string.app_name, notification);
	
	}
	
	private void clearNotification(){
		mNM.cancel(R.string.app_name);
	}

	private void update() {
		String url = null;
		WebBaseDecoder decoder = null;
		switch (mCurTask) {
		case UPDATE_TASK_TOP_GALLERY:
			url = WebSite.TOP_GALLERY_IMGS;
			decoder = new TopGalleryDecoder(this);
			break;
		case UPDATE_TASK_PARTNER:
			url = WebSite.PARTNER;
			decoder = new PartnerDecoder(this);
			break;
		case UPDATE_TASK_EFFECT:
			url = WebSite.EFFECT;
			decoder = new RenderingDecoder(this);
			((RenderingDecoder)decoder).setDecoderType(RenderingDecoder.DECODER_TYPE_EFFECT_SHOW);
			break;
		case UPDATE_TASK_HOUSE:
			url = WebSite.HOUSE;
			decoder = new RenderingDecoder(this);
			((RenderingDecoder)decoder).setDecoderType( RenderingDecoder.DECODER_TYPE_HOUSE_SHOW);
			break;
		case UPDATE_TASK_WEB_DESIGN:
			url = "";
			decoder = new WebDesignDecoder(this);
			break;
			
		case UPDATE_TASK_ANI_DESIGN:
			url = WebSite.ANI_DESIGN;
			decoder = new AniDesignDecoder(this);
			break;
			
		case UPDATE_TASK_EBOOK_DESIGN:
			url = WebSite.EBOOK_DESIGN;
			decoder = new EbookDecoder(this);
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
		
		showNotification();
		UpdateMode.setUpdateMode(UpdateMode.FULL_UPDATE_MODE);
		mUpdateMode = UPDATE_MODE_SINGLE;
		mCurTask = type;
		update();
		return true;
	}

	public boolean updateAll() {
		if (mUpdateState == UPDATE_STATE_UPDATING)
			return false;
		
		showNotification();
		UpdateMode.setUpdateMode(UpdateMode.FAST_UPDATE_MODE);
		mUpdateMode = UPDATE_MODE_ALL;
		mCurTask = UPDATE_TASK_TOP_GALLERY;
		update();
		return true;
	}

	public void setNotifier(int type, WebUpdateNotification notifier) {
		// TODO Auto-generated method stub
		Log.v(TAG, "setNotifier " + type);
		WebUpdateNotification old = mNotifications.get(type);
		if( old != null ){
			mNotifications.remove(type);
		}
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
				clearNotification();
				mTimeStamp = System.currentTimeMillis();
				saveSettings();
				schedule();
			}

		} else {
			mUpdateState = UPDATE_STATE_IDLE;
			clearNotification();
			mTimeStamp = System.currentTimeMillis();
			saveSettings();
			schedule();
		}
	}


}
