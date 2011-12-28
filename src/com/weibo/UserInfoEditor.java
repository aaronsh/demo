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
import android.widget.RadioButton;
import android.widget.TextView;
/**
 * 用户资料编辑（只能编辑自己的资料）
 * @author starry
 *
 */
public class UserInfoEditor extends Activity {
	private Button titleCancel;
	private TextView titleName;
	private Button titleSave;
	private LinearLayout updateIcon;
	private ImageView icon;
	private RadioButton male;
	private RadioButton female;
	private LinearLayout updateNickName;
	private TextView nickName;
	private LinearLayout updateIntro;
	private TextView intro;

	private User user;
	public static final String USER = "user";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfoeditor);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			user = (User) bundle.getSerializable(USER);
		} else {
			try {
				user = Sina.getInstance().getWeibo().showUser("Patrrr");
			} catch (WeiboException e) {
				e.printStackTrace();
			}
		}
		getViews();
		setViews();
		setListeners();
	}

	private void getViews() {
		LinearLayout titlebar = (LinearLayout) findViewById(R.id.editor_titlebar);
		titleCancel = (Button) titlebar.findViewById(R.id.titlebar_back);
		titleName = (TextView) titlebar.findViewById(R.id.titlebar_name);
		titleSave = (Button) titlebar.findViewById(R.id.titlebar_home);
		male = (RadioButton) findViewById(R.id.editor_male);
		female = (RadioButton) findViewById(R.id.editor_female);
		updateIcon = (LinearLayout) findViewById(R.id.editor_updateIcon);
		icon = (ImageView) findViewById(R.id.editor_icon);
		updateNickName = (LinearLayout) findViewById(R.id.editor_updateNickName);
		nickName = (TextView) findViewById(R.id.editor_nickName);
		updateIntro = (LinearLayout) findViewById(R.id.editor_updateIntro);
		intro = (TextView) findViewById(R.id.editor_intro);
	}

	private void setViews() {
		titleCancel.setVisibility(View.VISIBLE);
		titleName.setVisibility(View.VISIBLE);
		titleSave.setVisibility(View.VISIBLE);
		titleName.setText(R.string.editInfo);

		nickName.setText(user.getScreenName());
		intro.setText(user.getDescription());
		log(user.getGender());
		if (user.getGender().equals("m")) {
			male.setChecked(true);
		} else {
			female.setChecked(true);
		}
	}

	private void setListeners() {
		titleCancel.setOnClickListener(clickListener);
		titleSave.setOnClickListener(clickListener);
		updateIcon.setOnClickListener(clickListener);
		updateNickName.setOnClickListener(clickListener);
		updateIntro.setOnClickListener(clickListener);
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.titlebar_back:
				back();
				break;
			case R.id.titlebar_home:
				save();
				break;
			case R.id.editor_updateIcon:
				updateIcon();
				break;
			case R.id.editor_updateNickName:
				updateNickName();
				break;
			case R.id.editor_updateIntro:
				updateIntor();
				break;
			default:
				break;
			}
		}
	};

	private void back() {
		finish();
	}

	private void save() {
		String name = nickName.getText().toString();
		String gender = "m";
		if (male.isChecked()) {
			gender = "m";
		} else {
			gender = "f";
		}
		String description = intro.getText().toString();
		try {
			Sina.getInstance().getWeibo().updateProfile(name, gender,
					description);
			WeiboToast.show(this, "更改资料成功");
		} catch (WeiboException e) {
			log(e.toString());
			e.printStackTrace();
		}
	}

	private void updateIcon() {

	}

	private void updateNickName() {

	}

	private void updateIntor() {

	}

	void log(String msg) {
		Log.i("weibo", "UserInfoEdit--" + msg);
	}

}
