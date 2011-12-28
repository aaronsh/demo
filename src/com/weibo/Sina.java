package com.weibo;

import weibo4android.Status;
import weibo4android.User;
import weibo4android.Weibo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Sina {	
	private static Sina sina;
	private static Weibo weibo;
	
	public synchronized static Sina getInstance() {
		if (sina == null) {
			sina = new Sina();
		}
		return sina;
	}
	
	public Weibo getWeibo(){
		if(weibo==null){
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret",
					Weibo.CONSUMER_SECRET);
			weibo=new Weibo();
			//weibo.setOAuthAccessToken(OAuthConstant.getInstance().getToken(),
			//		OAuthConstant.getInstance().getTokenSecret());
			
			//for test
			weibo.setOAuthAccessToken("b0da207d8c6035f0ba8e139bda95bba8", "9b635c3f032a3be4c8f70e4e61099f9a");
		}
		return weibo;
	}
	/**
	 * 进入发布微博界面
	 * @param context
	 */
	public void updateWeibo(Context context) {
		Intent i = new Intent(context, WeiboUpdater.class);
		Bundle bundle = new Bundle();
		bundle.putInt(WeiboUpdater.WEIBO_CATE, WeiboUpdater.UPDATE);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	/**
	 * 进入转发微博界面
	 * @param context
	 * @param id 要转发的微博id
	 */
	public void redirectWeibo(Context context, long id) {
		Intent i = new Intent(context, WeiboUpdater.class);
		Bundle bundle = new Bundle();
		bundle.putInt(WeiboUpdater.WEIBO_CATE, WeiboUpdater.REDIRECT);
		bundle.putLong(WeiboUpdater.WEIBO_ID, id);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	/**
	 * 进入评论列表界面
	 * @param context context
	 * @param id
	 */
	public void goToCommentList(Context context,long id){
		Intent i =new Intent(context,CommentList.class);
		Bundle bundle=new Bundle();
		bundle.putLong(CommentList.WEIBO_ID, id);
		i.putExtras(bundle);
		context.startActivity(i);		
	}
	/**
	 * 进入评论微博界面
	 * @param context
	 * @param id
	 */
	public void commentWeibo(Context context, long id) {
		Intent i = new Intent(context, WeiboUpdater.class);
		Bundle bundle = new Bundle();
		bundle.putInt(WeiboUpdater.WEIBO_CATE, WeiboUpdater.COMMENT);
		bundle.putLong(WeiboUpdater.WEIBO_ID, id);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	/**
	 * 返回首页/Home
	 * @param context
	 */
	public void backToHome(Context context) {
		Intent i=new Intent(context, Home.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(i);
	}
	/**
	 * 进入微博详细信息界面
	 * @param context
	 * @param status 对应微博的Status
	 */
	public void goToDetail(Context context, Status status) {
		Intent i = new Intent(context, WeiboDetail.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(WeiboDetail.STATUS, status);		
		i.putExtras(bundle);
		context.startActivity(i);
	}
	/**
	 * 进入用户资料界面
	 * @param context
	 * @param userId 对应用户id
	 */
	public void goToUserInfo(Context context, long userId) {
		Intent i = new Intent(context, UserInfo.class);
		Bundle bundle = new Bundle();
		bundle.putLong(UserInfo.USER_ID, userId);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	/**
	 * 进入用户资料编辑界面（仅支持编辑自己的资料）
	 * @param context
	 * @param user
	 */
	public void goToInfoEditor(Context context,User user){
		Intent i = new Intent(context, UserInfoEditor.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(UserInfoEditor.USER, user);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	/**
	 * 显示关注界面
	 * @param context
	 * @param userId 对应的用户id
	 */
	public void showAttention(Context context, long userId) {
		Intent i=new Intent(context,UserList.class);
		Bundle bundle=new Bundle();
		bundle.putInt(UserList.USER_CATE, UserList.CATE_ATTENTION);
		bundle.putLong(UserList.USER_ID, userId);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	/**
	 * 显示发表的微博界面
	 * @param context
	 * @param userId 对应的用户id
	 */
	public void showWeibo(Context context,long userId){
		Intent i=new Intent(context,WeiboList.class);
		Bundle bundle=new Bundle();
		bundle.putInt(WeiboList.WEIBO_CATE, WeiboList.CATE_ALL);
		bundle.putLong(WeiboList.USER_ID, userId);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	/**
	 * 显示粉丝界面
	 * @param context
	 * @param userId 对应的用户id
	 */
	public void showFans(Context context, long userId) {
		Intent i=new Intent(context,UserList.class);
		Bundle bundle=new Bundle();
		bundle.putInt(UserList.USER_CATE, UserList.CATE_FANS);
		bundle.putLong(UserList.USER_ID, userId);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	/**
	 * 显示话题界面（未完成）
	 */
	public void ShowTopic() {

	}
	/**
	 * 显示收藏界面（仅支持显示自己的收藏）
	 * @param context
	 */
	public void showFavorite(Context context) {
		Intent i=new Intent(context,WeiboList.class);
		Bundle bundle=new Bundle();
		bundle.putInt(WeiboList.WEIBO_CATE, WeiboList.CATE_FAVORITE);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	/**
	 * 显示黑名单界面（暂不支持）（SDK有误，尚未修改）
	 * @param context
	 * @param userId
	 */
	public void showBlacklist(Context context,long userId) {
		Intent i=new Intent(context,UserList.class);
		Bundle bundle=new Bundle();
		bundle.putInt(UserList.USER_CATE, UserList.CATE_BLACKLIST);
		bundle.putLong(UserList.USER_ID, userId);
		i.putExtras(bundle);
		context.startActivity(i);
	}

}
