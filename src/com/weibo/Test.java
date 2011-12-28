package com.weibo;

import java.util.ArrayList;
import java.util.HashMap;

import com.weibo.load.DataLoadContext;
import com.weibo.load.WeiboDataLoader;
import com.weibo.util.TextUtil;

import dibang.com.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * test
 * 
 * @author starry
 * 
 */
public class Test extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);

		TextView tv = (TextView) findViewById(R.id.tv);
		tv.setText("123");
		ListView lv = (ListView) findViewById(R.id.listview);
		new DataLoadContext(this, lv, DataLoadContext.STATUS_LIST).loadData();
	}
	
	void Log(String msg){
		Log.i("weibo", "Test--"+msg);
	}

}
