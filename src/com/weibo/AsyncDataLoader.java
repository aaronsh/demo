package com.weibo;

import android.os.AsyncTask;
import android.util.Log;

/**
 * 异步数据加载
 * 
 * @author starry
 * 
 */
public class AsyncDataLoader extends AsyncTask<Void, Long, Object> {
	private AsyncDataLoader.Callback mCallback;

	public AsyncDataLoader(AsyncDataLoader.Callback callback) {
		setCallback(callback);
	}

	@Override
	protected Object doInBackground(Void... voids) {
		if (mCallback != null) {
			mCallback.onStart();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (mCallback != null) {
			mCallback.onFinish();
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mCallback != null) {
			mCallback.onPrepare();
		}
	}

	public void setCallback(AsyncDataLoader.Callback callback) {
		this.mCallback = callback;
	}

	public interface Callback {
		public void onPrepare();

		public void onStart();

		public void onFinish();
	}

	void Log(String msg) {
		Log.i("weibo", "Asyc---" + msg);
	}

}
