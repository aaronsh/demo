package dibang.com.handle;


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

	ServerManager(Context cntx){
		mCntx = cntx;
	}
	private WebUpdateService mService;
	private WebUpdateNotification mNotifier=null;

	private int mNotificationType = 0;

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mService = ((WebUpdateService.UpdaterBinder)service).getService();
			mService.setNotifier(mNotificationType, ServerManager.this);
		}

		public void onServiceDisconnected(ComponentName className) {
			mService = null;
		}
	};	
	
	private Handler mHandler = new Handler() {
		@Override public void handleMessage(Message msg) {

			switch (msg.what) {
			default:
				if( mNotifier != null ){
					mNotifier.onWebUpdateFinish();
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

	public void bindService() {
		Log.i(TAG, "[SERVICE] Bind");
		mCntx.bindService(new Intent(mCntx, 
				WebUpdateService.class), mConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
	}

	public void unbindService() {
		Log.i(TAG, "[SERVICE] Unbind");
		mCntx.unbindService(mConnection);
	}

	public void stopService() {
		Log.i(TAG, "[SERVICE] Stop");

		Log.i(TAG, "[SERVICE] stopService");
		mCntx.stopService(new Intent(mCntx,
				WebUpdateService.class));
	}

	public void updateWeb(int update)
	{
		if( mService != null ){
			mService.updateWeb(update);
		}
	}
	
	public void updateAll()
	{
		if( mService != null ){
			mService.updateAll();
		}
	}

	public void onWebUpdateFinish() {
		// TODO Auto-generated method stub
		if(mHandler!=null){
			mHandler.sendMessage(mHandler.obtainMessage(0, 0, 0));
		}
	}

}
