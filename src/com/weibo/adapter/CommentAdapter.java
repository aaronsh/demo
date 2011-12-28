package com.weibo.adapter;

import java.util.List;

import weibo4android.Comment;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weibo.AsyncImageLoader;

import com.weibo.util.TextUtil;
import com.weibo.util.TimeUtil;

import dibang.com.R;

public class CommentAdapter extends BaseAdapter {
	private Context mContext;
	private List<Comment> mComments;

	public CommentAdapter(Context context, List<Comment> comments) {
		mContext = context;
		mComments = comments;
	}

	@Override
	public int getCount() {
		return mComments == null ? 0 : mComments.size();
	}

	@Override
	public Object getItem(int position) {
		return mComments.get(position);
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
			item.pic.setVisibility(View.GONE);
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
			item.redirectPic.setVisibility(View.GONE);
			item.redirectNum = (TextView) convertView
					.findViewById(R.id.weibo_item_redirectNum);
			item.redirectNum.setVisibility(View.GONE);
			item.commentPic = (ImageView) convertView
					.findViewById(R.id.weibo_item_commentPic);
			item.commentPic.setVisibility(View.GONE);
			item.commentNum = (TextView) convertView
					.findViewById(R.id.weibo_item_commentNum);
			item.commentNum.setVisibility(View.GONE);
			convertView.setTag(item);
		} else {
			item = (WeiboItem) convertView.getTag();
		}
		Comment comment = mComments.get(position);
		item.name.setText(comment.getUser().getScreenName());
		AsyncImageLoader.getInstance().loadPortrait(comment.getUser().getId(),
				comment.getUser().getProfileImageURL(), item.icon);
		item.createTime.setText(TimeUtil.getTimeStr(comment
				.getCreatedAt()));
		item.content.setText(TextUtil.formatContent(comment.getText(), mContext));
		item.source.setText(mContext.getString(R.string.from)
				+ comment.getSource());
		if (comment.getreplay() != null) {
			item.sub.setVisibility(View.VISIBLE);
			item.subContent.setText(TextUtil.formatContent(comment.getreplay().getText(), mContext));
		}

		return convertView;
	}

	void Log(String msg) {
		Log.i("weibo", "CommentAdapter--" + msg);
	}

}
