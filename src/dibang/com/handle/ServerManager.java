package dibang.com.handle;


import java.util.HashMap;
import java.util.Set;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class ServerManager implements WebUpdateNotification{
	private static final String TAG = "ServerManager";
	private Context mCntx;
	private static ServerManager mIns = null;

	ServerManager(Context cntx){
		mCntx = cntx;
	}

	public static ServerManager getInstance(Context cntx){
		if( mIns == null ){
			mIns = new ServerManager(cntx);
		}
		return mIns;
	}
	
	private static WebUpdateService mService;
	private static HashMap<Integer, WebUpdateNotification> mNotifiers = new HashMap<Integer, WebUpdateNotification>();


	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			Log.v(TAG, "onServiceConnected");
			mService = ((WebUpdateService.UpdaterBinder)service).getService();
			Set<Integer> keys = mNotifiers.keySet();
			for( Integer key:keys ){
				mService.setNotifier(key, ServerManager.this);				
			}
		}

		public void onServiceDisconnected(ComponentName className) {
			mService = null;
		}
	};	
	
	private Handler mHandler = new Handler() {
		@Override public void handleMessage(Message msg) {

			switch (msg.what) {
			default:
				WebUpdateNotification notifier = mNotifiers.get(msg.what);
				if( notifier != null ){
					notifier.onWebUpdateFinish(msg.what);
				}
				super.handleMessage(msg);
			}
		}

	};
	
	public void startService() {
		Log.i(TAG, "[SERVICE] Start");
		mCntx.startService(new Intent(mCntx,
				WebUpdateService.class));
	}
	
	public void registerEvent(int event, WebUpdateNotification notifier){
		WebUpdateNotification old = mNotifiers.get(event);
		if( old != null ){
			mNotifiers.remove(event);
		}
		mNotifiers.put(event, notifier);
		if( mService != null ){
			mService.setNotifier(event, ServerManager.this);				
		}
		Log.v(TAG, "event:"+event+",obj:"+mNotifiers.get(event));
	}

	public void bindService() {
		Log.i(TAG, "[SERVICE] Bind");
		
		mCntx.bindService(new Intent(mCntx, 
				WebUpdateService.class), mConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
	}

	public void unbindService() {
		Log.i(TAG, "[SERVICE] Unbind");
		if( mService != null )
			mCntx.unbindService(mConnection);
	}

	public void stopService() {
		Log.i(TAG, "[SERVICE] Stop");

		Log.i(TAG, "[SERVICE] stopService");
		mCntx.stopService(new Intent(mCntx,
				WebUpdateService.class));
	}

	public boolean updateWeb(int update)
	{
		if( mService != null ){
			return mService.updateWeb(update);
		}
		return false;
	}
	
	public void updateAll()
	{
		Log.v(TAG, "updateAll "+mService);
		if( mService != null ){
			mService.updateAll();
		}
	}

	public void onWebUpdateFinish(int updater) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onWebUpdateFinish");
		if(mHandler!=null){
			mHandler.sendMessage(mHandler.obtainMessage(updater, 0, 0));
		}
	}

}
