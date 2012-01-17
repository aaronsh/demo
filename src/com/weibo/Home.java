package com.weibo;

import java.util.HashMap;
import java.util.List;

import weibo4android.Count;
import weibo4android.Status;
import weibo4android.WeiboException;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.weibo.adapter.WeiboItemAdapter;
import com.weibo.data.DB;
import com.weibo.data.DBHandler;

import dibang.com.R;
/**
 * 首页
 * @author starry
 *
 */
public class Home extends Activity {
	private Button titleCreateWeibo;
	private LinearLayout titlePoint;
	private TextView titleName;
	private ImageView titlePoint_nor;
	private ImageView titlePoint_sel;
	private Button titleRefresh;
	private ProgressBar titleProgress;
	private LinearLayout list_footer;
	private TextView list_footer_more;
	private LinearLayout list_footer_loading;
	private ListView weiboList;
	private List<Status> statusList;
	private HashMap<Long, Count> counts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		getViews();
		setViews();
		setListener();
		new AsyncDataLoader(asyncLocalCallback).execute();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log("home destory");
	}

	/**
	 * 异步加载服务器上的微博数据的监听器
	 */
	private AsyncDataLoader.Callback asyncRemoteCallback = new AsyncDataLoader.Callback() {

		@Override
		public void onStart() {
			try {
				statusList = Sina.getInstance().getWeibo().getFriendsTimeline();
				counts=getCounts(statusList);
				saveData(statusList, counts);
			} catch (WeiboException e) {
				e.printStackTrace();
				Log(e.toString());
			}
		}

		@Override
		public void onPrepare() {
			titleRefresh.setVisibility(View.GONE);
			titleProgress.setVisibility(View.VISIBLE);
		}

		@Override
		public void onFinish() {
			WeiboItemAdapter adapter = new WeiboItemAdapter(Home.this,
					statusList, counts);
			weiboList.setAdapter(adapter);
			titleProgress.setVisibility(View.GONE);
			titleRefresh.setVisibility(View.VISIBLE);
		}
	};
	/**
	 * 异步加载本地的微博数据的监听器
	 */
	private AsyncDataLoader.Callback asyncLocalCallback = new AsyncDataLoader.Callback() {

		@Override
		public void onStart() {
			statusList = new DBHandler(Home.this).getStatus();
			counts = new DBHandler(Home.this).getCounts();
		}

		@Override
		public void onPrepare() {
			titleRefresh.setVisibility(View.GONE);
			titleProgress.setVisibility(View.VISIBLE);
		}

		@Override
		public void onFinish() {
			WeiboItemAdapter adapter = new WeiboItemAdapter(Home.this,
					statusList, counts);
			weiboList.setAdapter(adapter);
			titleProgress.setVisibility(View.GONE);
			titleRefresh.setVisibility(View.VISIBLE);
		}
	};
	/**
	 * 保存微博数据
	 * @param status
	 * @param counts
	 */
	private void saveData(List<Status> status, HashMap<Long, Count> counts) {
		DBHandler dbHandler = new DBHandler(Home.this);
		dbHandler.clearTable(DB.HOME_TABLE);
		if(status.size()>20){ //只保存前20项
			status=status.subList(0, 19);
		}
		for (int i = 0; i < status.size(); i++) {
			dbHandler.save(status.get(i), counts.get(status.get(i)
					.getId()));
		}
	}

	private void getViews() {
		LinearLayout titlebar = (LinearLayout) findViewById(R.id.home_titlebar);
		titlePoint = (LinearLayout) titlebar.findViewById(R.id.titlebar_point);
		titleCreateWeibo = (Button) titlebar
				.findViewById(R.id.titlebar_createWeibo);
		titleName = (TextView) titlebar.findViewById(R.id.titlebar_name);
		titlePoint_nor = (ImageView) titlebar
				.findViewById(R.id.titlebar_point_nor);
		titlePoint_sel = (ImageView) titlebar
				.findViewById(R.id.titlebar_point_sel);
		titleRefresh = (Button) titlebar.findViewById(R.id.titlebar_refresh);
		titleProgress=(ProgressBar)titlebar.findViewById(R.id.titlebar_progress);
		weiboList = (ListView) findViewById(R.id.home_weiboList);
		list_footer=(LinearLayout) getLayoutInflater().inflate(R.layout.list_footer, null);
		list_footer_more=(TextView)list_footer.findViewById(R.id.footer_more);
		list_footer_loading=(LinearLayout)list_footer.findViewById(R.id.footer_loading);
	}

	private void setViews() {
		titleCreateWeibo.setVisibility(View.VISIBLE);
		titleName.setVisibility(View.VISIBLE);
		titlePoint.setClickable(true);
		titlePoint_nor.setVisibility(View.VISIBLE);
		titleRefresh.setVisibility(View.VISIBLE);
		titleName.setText("");
		
		weiboList.addFooterView(list_footer);
	}

	private void setListener() {
		titleCreateWeibo.setOnClickListener(clickListener);
		titlePoint.setOnClickListener(clickListener);
		titleRefresh.setOnClickListener(clickListener);
		weiboList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2==(weiboList.getCount()-1)){ //列表最后一项：显示更多微博
					//未做异步处理------------------------------------------------------------异步处理
					//showMoreWeiboMax(statusList.get(statusList.size()-1).getId()+"");
					return;
				}
				Sina.getInstance().goToDetail(Home.this, statusList.get(arg2));
			}
		});
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.titlebar_createWeibo:
				createWeibo();
				break;
			case R.id.titlebar_point:
				changePoint();
				break;
			case R.id.titlebar_refresh:
				refresh();
				break;
			default:
				break;
			}
		}
	};
	/**
	 * 发表新微博
	 */
	private void createWeibo() {
		Sina.getInstance().updateWeibo(this);
	}

	private void changePoint() {
		if (titlePoint_nor.isShown()) {
			titlePoint_nor.setVisibility(View.GONE);
			titlePoint_sel.setVisibility(View.VISIBLE);
		} else {
			titlePoint_nor.setVisibility(View.VISIBLE);
			titlePoint_sel.setVisibility(View.GONE);
		}
	}
	/**
	 * 刷新
	 */
	private void refresh() {
		new AsyncDataLoader(asyncRemoteCallback).execute();
	}
	/**
	 * 获取一个HashMap，以微博ID为key，对应微博的Count为值
	 * @param sList
	 * @return
	 */
	private HashMap<Long,Count> getCounts(List<Status> sList){
		HashMap<Long, Count> counts = new HashMap<Long, Count>();
		StringBuffer buffer=new StringBuffer();
		for (int i = 0; i < sList.size(); i++) {
			buffer.append(sList.get(i).getId() + ",");
		}
		try {
			List<Count> count = Sina.getInstance().getWeibo()
					.getCounts(buffer.substring(0, buffer.length() - 1));
			for (int i = 0; i < count.size(); i++) {
				counts.put(count.get(i).getId(), count.get(i));
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return counts;
	}
	
	private HashMap<Long,Count> appendCounts(List<Status> sList,HashMap<Long,Count> counts){
		StringBuffer buffer=new StringBuffer();
		for (int i = 0; i < sList.size(); i++) {
			buffer.append(sList.get(i).getId() + ",");
		}
		try {
			List<Count> count = Sina.getInstance().getWeibo()
					.getCounts(buffer.substring(0, buffer.length() - 1));
			for (int i = 0; i < count.size(); i++) {
				counts.put(count.get(i).getId(), count.get(i));
			}
		} catch (WeiboException e) {
			e.printStackTrace();
			Log(e.toString());
		}
		return counts;
	}
	/**
	 * 获取比指定微博id发表时间早的微博（包括此id代表的微博）
	 * @param id
	 */
	private void showMoreWeiboMax(String id){
		list_footer_more.setVisibility(View.GONE);
		list_footer_loading.setVisibility(View.VISIBLE);
		int foot=weiboList.getCount();
		try {
			List<Status> moreWeibo=Sina.getInstance().getWeibo().getFriendsTimelineMax(id);
			counts=appendCounts(moreWeibo, counts);
			statusList.addAll(moreWeibo);
		} catch (WeiboException e) {
			e.printStackTrace();
			Log(e.toString());
		}
		list_footer_loading.setVisibility(View.GONE);
		list_footer_more.setVisibility(View.VISIBLE);
		WeiboItemAdapter adapter=new WeiboItemAdapter(this, statusList, counts);
		weiboList.setAdapter(adapter);
		weiboList.setSelection(foot);
	}

	void Log(String msg) {
		Log.i("weibo", "home--" + msg);
	}

}
