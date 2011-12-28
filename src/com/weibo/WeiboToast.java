package com.weibo;

import org.apache.commons.httpclient.methods.GetMethod;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class WeiboToast {
	private static String t="";
	private static Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Toast.makeText((Context)msg.obj, t, 1000).show();
		}
	};

	public static void show(Context context,String text){
		Toast.makeText(context, text, 1000).show();
	}
	public static void asyncShow(Context context,String text){
		Message message=handler.obtainMessage();
		message.obj=context;
		t=text;
	}

}
