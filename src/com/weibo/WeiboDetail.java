package com.weibo;

import java.util.List;

import com.weibo.util.TextUtil;
import com.weibo.util.TimeUtil;

import dibang.com.R;

import weibo4android.Count;
import weibo4android.Status;
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
import android.widget.TextView;
/**
 * 微博详细信息
 * @author starry
 *
 */
public class WeiboDetail extends Activity {
	private Button titleBack;
	private TextView titleName;
	private Button titleHome;
	private LinearLayout userinfo;
	private ImageView icon;
	private ImageView v;
	private TextView name;
	private TextView content;
	private ImageView pic;
	private LinearLayout sub;
	private TextView subContent;
	private ImageView subPic;
	private LinearLayout subRedirect;
	private TextView subRedirectNum;
	private LinearLayout subComment;
	private TextView subCommentNum;
	private Button redirect_bt;
	private Button comment_bt;
	private TextView time;
	private TextView source;
	private TextView refresh;
	private TextView comment;
	private TextView redirect;
	private TextView favorite;
	private TextView more;

	private Status mStatus;
	public static final String STATUS = "status";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_detail);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mStatus = (Status) bundle.getSerializable(STATUS);
		}

		getViews();
		setListeners();
		setViews();
	}

	private void getViews() {
		LinearLayout titlebar = (LinearLayout) findViewById(R.id.detail_titlebar);
		titleBack = (Button) titlebar.findViewById(R.id.titlebar_back);
		titleName = (TextView) titlebar.findViewById(R.id.titlebar_name);
		titleHome = (Button) titlebar.findViewById(R.id.titlebar_home);
		userinfo = (LinearLayout) findViewById(R.id.detail_userinfo);
		icon = (ImageView) findViewById(R.id.detail_icon);
		v = (ImageView) findViewById(R.id.detail_v);
		name = (TextView) findViewById(R.id.detail_name);
		content = (TextView) findViewById(R.id.detail_content);
		pic = (ImageView) findViewById(R.id.detail_pic);
		sub = (LinearLayout) findViewById(R.id.detail_sub);
		subContent = (TextView) findViewById(R.id.detail_subContent);
		subPic = (ImageView) findViewById(R.id.detail_subPic);
		subRedirect = (LinearLayout) findViewById(R.id.detail_subRedirect);
		subRedirectNum = (TextView) findViewById(R.id.detail_sub_redirectNum);
		subComment = (LinearLayout) findViewById(R.id.detail_subComent);
		subCommentNum = (TextView) findViewById(R.id.detail_sub_commentNum);
		redirect_bt = (Button) findViewById(R.id.detail_redirect_bt);
		comment_bt = (Button) findViewById(R.id.detail_comment_bt);
		time = (TextView) findViewById(R.id.detail_time);
		source = (TextView) findViewById(R.id.detail_from);
		refresh = (Button) findViewById(R.id.detail_refresh);
		comment = (Button) findViewById(R.id.detail_comment);
		redirect = (Button) findViewById(R.id.detail_redirect);
		favorite = (Button) findViewById(R.id.detail_favorite);
		more = (Button) findViewById(R.id.detail_more);
	}

	private void setListeners() {
		titleBack.setOnClickListener(clickListener);
		titleHome.setOnClickListener(clickListener);
		userinfo.setOnClickListener(clickListener);
		pic.setOnClickListener(clickListener);
		subPic.setOnClickListener(clickListener);
		subRedirect.setOnClickListener(clickListener);
		subComment.setOnClickListener(clickListener);
		redirect_bt.setOnClickListener(clickListener);
		comment_bt.setOnClickListener(clickListener);
		refresh.setOnClickListener(clickListener);
		comment.setOnClickListener(clickListener);
		redirect.setOnClickListener(clickListener);
		favorite.setOnClickListener(clickListener);
		more.setOnClickListener(clickListener);
	}

	private void setViews() {
		titleBack.setVisibility(View.VISIBLE);
		titleName.setVisibility(View.VISIBLE);
		titleHome.setVisibility(View.VISIBLE);
		titleName.setText(R.string.weiboBody);

		User user = mStatus.getUser();
		AsyncImageLoader.getInstance().loadPortrait(mStatus.getUser().getId(),
				mStatus.getProfileImageUrl(), icon);
		name.setText(user.getScreenName());
		if(user.isVerified()){
			v.setVisibility(View.VISIBLE);
		}else{
			v.setVisibility(View.GONE);
		}
		content.setText(TextUtil.formatContent(mStatus.getText(), this));
		time.setText(TimeUtil.getTimeStr(mStatus.getCreatedAt()));
		source.setText(getString(R.string.from) + mStatus.getSource());
		AsyncImageLoader.getInstance().loadPre(mStatus.getId(),
				mStatus.getBmiddle_pic(), pic);
		if (mStatus.getRetweeted_status() != null) { //若存在转发、评论的微博
			AsyncImageLoader.getInstance().loadPre(
					mStatus.getRetweeted_status().getId(),
					mStatus.getRetweeted_status().getBmiddle_pic(), subPic);
			Log(mStatus.getRetweeted_status().getOriginal_pic());
		}
		//更新微博的转发数、评论数
		try {
			List<Count> countNum = Sina.getInstance().getWeibo()
					.getCounts(mStatus.getId() + "");
			redirect_bt.setText(countNum.get(0).getRt() + "");
			comment_bt.setText(countNum.get(0).getComments() + "");
		} catch (WeiboException e1) {
			e1.printStackTrace();
		}

		if (mStatus.getRetweeted_status() != null) { //若存在转发、评论的微博
			sub.setVisibility(View.VISIBLE);
			String sContent="@"+mStatus.getRetweeted_status().getUser().getScreenName()+":"+mStatus.getRetweeted_status().getText();
			subContent.setText(TextUtil.formatContent(sContent, this));
			try { //更新转发、评论微博的转发数、评论数
				List<Count> count = Sina.getInstance().getWeibo()
						.getCounts(mStatus.getRetweeted_status().getId() + "");
				subRedirectNum.setText(count.get(0).getRt() + "");
				subCommentNum.setText(count.get(0).getComments() + "");
			} catch (WeiboException e) {
				e.printStackTrace();
				Log(e.toString());
			}
		} else {
			sub.setVisibility(View.GONE);
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
			case R.id.titlebar_back:
				back();
				break;
			case R.id.titlebar_home:
				backToHome();
				break;
			case R.id.detail_userinfo:
				goToUserinfo();
				break;
			case R.id.detail_pic:
				showPic();
				break;
			case R.id.detail_subPic:
				showPic();
				break;
			case R.id.detail_subRedirect:
				redirectWeibo(mStatus.getRetweeted_status().getId());
				break;
			case R.id.detail_subComent:
				goToCommentList(mStatus.getId());
				break;
			case R.id.detail_redirect_bt:
				redirectWeibo(mStatus.getId());
				break;
			case R.id.detail_comment_bt:
				goToCommentList(mStatus.getId());
				break;
			case R.id.detail_refresh:
				refresh();
				break;
			case R.id.detail_comment:
				commentWeibo(mStatus.getId());
				break;
			case R.id.detail_redirect:
				redirectWeibo(mStatus.getId());
				break;
			case R.id.detail_favorite:
				favWeibo();
				break;
			case R.id.detail_more:
				showMore();
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
	 * 返回首页
	 */
	private void backToHome() {
		Sina.getInstance().backToHome(this);
	}
	/**
	 * 转入用户信息界面
	 */
	private void goToUserinfo() {
		Sina.getInstance().goToUserInfo(this, mStatus.getUser().getId());
	}
	/**
	 * 显示图片
	 */
	private void showPic() {

	}
	/**
	 * 刷新
	 */
	private void refresh() {

	}
	/**
	 * 转到评论列表界面
	 * @param id
	 */
	private void goToCommentList(long id){
		Sina.getInstance().goToCommentList(this, id);
	}
	/**
	 * 评论微博
	 */
	private void commentWeibo(long id) {
		Sina.getInstance().commentWeibo(this, id);
	}
	/**
	 * 转发微博
	 * @param id
	 */
	private void redirectWeibo(long id) {
		Sina.getInstance().redirectWeibo(this, id);
	}
	/**
	 * 收藏微博
	 */
	private void favWeibo() {
		try {
			Sina.getInstance().getWeibo().createFavorite(mStatus.getId());
			WeiboToast.show(this, "加入收藏");
		} catch (WeiboException e) {
			e.printStackTrace();
			WeiboToast.show(this, "收藏失败");
		}
	}
	/**
	 * 更多
	 */
	private void showMore() {

	}

	void Log(String msg) {
		Log.i("weibo", "WeiboDetail--" + msg);
	}

}
