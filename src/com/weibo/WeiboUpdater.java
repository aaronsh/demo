package com.weibo;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.weibo.adapter.FaceAdapter;
import com.weibo.util.TextUtil;

import dibang.com.R;

import weibo4android.Trends;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import weibo4android.http.ImageItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 发布新微博 评论微博 转发微博
 * 
 * @author starry
 * 
 */
public class WeiboUpdater extends Activity {
	private ImageView back;
	private TextView name;
	private Button send;
	private EditText edit;
	private ImageView contentPic;
	private LinearLayout delete;
	private TextView delete_num;
	private LinearLayout rtAndComment;
	private CheckBox rtAndComment_checkBox;
	private TextView rtAndComment_text;
	private ImageButton location;
	private ImageButton pic;
	private ImageButton topic;
	private ImageButton at;
	private ImageButton face;
	private ImageButton keyboard;
	private LinearLayout bottomBar;
	private LinearLayout trendListLayout;
	private ListView trendList;
	private LinearLayout atListLayout;
	private ListView atList;
	private GridView faceGrid;

	private byte[] contentPicByte;

	private long id;

	private Weibo weibo;

	private int mode;
	public static final String WEIBO_ID = "weiboId";
	public static final String WEIBO_CATE = "weiboCate";
	public static final int UPDATE = 0;
	public static final int REDIRECT = 10;
	public static final int COMMENT = 100;

