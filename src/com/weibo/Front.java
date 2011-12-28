package com.weibo;

import java.util.ArrayList;

import dibang.com.R;

import weibo4android.Weibo;
import weibo4android.WeiboException;
import weibo4android.http.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
/**
 * 欢迎界面
 * @author starry
 *
 */
public class Front extends Activity {
	private Spinner accountSelector;
	private CheckBox remenberMe;
	private CheckBox autoSignIn;
	private Button login;
	private Button exit;
	private Button newAuthorize;

	private String userName;
	private String[] userNames;
	
	public static boolean isRemenber;
	public static boolean isAutoLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.front);
		getViews();
		setListeners();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setViews();
	}

	private void getViews() {
		accountSelector = (Spinner) findViewById(R.id.front_accountSelector);
		remenberMe = (CheckBox) findViewById(R.id.front_remenberMe);
		autoSignIn = (CheckBox) findViewById(R.id.front_autoSignIn);
		login = (Button) findViewById(R.id.front_login);
		exit = (Button) findViewById(R.id.front_exit);
		newAuthorize = (Button) findViewById(R.id.front_register);
	}

	private void setViews() {
		userNames = getAccountNames();
		ArrayAdapter accountSelectorAdapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, userNames);
		accountSelectorAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		accountSelector.setAdapter(accountSelectorAdapter);
	}

	private void setListeners() {
		accountSelector.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				userName = userNames[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		login.setOnClickListener(clickListener);
		exit.setOnClickListener(clickListener);
		newAuthorize.setOnClickListener(clickListener);
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.front_login:
				toMainTabs(userName);
				break;
			case R.id.front_exit:
				exit();
				break;
			case R.id.front_register:
				startOAuth();
				break;
			default:
				break;
			}
		}
	};
	
	/**
	 * 进入MainTabs，开始显示首页（Home）
	 * @param name
	 */
	private void toMainTabs(String name) {
		if(name==null||"".equals(name))
			return;
		Intent i = new Intent(Front.this, MainTabs.class);
		i.putExtras(getAccountInfo(name));
		startActivity(i);
	}
	/**
	 * 退出
	 */
	private void exit() {
		finish();
	}
	/**
	 * 转入OAuth授权界面
	 */
	private void startOAuth() {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret",
				Weibo.CONSUMER_SECRET);
		Weibo weibo = new Weibo();
		RequestToken requestToken;
		try {
			requestToken = weibo
					.getOAuthRequestToken("weibo4android://OAuthActivity");
			OAuthConstant.getInstance().setRequestToken(requestToken);
			Uri uri = Uri.parse(requestToken.getAuthenticationURL()
					+ "&display=mobile");
			startActivity(new Intent(Intent.ACTION_VIEW, uri));
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取自动登录的帐号名称
	 * @return
	 */
	private String getAutoLoginName(){
		SharedPreferences sharedPreferences=getSharedPreferences(AccountInfo.AUTO_LOGIN, MODE_PRIVATE);
		String nick=sharedPreferences.getString(AccountInfo.NICK, "");
		return nick;
	}
	/**
	 * 获取用户名列表
	 * @return  String[]
	 */
	private String[] getAccountNames() {
		ArrayList<String> accounts = new ArrayList<String>();
		int i = 0;
		SharedPreferences preferences = getSharedPreferences(AccountInfo.ACCOUNTS, MODE_PRIVATE);
		while (preferences.contains(AccountInfo.NICK + i)) {
			String name = preferences.getString(AccountInfo.NICK + i, "");
			accounts.add(name);
			i++;
		}
		return accounts.toArray(new String[] {});
	}
	/**
	 * 获取用户信息  用户id & token & tokenSecret
	 * @param name 用户名
	 * @return
	 */
	private Bundle getAccountInfo(String name) {
		Bundle bundle = new Bundle();
		SharedPreferences preferences = getSharedPreferences(AccountInfo.ACCOUNTS,
				MODE_PRIVATE);
		for (int i = 0; i < userNames.length; i++) {
			if (preferences.getString(AccountInfo.NICK + i, "").equals(name)) {
				String userId = preferences.getString(AccountInfo.ID + i, "");
				String token = preferences.getString(AccountInfo.TOKEN + i, "");
				String tokenSecret = preferences.getString(AccountInfo.TOKEN_SECRET + i, "");

				bundle.putString(AccountInfo.NICK, name);
				bundle.putString(AccountInfo.ID, userId);
				bundle.putString(AccountInfo.TOKEN, token);
				bundle.putString(AccountInfo.TOKEN_SECRET, tokenSecret);
				break;
			}
		}
		return bundle;
	}

	void Log(String msg) {
		Log.i("weibo", "front--" + msg);
	}

}
