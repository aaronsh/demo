package com.weibo;

import dibang.com.R;
import weibo4android.User;
import weibo4android.WeiboException;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 用户个人资料
 * @author starry
 *
 */
public class SelfInfo extends Activity {
	private Button titleCreateWeibo;
	private TextView titleName;
	private Button titleRefresh;
	private ProgressBar titleProgress;

	private ImageView icon;
	private TextView name;
	private ImageView gender;
	private Button edit_bt;
	private TextView address;
	private TextView loginName;
	private LinearLayout attentionLayout;
	private TextView attention;
	private LinearLayout weiboLayout;
	private TextView weibo;
	private LinearLayout fansLayout;
	private TextView fans;
	private LinearLayout topicLayout;
	private TextView topic;
	private LinearLayout favLayout;
	private TextView favorite;
	private LinearLayout blacklistLayout;

	private User user;

	public static final String USER = "user";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selfinfo);
		
		getViews();
		setListeners();
		try {
			user=Sina.getInstance().getWeibo().verifyCredentials();
			setViews();
		} catch (WeiboException e) {
			e.printStackTrace();
			Log(e.toString());
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.titlebar_createWeibo:
				createWeibo();
				break;
			case R.id.titlebar_refresh:
				refresh();
				break;
			case R.id.selfinfo_editInfo:
				editInfo();
				break;
			case R.id.selfinfo_attentionLayout:
				showAttention();
				break;
			case R.id.selfinfo_weiboLayout:
				showWeibo();
				break;
			case R.id.selfinfo_fansLayout:
				showFans();
				break;
			case R.id.selfinfo_topicLayout:
				showTopic();
				break;
			case R.id.selfinfo_favLayout:
				showFavorite();
				break;
			case R.id.selfinfo_blacklistLayout:
				showBlacklist();
				break;
			default:
				break;
			}
		}
	};
	private AsyncDataLoader.Callback asyncCallback = new AsyncDataLoader.Callback() {
		User u = null;

		@Override
		public void onStart() {
			try {
				u = Sina.getInstance().getWeibo().showUser(
						user.getScreenName());
			} catch (WeiboException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onPrepare() {
			titleRefresh.setVisibility(View.GONE);
			titleProgress.setVisibility(View.VISIBLE);
		}

		@Override
		public void onFinish() {
			if (user != null) {
				name.setText(user.getScreenName());
				address.setText(user.getLocation());
				attention.setText(String.valueOf(user.getFriendsCount()));
				weibo.setText(String.valueOf(user.getStatusesCount()));
				fans.setText(String.valueOf(user.getFollowersCount()));
				topic.setText("话题数");
				favorite.setText(String.valueOf(user.getFavouritesCount()));
			}
			titleProgress.setVisibility(View.GONE);
			titleRefresh.setVisibility(View.VISIBLE);
		}
	};
	/**
	 * 新建微博
	 */
	private void createWeibo() {
		Sina.getInstance().updateWeibo(this);
	}
	/**
	 * 刷新
	 */
	private void refresh() {
		new AsyncDataLoader(asyncCallback).execute();
	}
	/**
	 * 编辑用户资料
	 */
	private void editInfo() {
		Sina.getInstance().goToInfoEditor(this, user);
	}
	/**
	 * 显示关注用户列表
	 */
	private void showAttention() {
		Sina.getInstance().showAttention(this, user.getId());
	}
	/**
	 * 显示微博列表
	 */
	private void showWeibo() {
		Sina.getInstance().showWeibo(this, user.getId());
	}
	/**
	 * 显示粉丝列表
	 */
	private void showFans() {
		Sina.getInstance().showFans(this, user.getId());
	}
	/**
	 * 显示话题列表
	 */
	private void showTopic() {
		Sina.getInstance().ShowTopic();
	}
	/**
	 * 显示收藏列表
	 */
	private void showFavorite() {
		Sina.getInstance().showFavorite(this);
	}
	/**
	 * 显示黑名单列表
	 */
	private void showBlacklist() {
		Sina.getInstance().showBlacklist(this, user.getId());
	}

	private void getViews() {
		LinearLayout titleBar = (LinearLayout) findViewById(R.id.selfinfo_titlebar);
		titleCreateWeibo = (Button) titleBar
				.findViewById(R.id.titlebar_createWeibo);
		titleRefresh = (Button) titleBar.findViewById(R.id.titlebar_refresh);
		titleName = (TextView) titleBar.findViewById(R.id.titlebar_name);
		titleProgress = (ProgressBar) titleBar
				.findViewById(R.id.titlebar_progress);
		icon = (ImageView) findViewById(R.id.selfinfo_userIcon);
		name = (TextView) findViewById(R.id.selfinfo_userName);
		gender = (ImageView) findViewById(R.id.selfinfo_userGender);
		edit_bt = (Button) findViewById(R.id.selfinfo_editInfo);
		address = (TextView) findViewById(R.id.selfinfo_address);
		loginName = (TextView) findViewById(R.id.selfinfo_loginName);
		attentionLayout = (LinearLayout) findViewById(R.id.selfinfo_attentionLayout);
		attention = (TextView) findViewById(R.id.selfinfo_guanzhu);
		weiboLayout = (LinearLayout) findViewById(R.id.selfinfo_weiboLayout);
		weibo = (TextView) findViewById(R.id.selfinfo_weibo);
		fansLayout = (LinearLayout) findViewById(R.id.selfinfo_fansLayout);
		fans = (TextView) findViewById(R.id.selfinfo_fans);
		topicLayout = (LinearLayout) findViewById(R.id.selfinfo_topicLayout);
		topic = (TextView) findViewById(R.id.selfinfo_topic);
		favLayout = (LinearLayout) findViewById(R.id.selfinfo_favLayout);
		favorite = (TextView) findViewById(R.id.selfinfo_favorite);
		blacklistLayout = (LinearLayout) findViewById(R.id.selfinfo_blacklistLayout);
	}

	private void setViews() {
		titleCreateWeibo.setVisibility(View.VISIBLE);
		titleName.setVisibility(View.VISIBLE);
		titleRefresh.setVisibility(View.VISIBLE);
		titleName.setText(R.string.selfInfo);
		name.setText(user.getScreenName());
		AsyncImageLoader.getInstance().loadPortrait(user.getId(), user.getProfileImageURL(), icon);
		address.setText(user.getLocation());
		attention.setText(String.valueOf(user.getFriendsCount()));
		weibo.setText(String.valueOf(user.getStatusesCount()));
		fans.setText(String.valueOf(user.getFollowersCount()));
		topic.setText("话题数");
		favorite.setText(String.valueOf(user.getFavouritesCount()));
	}

	private void setListeners() {
		titleCreateWeibo.setOnClickListener(clickListener);
		titleRefresh.setOnClickListener(clickListener);
		edit_bt.setOnClickListener(clickListener);
		attentionLayout.setOnClickListener(clickListener);
		weiboLayout.setOnClickListener(clickListener);
		fansLayout.setOnClickListener(clickListener);
		topicLayout.setOnClickListener(clickListener);
		favLayout.setOnClickListener(clickListener);
		blacklistLayout.setOnClickListener(clickListener);
	}
	void Log(String msg) {
		android.util.Log.i("weibo", msg);
	}

}
