package com.weibo.adapter;

import java.util.HashMap;
import java.util.List;

import weibo4android.Count;
import weibo4android.Status;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weibo.AsyncImageLoader;
import com.weibo.Sina;
import com.weibo.util.TextUtil;
import com.weibo.util.TimeUtil;

import dibang.com.R;

public class WeiboItemAdapter extends BaseAdapter {
	private Context mContext;
	private List<Status> mStatusList;
	private HashMap<Long, Count> counts;
	private AsyncImageLoader asyncImageLoader = AsyncImageLoader.getInstance();

	public WeiboItemAdapter(Context context, List<Status> status,
			HashMap<Long, Count> counts) {
		mContext = context;
		mStatusList = status;
		this.counts = counts;
	}

	@Override
	public int getCount() {
		return mStatusList == null ? 0 : mStatusList.size();
	}

	@Override
	public Object getItem(int position) {
		return mStatusList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		WeiboItem item = null;
		if (convertView == null) {
			item = new WeiboItem();
			convertView = View.inflate(mContext, R.layout.weibo_item, null);
			item.icon = (ImageView) convertView
					.findViewById(R.id.weibo_item_icon);
			item.v = (ImageView) convertView.findViewById(R.id.weibo_item_v);
			item.name = (TextView) convertView
					.findViewById(R.id.weibo_item_name);
			item.pic = (ImageView) convertView
					.findViewById(R.id.weibo_item_pic);
			item.createTime = (TextView) convertView
					.findViewById(R.id.weibo_item_createTime);
			item.content = (TextView) convertView
					.findViewById(R.id.weibo_item_content);
			item.content_pic = (ImageView) convertView
					.findViewById(R.id.weibo_item_content_pic);
			item.sub = (LinearLayout) convertView
					.findViewById(R.id.weibo_item_sub);
			item.subContent = (TextView) convertView
					.findViewById(R.id.weibo_item_subContent);
			item.subPic = (ImageView) convertView
					.findViewById(R.id.weibo_item_subPic);
			item.source = (TextView) convertView
					.findViewById(R.id.weibo_item_source);
			item.redirectPic = (ImageView) convertView
					.findViewById(R.id.weibo_item_redirectPic);
			item.redirectNum = (TextView) convertView
					.findViewById(R.id.weibo_item_redirectNum);
			item.commentPic = (ImageView) convertView
					.findViewById(R.id.weibo_item_commentPic);
			item.commentNum = (TextView) convertView
					.findViewById(R.id.weibo_item_commentNum);

			convertView.setTag(item);
		}
		item = (WeiboItem) convertView.getTag();
		final Status status = mStatusList.get(position);
		asyncImageLoader.loadPortrait(status.getUser().getId(), status
				.getProfileImageUrl(), item.icon);
		asyncImageLoader.loadPre(status.getId(), status.getThumbnail_pic(),
				item.content_pic);
		if (status.getUser().isVerified()) {
			item.v.setVisibility(View.VISIBLE);
		} else {
			item.v.setVisibility(View.GONE);
		}
		item.name.setText(status.getUser().getScreenName());
		item.createTime.setText(TimeUtil
				.getTimeStr(status.getCreatedAt()));
		item.content.setText(TextUtil.formatContent(status.getText(),mContext));
		if (status.getRetweeted_status() != null) {
			item.sub.setVisibility(View.VISIBLE);
			String subContent="@"
				+ status.getRetweeted_status().getUser().getScreenName()
				+ ":" + status.getRetweeted_status().getText();
			item.subContent.setText(TextUtil.formatContent(subContent, mContext));
			asyncImageLoader.loadPre(status.getRetweeted_status().getId(),
					status.getRetweeted_status().getThumbnail_pic(),
					item.subPic);
		} else {
			item.sub.setVisibility(View.GONE);
		}
		item.source.setText(mContext.getString(R.string.from)
				+ status.getSource());
		if (counts != null) {
			item.redirectNum.setText("" + counts.get(status.getId()).getRt());
			item.commentNum.setText("" + counts.get(status.getId()).getComments());
		}
		item.redirectPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Sina.getInstance().redirectWeibo(mContext, status.getId());
			}
		});
		item.redirectNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Sina.getInstance().redirectWeibo(mContext, status.getId());
			}
		});
		item.commentPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Sina.getInstance().commentWeibo(mContext, status.getId());
			}
		});
		item.commentNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Sina.getInstance().commentWeibo(mContext, status.getId());
			}
		});
		return convertView;
	}

	void Log(String msg) {
		android.util.Log.i("weibo", "itemAdapter---" + msg);
	}

}
