package dibang.com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dibang.com.handle.BaseActivity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends BaseActivity implements OnItemClickListener {
	private ListView listviews;
	EfficientAdapter mAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		InitView();
	}

	/***
	 * 初始化布局属性
	 */
	private void InitView() {
		listviews = (ListView) findViewById(R.id.listview);
		SimpleAdapter adapter = new SimpleAdapter(this, getData(),
				R.layout.list_view, new String[] { "img", "username" },
				new int[] { R.id.img, R.id.username });

		mAdapter = new EfficientAdapter(this);
		listviews.setAdapter(mAdapter);
		
		listviews.setOnItemClickListener(this);
		
		super.onInitView();
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("img", R.drawable.icon);
		map.put("username", "网站设计");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "三维动画");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "效 果 图");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "戶 型 图");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "电 子 杂 知");
		list.add(map);
	
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "合 作 伙 伴");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "联 系 我 们");
		list.add(map);
		
		return list;
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}