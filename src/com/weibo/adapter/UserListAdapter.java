package com.weibo.adapter;

import java.util.List;

import weibo4android.User;
import weibo4android.WeiboException;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.weibo.Sina;

import dibang.com.R;

public class UserListAdapter extends BaseAdapter {
	private Context mContext;
	private List<User> users;

	public UserListAdapter(Context context, List<User> users) {
		this.mContext = context;
		this.users = users;
	}

	@Override
	public int getCount() {
		return users == null ? 0 : users.size();
	}

	@Override
	public Object getItem(int position) {
		return users.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UserItem item = null;
		if (convertView == null) {
			item = new UserItem();
			convertView = View.inflate(mContext, R.layout.useritem, null);
			item.icon = (ImageView) convertView
					.findViewById(R.id.useritem_icon);
			item.v = (ImageView) convertView.findViewById(R.id.useritem_v);
			item.name = (TextView) convertView.findViewById(R.id.useritem_name);
			item.weibo = (TextView) convertView
					.findViewById(R.id.useritem_weibo);
			item.attention = (Button) convertView
					.findViewById(R.id.useritem_attention);
			item.unattention = (Button) convertView
					.findViewById(R.id.useritem_unattention);
			
			convertView.setTag(item);
		} else {
			item = (UserItem) convertView.getTag();
		}
		final User user = users.get(position);

		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Sina.getInstance().goToUserInfo(mContext, user.getId());
			}
		});
		if (user.isVerified()) {
			item.v.setVisibility(View.VISIBLE);
		} else {
			item.v.setVisibility(View.GONE);
		}
		
		item.attention.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					Sina.getInstance().getWeibo().createFriendshipByUserid(""+user.getId());
				} catch (WeiboException e) {
					e.printStackTrace();
					log(e.toString());
				}
			}
		});
		item.unattention.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					Sina.getInstance().getWeibo().destroyFriendship(String.valueOf(user.getId()));
				} catch (WeiboException e) {
					e.printStackTrace();
					log(e.toString());
				}
			}
		});
		item.name.setText(user.getScreenName());
		
		return convertView;
	}

	class UserItem {
		ImageView icon;
		ImageView v;
		TextView name;
		TextView weibo;
		Button attention;
		Button unattention;
	}
	
	void log(String msg){
		Log.i("weibo", "UserListAdapter--"+msg);
	}

}
