package com.weibo;

import java.util.List;

import weibo4android.User;
import weibo4android.WeiboException;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.weibo.adapter.UserListAdapter;

import dibang.com.R;
/**
 * 显示用户关注列表/用户粉丝列表/用户黑名单列表（暂不支持）
 * @author starry
 *
 */
public class UserList extends Activity{
	private Button titleBack;
	private TextView titleName;
	private Button titleHome;
	
	private List<User> users;
	
	private ListView userList;
	
	private int cate; //显示种类（关注 粉丝 黑名单）
	
	public static final String USER_CATE="user_cate";
	public static final int CATE_ATTENTION=0; //关注
	public static final int CATE_FANS=1; //粉丝
	public static final int CATE_BLACKLIST=3; //黑名单
	
	private long userId; //传入的用户id
	public static final String USER_ID="user_id";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attentionerlist);
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			userId=bundle.getLong(USER_ID,0);
			cate=bundle.getInt(USER_CATE,0);
		}
		
		LinearLayout titleBar=(LinearLayout)findViewById(R.id.attentioner_titlebar);
		titleBack=(Button)titleBar.findViewById(R.id.titlebar_back);
		titleName=(TextView)titleBar.findViewById(R.id.titlebar_name);
		titleHome=(Button)titleBar.findViewById(R.id.titlebar_home);
		
		userList=(ListView)findViewById(R.id.attentioner_listView);
		
		titleBack.setVisibility(View.VISIBLE);
		titleName.setVisibility(View.VISIBLE);
		titleHome.setVisibility(View.VISIBLE);
		titleBack.setOnClickListener(clickListener);
		titleHome.setOnClickListener(clickListener);
		
		if(cate==CATE_ATTENTION){
			titleName.setText(R.string.attention);
		}else if(cate==CATE_FANS){
			titleName.setText(R.string.fans);
		}else if(cate==CATE_BLACKLIST){
			titleName.setText(R.string.blackList);
		}
		AsyncDataLoader asyncLoadTask=new AsyncDataLoader(asyncCallback);
		asyncLoadTask.execute();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private AsyncDataLoader.Callback asyncCallback=new AsyncDataLoader.Callback(){
		
		@Override
		public void onStart() {
			try {
				if(cate==CATE_ATTENTION){
					//error:40302 auth faild--------------------------------------------------
					log("user attention");
					users=Sina.getInstance().getWeibo().getFriendsStatuses(String.valueOf(userId));
				}else if(cate==CATE_FANS){
					users=Sina.getInstance().getWeibo().getFollowersStatuses(String.valueOf(userId));
				}else if(cate==CATE_BLACKLIST){
					users=Sina.getInstance().getWeibo().getBlockingUsers();
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
			UserListAdapter adapter=new UserListAdapter(UserList.this, users);
			userList.setAdapter(adapter);
		}
	};

	private OnClickListener clickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.titlebar_back:
				finish();
				break;
			case R.id.titlebar_home:
				backToHome();
				break;
			}
		}
	};
	/**
	 * 返回首页
	 */
	private void backToHome() {
		
	}
	
	void log(String msg){
		Log.i("weibo", "UserList--"+msg);
	}
}
