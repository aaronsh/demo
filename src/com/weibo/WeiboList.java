package com.weibo;

import java.util.List;

import weibo4android.Status;
import weibo4android.WeiboException;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.weibo.adapter.WeiboItemAdapter;

import dibang.com.R;
/**
 * 微博列表
 * @author starry
 *
 */
public class WeiboList extends Activity {
	private Button titleBack;
	private TextView titleName;
	private Button titleHome;

	private long userId;
	private ListView weiboList;
	private List<Status> status;
	private int cate;
	
	public static final String USER_ID="user_id";
	
	public static final String WEIBO_CATE="weibo_cate";
	public static final int CATE_ALL=0; //显示所有微博
	public static final int CATE_FAVORITE=1; //显示收藏微博

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibolist);
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			cate=bundle.getInt(WEIBO_CATE,CATE_ALL);
			userId=bundle.getLong(USER_ID,0);
		}

		getViews();
		setViews();
		setLisneter();
		new AsyncDataLoader(asyncCallback).execute();
	}
	private AsyncDataLoader.Callback asyncCallback=new AsyncDataLoader.Callback() {
		
		@Override
		public void onStart() {
			try {
				if(cate==CATE_ALL){
					log("weibo all");
					status=Sina.getInstance().getWeibo().getUserTimeline(String.valueOf(userId));
				}else if(cate==CATE_FAVORITE){
					log("weibo fav");
					status=Sina.getInstance().getWeibo().getFavorites(0);
				}
			} catch (WeiboException e) {
				e.printStackTrace();
				log(e.toString());
			}
		}
		
		@Override
		public void onPrepare() {
			
		}
		
		@Override
		public void onFinish() {
			WeiboItemAdapter adapter=new WeiboItemAdapter(WeiboList.this, status, null);
			weiboList.setAdapter(adapter);
		}
	};

	private void getViews() {
		LinearLayout titlebar = (LinearLayout) findViewById(R.id.weibolist_titlebar);
		titleBack = (Button) titlebar.findViewById(R.id.titlebar_back);
		titleName = (TextView) titlebar.findViewById(R.id.titlebar_name);
		titleHome = (Button) titlebar.findViewById(R.id.titlebar_home);

		weiboList = (ListView) findViewById(R.id.weibolist_weiboList);
	}

	private void setViews() {
		titleBack.setVisibility(View.VISIBLE);
		titleName.setVisibility(View.VISIBLE);
		titleHome.setVisibility(View.VISIBLE);
		if(cate==CATE_ALL){
			titleName.setText(R.string.weibo);
		}else if(cate==CATE_FAVORITE){
			titleName.setText(R.string.favorite);
		}
	}

	private void setLisneter() {
		titleBack.setOnClickListener(clickListener);
		titleHome.setOnClickListener(clickListener);
		weiboList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Sina.getInstance().goToDetail(WeiboList.this, status.get(arg2));
			}
		});
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.titlebar_back:
				back();
				break;
			case R.id.titlebar_home:
				backToHome();
				break;
			default:
				break;
			}
		}
	};	

	private void back() {
		finish();
	}

	private void backToHome() {
		Sina.getInstance().backToHome(this);
	}

	void log(String msg) {
		Log.i("weibo", "WeiboList--" + msg);
	}

}