	// hanlder处理发布、转发、评论微博
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			handler.post(runnable);
		}
	};
	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			String content = edit.getText().toString();
			if (mode == UPDATE) {
				updateWeibo(content);
			} else if (mode == REDIRECT) {
				redirectWeibo(id, content);
			} else if (mode == COMMENT) {
				commentWeibo(id, content);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_updater);
		init();

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			id = bundle.getLong(WEIBO_ID, 0);
			mode = bundle.getInt(WEIBO_CATE, UPDATE);
		}
		switch (mode) {
		case UPDATE:
			name.setText(R.string.updateWeibo);
			rtAndComment.setVisibility(View.GONE);
			location.setVisibility(View.VISIBLE);
			pic.setVisibility(View.VISIBLE);
			break;
		case REDIRECT:
			name.setText(R.string.redirectWeibo);
			rtAndComment.setVisibility(View.VISIBLE);
			rtAndComment_text.setText(R.string.rt_sametime_comment);
			location.setVisibility(View.GONE);
			pic.setVisibility(View.GONE);
			break;
		case COMMENT:
			name.setText(R.string.commentWeibo);
			rtAndComment.setVisibility(View.VISIBLE);
			rtAndComment_text.setText(R.string.comment_sametime_update);
			location.setVisibility(View.GONE);
			pic.setVisibility(View.GONE);
			break;
		default:
			break;
		}
		weibo = Sina.getInstance().getWeibo();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		if (trendListLayout.isShown()) {
			trendListLayout.setVisibility(View.GONE);
			bottomBar.setVisibility(View.VISIBLE);
			return;
		}
		if (atListLayout.isShown()) {
			atListLayout.setVisibility(View.GONE);
			bottomBar.setVisibility(View.VISIBLE);
			return;
		}
		if (faceGrid.isShown()) {
			faceGrid.setVisibility(View.GONE);
			face.setVisibility(View.VISIBLE);
			keyboard.setVisibility(View.GONE);
			return;
		}
		back();
	}

	private void init() {
		getViews();
		setViews();
		setListeners();
	}

	private void getViews() {
		back = (ImageView) findViewById(R.id.weiboUpdater_title_back);
		name = (TextView) findViewById(R.id.weiboUpdater_title_name);
		send = (Button) findViewById(R.id.weiboUpdater_title_send);
		edit = (EditText) findViewById(R.id.weiboUpdater_edit);
		contentPic = (ImageView) findViewById(R.id.updater_content_pic);
		delete = (LinearLayout) findViewById(R.id.weiboUpdater_del);
		delete_num = (TextView) findViewById(R.id.weiboUpdater_del_num);
		rtAndComment = (LinearLayout) findViewById(R.id.weiboUpdater_rtAndComment);
		rtAndComment_checkBox = (CheckBox) findViewById(R.id.weiboUpdater_check);
		rtAndComment_text = (TextView) findViewById(R.id.weiboUpdater_rtAndComment_tv);
		location = (ImageButton) findViewById(R.id.weiboUpdater_bottom_location);
		pic = (ImageButton) findViewById(R.id.weiboUpdater_bottom_pic);
		topic = (ImageButton) findViewById(R.id.weiboUpdater_bottom_topic);
		at = (ImageButton) findViewById(R.id.weiboUpdater_bottom_at);
		face = (ImageButton) findViewById(R.id.weiboUpdater_bottom_face);
		keyboard = (ImageButton) findViewById(R.id.weiboUpdater_bottom_keyboard);
		bottomBar = (LinearLayout) findViewById(R.id.updater_bottomBar);
		trendListLayout = (LinearLayout) findViewById(R.id.updater_trendsList_layout);
		trendList = (ListView) findViewById(R.id.updater_trendsList);
		atListLayout = (LinearLayout) findViewById(R.id.updater_atList_layout);
		atList = (ListView) findViewById(R.id.updater_atList);
		faceGrid = (GridView) findViewById(R.id.updater_faceGrid);
	}

	private void setViews() {

	}

	private void setListeners() {
		back.setOnClickListener(clickListener);
		send.setOnClickListener(clickListener);
		contentPic.setOnClickListener(clickListener);
		delete.setOnClickListener(clickListener);
		location.setOnClickListener(clickListener);
		pic.setOnClickListener(clickListener);
		topic.setOnClickListener(clickListener);
		at.setOnClickListener(clickListener);
		face.setOnClickListener(clickListener);
		keyboard.setOnClickListener(clickListener);
		// 监听编辑字数，实时显示可输入的字数，超过140则无法输入
		edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				int num = s.toString().length();
				if (num > 140) {
					s.delete(140, s.toString().length());
					edit.setText(s);
					edit.setSelection(edit.getText().length());
					return;
				}
				delete_num.setText("" + (140 - num));
			}
		});
		trendList.setOnItemClickListener(itemClickListener);
		atList.setOnItemClickListener(itemClickListener);
		faceGrid.setOnItemClickListener(itemClickListener);
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.weiboUpdater_title_back:
				back();
				break;
			case R.id.weiboUpdater_title_send:
				handler.sendEmptyMessage(0);
				break;
			case R.id.updater_content_pic:
				break;
			case R.id.weiboUpdater_del:
				clean();
				break;
			case R.id.weiboUpdater_bottom_location:
				showLocation();
				break;
			case R.id.weiboUpdater_bottom_pic:
				showPic();
				break;
			case R.id.weiboUpdater_bottom_topic:
				showTopic();
				break;
			case R.id.weiboUpdater_bottom_at:
				showAt();
				break;
			case R.id.weiboUpdater_bottom_face:
				showFace();
				break;
			case R.id.weiboUpdater_bottom_keyboard:
				showKeyboard();
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
			switch (arg0.getId()) {
			case R.id.updater_trendsList:
				String trend = (String) trendList.getAdapter().getItem(arg2);
				edit.append(TextUtil.formatContent("#"+trend+"#", WeiboUpdater.this));
				break;
			case R.id.updater_atList:
				String at = (String) atList.getAdapter().getItem(arg2);
				edit.append(TextUtil.formatContent("@"+at+" ", WeiboUpdater.this));
				break;
			case R.id.updater_faceGrid:
				if (arg2 < Face.faceNames.length) {
					edit.append(TextUtil.formatImage("[" + Face.faceNames[arg2]
							+ "]", WeiboUpdater.this));
				}
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
		String content = edit.getText().toString();
		if (!content.equals("")) { // 若当前内容不为空，提示是否保存
			showDialog(DIALOG_SAVE);
		} else {
			finish();
		}
	}

	/**
	 * 发布微博
	 */
	private void updateWeibo(String content) {
		try {
			weibo.updateStatus(content);
			WeiboToast.show(getApplicationContext(), "发布成功");
			finish();
		} catch (WeiboException e) {
			e.printStackTrace();
			WeiboToast.show(getApplicationContext(), "发布失败");
		}
	}

	/**
	 * 转发微博
	 * 
	 * @param weiboId
	 *            微博id
	 * @param content
	 *            转发内容
	 */
	private void redirectWeibo(long weiboId, String content) {
		if ("".equals(content)) {
			content = "转发微博";
		}
		if (rtAndComment_checkBox.isChecked()) {
			try {
				weibo.repost(String.valueOf(weiboId), content, 1);
				WeiboToast.show(getApplicationContext(), "转发成功");
				finish();
			} catch (WeiboException e) {
				e.printStackTrace();
				WeiboToast.show(getApplicationContext(), "转发失败");
			}
		} else {
			try {
				weibo.repost(String.valueOf(weiboId), content, 0);
				WeiboToast.show(getApplicationContext(), "转发成功");
				finish();
			} catch (WeiboException e) {
				e.printStackTrace();
				WeiboToast.show(getApplicationContext(), "转发失败");
			}
		}
	}

	/**
	 * 评论微博
	 * 
	 * @param weiboId
	 *            微博id
	 * @param content
	 *            评论内容
	 */
	private void commentWeibo(long weiboId, String content) {
		if ("".equals(content)) {
			WeiboToast.show(getApplicationContext(), "请输入评论信息");
			return;
		}
		try {
			weibo.updateComment(content, String.valueOf(weiboId), null);
			WeiboToast.show(getApplicationContext(), "评论成功");
			if (!rtAndComment_checkBox.isChecked()) {
				finish();
			}
		} catch (WeiboException e) {
			e.printStackTrace();
			WeiboToast.show(getApplicationContext(), "评论失败");
		}
		if (rtAndComment_checkBox.isChecked()) {
			updateWeibo(content);
		}
	}

	/**
	 * 清除文字
	 */
	private void clean() {
		String content = edit.getText().toString();
		if (!content.equals("")) {
			showDialog(DIALOG_CLEAN);
		}
	}

	/**
	 * 显示地理信息（未完成）
	 */
	private void showLocation() {

	}

	/**
	 * 插入图片（未完成）
	 */
	private void showPic() {
		showDialog(DIALOG_PIC);
	}

	/**
	 * 显示话题列表
	 */
	private void showTopic() {
		bottomBar.setVisibility(View.GONE);
		atListLayout.setVisibility(View.GONE);
		faceGrid.setVisibility(View.GONE);
		keyboard.setVisibility(View.GONE);
		face.setVisibility(View.VISIBLE);
		trendListLayout.setVisibility(View.VISIBLE);
		if (trendList.getAdapter() == null) {
			try {
				List<Trends> trends = weibo.getTrendsWeekly(0);
				ArrayList<String> trendNames = new ArrayList<String>();
				for (int i = 0; i < trends.size(); i++) {
					for (int j = 0; j < trends.get(i).getTrends().length; j++) {
						trendNames.add(trends.get(i).getTrends()[j].getName());
					}
				}
				ArrayAdapter<String> topicAdapter = new ArrayAdapter<String>(
						this, R.layout.textview, R.id.textview_tv, trendNames);
				trendList.setAdapter(topicAdapter);
			} catch (WeiboException e) {
				e.printStackTrace();
				WeiboToast.show(getApplicationContext(), "话题列表获取失败");
			}
		}
	}

	/**
	 * 显示@列表（未完成）
	 */
	private void showAt() {
		bottomBar.setVisibility(View.GONE);
		trendListLayout.setVisibility(View.GONE);
		faceGrid.setVisibility(View.GONE);
		keyboard.setVisibility(View.GONE);
		face.setVisibility(View.VISIBLE);
		atListLayout.setVisibility(View.VISIBLE);

		if (atList.getAdapter() == null) {
			ArrayAdapter<String> topicAdapter = new ArrayAdapter<String>(this,
					R.layout.textview, R.id.textview_tv, WeiboData
							.getHomeNames());
			atList.setAdapter(topicAdapter);
		}
	}

	/**
	 * 显示表情
	 */
	private void showFace() {
		trendListLayout.setVisibility(View.GONE);
		atListLayout.setVisibility(View.GONE);
		faceGrid.setVisibility(View.VISIBLE);
		face.setVisibility(View.GONE);
		keyboard.setVisibility(View.VISIBLE);
		if (faceGrid.getAdapter() == null) {
			faceGrid.setAdapter(new FaceAdapter(this, Face.faceNames));
		}
	}

	/**
	 * 显示键盘
	 */
	private void showKeyboard() {
		faceGrid.setVisibility(View.GONE);
		keyboard.setVisibility(View.GONE);
		face.setVisibility(View.VISIBLE);
	}

	private static final int DIALOG_SAVE = 0; // 提示是否保存
	private static final int DIALOG_CLEAN = 1; // 清空文字
	private static final int DIALOG_PIC = 2; // 询问图片来源

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		switch (id) {
		case DIALOG_SAVE:
			return new AlertDialog.Builder(this).setTitle(R.string.note)
					.setMessage(R.string.isSave).setPositiveButton(R.string.ok,
							saveListener).setNegativeButton(R.string.cancel,
							null).create();
		case DIALOG_CLEAN:
			return new AlertDialog.Builder(this).setTitle(R.string.note)
					.setMessage(R.string.clean).setPositiveButton(R.string.ok,
							cleanListener).setNegativeButton(R.string.cancel,
							null).create();
		case DIALOG_PIC:
			return new AlertDialog.Builder(this).setTitle(R.string.setting)
					.setItems(R.array.picFrom, picListener).create();
		default:
			return super.onCreateDialog(id, args);
		}
	}

	DialogInterface.OnClickListener saveListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			saveWeibo();
			finish();
		}
	};
	DialogInterface.OnClickListener cleanListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			edit.setText("");
		}
	};

	DialogInterface.OnClickListener picListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which == 0) {

			} else if (which == 1) {
				Intent intent = new Intent();
				// Type为image
				intent.setType("image/*");
				// Action:选择数据然后返回
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, 0);
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			try {
				InputStream is = getContentResolver().openInputStream(uri);
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
						1024);
				byte[] buffer = new byte[1024];
				int lenth;
				while ((lenth = is.read(buffer)) >= 0) {
					outputStream.write(buffer, 0, lenth);
				}
				contentPicByte = outputStream.toByteArray();
				// Drawable drawable = BitmapDrawable.createFromStream(
				// getContentResolver().openInputStream(uri), "");
				Bitmap b = BitmapFactory.decodeByteArray(contentPicByte, 0,
						contentPicByte.length);
				// contentPic.setImageDrawable(drawable);
				contentPic.setImageBitmap(b);
				b = null;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 保存要发布的微博信息
	 */
	private void saveWeibo() {

	}

	/**
	 * 获取保存的微博信息
	 * 
	 * @return
	 */
	private String getWeibo() {
		return null;
	}

	void Log(String msg) {
		android.util.Log.i("weibo", "update---" + msg);
	}

}
