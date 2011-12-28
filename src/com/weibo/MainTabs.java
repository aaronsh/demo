package com.weibo;

import dibang.com.R;
import weibo4android.User;
import weibo4android.WeiboException;
import weibo4android.http.AccessToken;
import weibo4android.http.RequestToken;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainTabs extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintabs);
		
		oAuth();

		final TabHost tabHost = getTabHost();

		Intent home = new Intent(this, Home.class);
		TabHost.TabSpec homeSpec = tabHost.newTabSpec("");
		homeSpec.setIndicator("");
		homeSpec.setContent(home);
		tabHost.addTab(homeSpec);

		Intent msg = new Intent(this, Msg.class);
		TabHost.TabSpec msgSpec = tabHost.newTabSpec("");
		msgSpec.setIndicator("");
		msgSpec.setContent(msg);
		tabHost.addTab(msgSpec);

		Intent selInfo = new Intent(this, SelfInfo.class);
		TabHost.TabSpec selSpec = tabHost.newTabSpec("");
		selSpec.setIndicator("");
		selSpec.setContent(selInfo);
		tabHost.addTab(selSpec);

		Intent square = new Intent(this, Square.class);
		TabHost.TabSpec squareSpec = tabHost.newTabSpec("");
		squareSpec.setIndicator("");
		squareSpec.setContent(square);
		tabHost.addTab(squareSpec);

		Intent more = new Intent(this, More.class);
		TabHost.TabSpec moreSpec = tabHost.newTabSpec("");
		moreSpec.setIndicator("");
		moreSpec.setContent(more);
		tabHost.addTab(moreSpec);

		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.mainTabs_radioGroup);
		RadioButton homeRadio = (RadioButton) findViewById(R.id.mainTabs_radio_home);
		homeRadio.setChecked(true);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.mainTabs_radio_home:
					tabHost.setCurrentTab(0);
					break;
				case R.id.mainTabs_radio_msg:
					tabHost.setCurrentTab(1);
					break;
				case R.id.mainTabs_radio_selfInfo:
					tabHost.setCurrentTab(2);
					break;
				case R.id.mainTabs_radio_square:
					tabHost.setCurrentTab(3);
					break;
				case R.id.mainTabs_radio_more:
					tabHost.setCurrentTab(4);
					break;
				default:
					break;
				}
			}
		});
	}
	
	private void oAuth() {
		Uri uri = this.getIntent().getData();
		if(uri==null){ //通过本地保存的帐号信息登录
			Bundle bundle=getIntent().getExtras();
			String id="";
			String token="";
			String tokenSecret="";
			if(bundle!=null){
				id=bundle.getString(AccountInfo.ID);
				token=bundle.getString(AccountInfo.TOKEN);
				tokenSecret=bundle.getString(AccountInfo.TOKEN_SECRET);
			}else{
				Log("bundle null");
			}
			OAuthConstant.getInstance().setUserId(Long.parseLong(id));
			OAuthConstant.getInstance().setToken(token);
			OAuthConstant.getInstance().setTokenSecret(tokenSecret);
			return;
		}
		//通过新浪微博授权页面登录
		OAuthConstant oAuthConstant = OAuthConstant.getInstance();
		try {
			RequestToken requestToken = oAuthConstant.getRequestToken();
			AccessToken accessToken = requestToken.getAccessToken(uri.getQueryParameter("oauth_verifier"));
			oAuthConstant.setAccessToken(accessToken);
			oAuthConstant.setUserId(accessToken.getUserId());
			Log(oAuthConstant.getToken()+"---"+oAuthConstant.getTokenSecret());
		} catch (WeiboException e) {
			e.printStackTrace();
			Log(e.toString());
		}
		//验证身份
		User user = verifyCredentials(OAuthConstant.getInstance().getUserId());
		if (user != null) {
			//保存帐号信息
			saveAccounts(user.getScreenName(), String.valueOf(user.getId()),
					oAuthConstant.getToken(), oAuthConstant.getTokenSecret());
		}
	}
	/**
	 * 验证当前用户身份
	 * @param userId
	 * @return
	 */
	private User verifyCredentials(long userId) {
		try {
			return Sina.getInstance().getWeibo().verifyCredentials();
		} catch (WeiboException e) {
			e.printStackTrace();
			WeiboToast.show(this, "用户验证失败");
			return null;
		}
	}
	/**
	 * 保存帐号信息
	 * @param name
	 * @param id
	 * @param token
	 * @param tokenSecrect
	 */
	/*
	 * 键： USER_*** + 一个数字,如 USER_NAME0,USER_NAME1，USER_TOKEN3;
	 * 数字确定：第一个保存的为0号,第二个为1号，以此类推。
	 */
	private void saveAccounts(String name, String id, String token,
			String tokenSecrect) {
		SharedPreferences preferences = getSharedPreferences(AccountInfo.ACCOUNTS,
				MODE_PRIVATE);
		for (int i = 0;; i++) {
			if (preferences.contains(AccountInfo.NICK + i)) { //遍历所有帐号名
				if (preferences.getString(AccountInfo.NICK + i, "").equals(name)) { //若已存在帐号名，则不保存
					break;
				}
			} else {
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString(AccountInfo.NICK + i, name);
				editor.putString(AccountInfo.ID + i, id);
				editor.putString(AccountInfo.TOKEN + i, token);
				editor.putString(AccountInfo.TOKEN_SECRET + i, tokenSecrect);
				editor.commit();
				break;
			}
		}
	}

	private void Log(String string) {
		Log.i("weibo", "MainTabs--" + string);
	}

}
