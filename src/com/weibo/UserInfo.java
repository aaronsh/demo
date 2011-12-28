package com.weibo;

import dibang.com.R;
import weibo4android.User;
import weibo4android.WeiboException;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * 显示用户信息
 * @author starry
 *
 */
public class UserInfo extends Activity {
	private Button titleBack;
	private TextView titleName;
	private Button titleHome;
	private ProgressBar titleProgressBar;
	private ImageView icon;
	private TextView name;
	private ImageView v;
	private ImageView gender;
	private Button attention_bt;
	private Button unattention_bt;
	private TextView sinaV;
	private TextView address;
	private TextView intro;
	private LinearLayout attentionLayout;
	private TextView attention;
	private LinearLayout weiboLayout;
	private TextView weibo;
	private LinearLayout fansLayout;
	private TextView fans;
	private LinearLayout topicLayout;
	private TextView topic;
	private Button refresh;
	private Button atElse;
	private Button sendMsg;
	private Button addToBlacklistl;

	private User user;

	public static final String USER_ID = "user_id";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo);

		getViews();
		setListener();

		new AsyncDataLoader(asyncCallback).execute();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	private AsyncDataLoader.Callback asyncCallback=new AsyncDataLoader.Callback(){
		
		@Override
		public void onStart() {
			Bundle bundle = getIntent().getExtras();
			if (bundle!=null) {
				long userId=bundle.getLong(USER_ID,0);
				try {
					user=Sina.getInstance().getWeibo().showUser(String.valueOf(userId));
				} catch (WeiboException e) {
					e.printStackTrace();
					Log(e.toString());
				}
			}
		}
		
		@Override
		public void onPrepare() {
			
		}
		
		@Override
		public void onFinish() {
			if(user!=null){
				setViews();
			}
			
		}
	};

	private void getViews() {
		LinearLayout titlebar=(LinearLayout)findViewById(R.id.userinfo_titlebar);
		titleBack = (Button) titlebar.findViewById(R.id.titlebar_back);
		titleName=(TextView)titlebar.findViewById(R.id.titlebar_name);
		titleHome = (Button) titlebar.findViewById(R.id.titlebar_home);
		titleProgressBar = (ProgressBar) titlebar.findViewById(R.id.titlebar_progress);
		icon = (ImageView) findViewById(R.id.userinfo_icon);
		name = (TextView) findViewById(R.id.userinfo_name);
		v = (ImageView) findViewById(R.id.userinfo_v);
		gender = (ImageView) findViewById(R.id.userinfo_gender);
		attention_bt = (Button) findViewById(R.id.userinfo_attention_bt);
		unattention_bt = (Button) findViewById(R.id.userinfo_unattention_bt);
		sinaV = (TextView) findViewById(R.id.userinfo_elseSinaV);
		address = (TextView) findViewById(R.id.userinfo_elseAddress);
		intro = (TextView) findViewById(R.id.userinfo_elseIntro);
		attentionLayout = (LinearLayout) findViewById(R.id.userinfo_attention_layout);
		attention = (TextView) findViewById(R.id.userinfo_attention);
		weiboLayout = (LinearLayout) findViewById(R.id.userinfo_weibo_layout);
		weibo = (TextView) findViewById(R.id.userinfo_weibo);
		fansLayout = (LinearLayout) findViewById(R.id.userinfo_fans_layout);
		fans = (TextView) findViewById(R.id.userinfo_fans);
		topicLayout = (LinearLayout) findViewById(R.id.userinfo_topic_layout);
		topic = (TextView) findViewById(R.id.userinfo_topic);
		refresh = (Button) findViewById(R.id.userinfo_refresh);
		atElse = (Button) findViewById(R.id.userinfo_atElse);
		sendMsg = (Button) findViewById(R.id.userinfo_senMsg);
		addToBlacklistl = (Button) findViewById(R.id.userinfo_addToBlacklist);
	}

	private void setViews() {
		titleBack.setVisibility(View.VISIBLE);
		titleName.setVisibility(View.VISIBLE);
		titleName.setText(R.string.info);
		titleHome.setVisibility(View.VISIBLE);
		attention_bt.setVisibility(View.VISIBLE);
		
		
		name.setText(user.getScreenName());
		AsyncImageLoader.getInstance().loadPortrait(user.getId(), user.getProfileImageURL(), icon);
		address.setText(user.getLocation());
		intro.setText(user.getDescription());
		attention.setText("" + user.getFriendsCount());
		weibo.setText("" + user.getStatusesCount());
		fans.setText("" + user.getFollowersCount());
		topic.setText("话题数");
	}

	private void setListener() {
		titleBack.setOnClickListener(clickListener);
		titleHome.setOnClickListener(clickListener);
		attention_bt.setOnClickListener(clickListener);
		unattention_bt.setOnClickListener(clickListener);
		attentionLayout.setOnClickListener(clickListener);
		weiboLayout.setOnClickListener(clickListener);
		fansLayout.setOnClickListener(clickListener);
		topicLayout.setOnClickListener(clickListener);
		refresh.setOnClickListener(clickListener);
		atElse.setOnClickListener(clickListener);
		sendMsg.setOnClickListener(clickListener);
		addToBlacklistl.setOnClickListener(clickListener);
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
			case R.id.userinfo_attention_bt:
				attention();
				break;
			case R.id.userinfo_unattention_bt:
				unattention();
				break;
			case R.id.userinfo_attention_layout:
				showAttention();
				break;
			case R.id.userinfo_weibo_layout:
				showWeibo();
				break;
			case R.id.userinfo_fans_layout:
				showFans();
				break;
			case R.id.userinfo_topic_layout:
				showTopic();
				break;
			case R.id.userinfo_refresh:
				refresh();
				break;
			case R.id.userinfo_atElse:
				atElse();
				break;
			case R.id.userinfo_senMsg:
				sendMsg();
				break;
			case R.id.userinfo_addToBlacklist:
				addToBlacklist();
				break;
			default:
				break;
			}
		}
	};
	/**
	 * 返回
	 */
	private void back() {
		finish();
	}
	/**
	 * 显示主页
	 */
	private void backToHome() {

	}
	/**
	 * 关注之 （未做异步处理）
	 */
	private void attention() {
		try {
			Sina.getInstance().getWeibo().createFriendshipByScreenName(user.getScreenName());
			WeiboToast.show(this, "关注成功");
			attention_bt.setVisibility(View.GONE);
			unattention_bt.setVisibility(View.VISIBLE);
		} catch (WeiboException e) {
			e.printStackTrace();
			WeiboToast.show(this, "关注失败");
			Log(e.toString());
		}
	}
	/**
	 * 解除关注 (未做异步处理)
	 */
	private void unattention() {
		try {
			Sina.getInstance().getWeibo().destroyFriendship(
					String.valueOf(user.getId()));
			WeiboToast.show(this, "取消对" + user.getScreenName() + "的关注");
			unattention_bt.setVisibility(View.GONE);
			attention_bt.setVisibility(View.VISIBLE);
		} catch (WeiboException e) {
			e.printStackTrace();
			WeiboToast.show(this, "取消对"+user.getScreenName()+"的关注失败");
		}
	}
	/**
	 * 显示该用户的关注列表 (未完成)
	 */
	private void showAttention() {
		Sina.getInstance().showAttention(this, user.getId());
	}
	/**
	 * 显示该用户的微博列表
	 */
	private void showWeibo() {
		Sina.getInstance().showWeibo(this, user.getId());
	}
	/**
	 * 显示该用户的粉丝列表
	 */
	private void showFans() {
		Sina.getInstance().showFans(this, user.getId());
	}
	/**
	 *  显示该用户的话题列表
	 */
	private void showTopic() {

	}
	/**
	 * 刷新
	 */
	private void refresh() {

	}
	/**
	 * @该用户
	 */
	private void atElse() {

	}
	/**
	 * 对该用户发私信
	 */
	private void sendMsg() {

	}
	/**
	 * 将该用户添加到黑名单
	 */
	private void addToBlacklist() {
		try {
			Sina.getInstance().getWeibo().createBlock(
					String.valueOf(user.getId()));
			WeiboToast.show(this, "已将 "+user.getScreenName()+" 加入黑名单");
		} catch (WeiboException e) {
			e.printStackTrace();
			Log(e.toString());
			WeiboToast.show(this, "操作失败，未能将 "+user.getScreenName()+" 加入黑名单");
		}
	}

	void Log(String msg) {
		Log.i("weibo", "UserInfo--" + msg);
	}
}
