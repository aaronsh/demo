package com.weibo;

import java.util.List;

import weibo4android.Comment;
import weibo4android.Status;
import weibo4android.WeiboException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.weibo.adapter.CommentAdapter;
import com.weibo.adapter.WeiboItemAdapter;

import dibang.com.R;
/**
 * 信息
 * @author starry
 *
 */
public class Msg extends Activity {
	private RadioButton atMe;
	private RadioButton comment;
	private RadioButton letter;
	private ListView atList;
	private ListView commentList;
	private WeiboItemAdapter atAdapter;
	private CommentAdapter commentAdapter;
	private List<Status> atStatus;
	private List<Comment> comments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msg);

		getViews();
		setViews();
		setListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private AsyncDataLoader.Callback asyncCallback = new AsyncDataLoader.Callback() {

		@Override
		public void onStart() {
			try {
				if (atMe.isChecked()) {
					atStatus = Sina.getInstance().getWeibo().getMentions();
				} else if (comment.isChecked()) {
					comments = Sina.getInstance().getWeibo()
							.getCommentsToMe();
				}
			} catch (WeiboException e) {
				e.printStackTrace();
				Log(e.toString());
			}
		}

		@Override
		public void onPrepare() {

		}

		@Override
		public void onFinish() {
			if (atMe.isChecked()) {
				atAdapter = new WeiboItemAdapter(Msg.this, atStatus, null);
				atList.setAdapter(atAdapter);
			} else if (comment.isChecked()) {
				commentAdapter = new CommentAdapter(Msg.this, comments);
				commentList.setAdapter(commentAdapter);
			}
		}
	};

	private void getViews() {
		atMe = (RadioButton) findViewById(R.id.msg_atme);
		comment = (RadioButton) findViewById(R.id.msg_comment);
		letter = (RadioButton) findViewById(R.id.msg_letter);
		atList = (ListView) findViewById(R.id.msg_atList);
		commentList = (ListView) findViewById(R.id.msg_commentList);
	}

	private void setViews() {
		showAtMe();
	}

	private void setListener() {
		atMe.setOnCheckedChangeListener(checkedChangeListener);
		comment.setOnCheckedChangeListener(checkedChangeListener);
		letter.setOnCheckedChangeListener(checkedChangeListener);
		atList.setOnItemClickListener(itemClickListener);
		atList.setOnItemLongClickListener(itemLongClickListener);
		commentList.setOnItemClickListener(itemClickListener);
	}

	private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				switch (buttonView.getId()) {
				case R.id.msg_atme:
					showAtMe();
					break;
				case R.id.msg_comment:
					showComment();
					break;
				case R.id.msg_letter:
					showLetter();
					break;
				default:
					break;
				}
			}
		}
	};
	/**
	 * 显示@me 的微博
	 */
	private void showAtMe() {
		atMe.setChecked(true);
		comment.setChecked(false);
		commentList.setVisibility(View.GONE);
		atList.setVisibility(View.VISIBLE);
		if (atList.getAdapter() == null) {
			new AsyncDataLoader(asyncCallback).execute();
		}
	}
	/**
	 * 显示评论me的微博
	 */
	private void showComment() {
		atMe.setChecked(false);
		comment.setChecked(true);
		atList.setVisibility(View.GONE);
		commentList.setVisibility(View.VISIBLE);
		if (commentList.getAdapter() == null) {
			new AsyncDataLoader(asyncCallback).execute();
		}
	}
	/**
	 * 显示私信
	 */
	private void showLetter() {
		letter.setChecked(true);
	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (arg0.getId() == R.id.msg_atList) {
				if (atStatus != null) {
					Sina.getInstance().goToDetail(Msg.this, atStatus.get(arg2));
				}
			} else if (arg0.getId() == R.id.msg_commentList) {
				Log("commentList");
			}
		}
	};

	private OnItemLongClickListener itemLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			showDialog(MSG_DIALOG);
			return true;
		}
	};

	private static final int MSG_DIALOG = 0;

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == MSG_DIALOG) {
			return new AlertDialog.Builder(this).setTitle(
					R.string.weiboFunction).setItems(R.array.weiboFunction,
					null).create();
		}
		return super.onCreateDialog(id);
	}

	void Log(String msg) {
		android.util.Log.i("weibo", "Msg--" + msg);
	}
}
