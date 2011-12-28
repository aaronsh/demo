package com.weibo;

import java.util.List;

import weibo4android.Comment;
import weibo4android.WeiboException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.weibo.adapter.CommentListAdapter;

import dibang.com.R;

public class CommentList extends Activity implements InitViews {
	private Button titleBack;
	private Button titleComment;
	private ListView cList;
	
	private List<Comment> commentList;
	
	private int clickLocation;

	public long weiboId;
	public static final String WEIBO_ID = "weiboId";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_list);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			weiboId = bundle.getLong(WEIBO_ID, 0);
		}
		getViews();
		setViews();
		setListeners();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private AsyncDataLoader.Callback asyncCallback = new AsyncDataLoader.Callback() {
		CommentListAdapter adapter;
		
		@Override
		public void onStart() {
			try {
				commentList=Sina.getInstance().getWeibo().getComments(String.valueOf(weiboId));
				adapter = new CommentListAdapter(CommentList.this, commentList);
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
			cList.setAdapter(adapter);			
		}
	};

	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.commentList_title_back:
				back();
				break;
			case R.id.commentList_title_comment:
				commentWeibo(weiboId);
				break;
			default:
				break;
			}
		}
	};

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			clickLocation = arg2;
			showDialog(DIALOG_OPTION);
		}
	};
	
	private DialogInterface.OnClickListener dialogClickListener=new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(which == 0){
				replyComment();
			}else if(which == 1){
				viewProfile(commentList.get(clickLocation).getUser().getId());
			}
		}
	};

	private final int DIALOG_OPTION = 0;

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		switch (id) {
		case DIALOG_OPTION:
			return new AlertDialog.Builder(this).setTitle(R.string.comment)
					.setItems(R.array.commentList_options, dialogClickListener).create();
		default:
			break;
		}
		return super.onCreateDialog(id, args);
	}
	/**
	 * 返回
	 */
	private void back() {
		finish();
	}
	/**
	 * 转到评论界面
	 * @param id
	 */
	private void commentWeibo(long id) {
		Sina.getInstance().commentWeibo(this, id);
	}
	/**
	 * 评论选中的评论(???)
	 */
	private void replyComment() {
		
	}
	/**
	 * 查看个人资料
	 */
	private void viewProfile(long uId) {
		Sina.getInstance().goToUserInfo(this, uId);
	}

	@Override
	public void getViews() {
		titleBack = (Button) findViewById(R.id.commentList_title_back);
		titleComment = (Button) findViewById(R.id.commentList_title_comment);
		cList = (ListView) findViewById(R.id.commentList_list);
	}

	@Override
	public void setListeners() {
		titleBack.setOnClickListener(clickListener);
		titleComment.setOnClickListener(clickListener);
		cList.setOnItemClickListener(itemClickListener);
	}

	@Override
	public void setViews() {
		new AsyncDataLoader(asyncCallback).execute();
	}

	void Log(String msg) {
		android.util.Log.i("weibo", "CommentList--" + msg);
	}

}
