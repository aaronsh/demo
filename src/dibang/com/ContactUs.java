package dibang.com;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dibang.com.handle.BaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ContactUs extends BaseActivity {
	private ListView listviews;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_us);
		onInitView();
//		InitView();
	}

	/***
	 * 初始化布局属性
	 */
	private void InitView() {
		listviews = (ListView) findViewById(R.id.listview);
		SimpleAdapter adapter = new SimpleAdapter(this, getData(),
				R.layout.list_contact_view, new String[] { "username" },
				new int[] {R.id.username });

		listviews.setAdapter(adapter);
		
		listviews.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				System.out.println("arg2 : " + arg2);
				android.util.Log.v("arg2","arg2 : "+ arg2);
				switch(arg2) {
				case 0 : 
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:025-83171365"));
					ContactUs.this.startActivity(intent);
				    break;
				case 2 : 
					Intent intent2 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:025-83171365"));
					ContactUs.this.startActivity(intent2);
				    break;
				case 4 :
					android.util.Log.v("arg2","test");
			        try {
						// 创建URL对象
						URL url = new URL("http://www.depcn.com");
						// 创建URL连接
						URLConnection connection = url.openConnection();
						// 对于 HTTP 连接可以直接转换成 HttpURLConnection，
						// 这样就可以使用一些 HTTP 连接特定的方法，如 setRequestMethod() 等
						// HttpURLConnection connection
						// =(HttpURLConnection)url.openConnection(Proxy_yours);
						// 设置参数
						connection.setConnectTimeout(10000);
						connection.addRequestProperty("User-Agent", "J2me/MIDP2.0");
						// 连接服务器
						connection.connect();

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		});
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("username", "电话 : 025-83171365");
		list.add(map);

		map = new HashMap<String, Object>();
	
		map.put("username", "传真 : 025-83171375");
		list.add(map);

		map = new HashMap<String, Object>();

		map.put("username", "24 h : 13813385566");
		list.add(map);
		
		map = new HashMap<String, Object>();

		map.put("username", "邮箱 : njgj001@162.com");
		list.add(map);
	
		map = new HashMap<String, Object>();

		map.put("username", "网址 : http://www.depcn.com");
		list.add(map);
		
		map = new HashMap<String, Object>();

		map.put("username", "邮编 : 210009");
		list.add(map);
		
		
		map = new HashMap<String, Object>();

		map.put("username", "地址 : 南京市....");
		list.add(map);
		return list;
	}
}