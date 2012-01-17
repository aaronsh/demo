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
	 * ���뷢��΢������
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
	 * ����ת��΢������
	 * @param context
	 * @param id Ҫת����΢��id
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
	 * ���������б����
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
	 * ��������΢������
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
	 * ������ҳ/Home
	 * @param context
	 */
	public void backToHome(Context context) {
		Intent i=new Intent(context, Home.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(i);
	}
	/**
	 * ����΢����ϸ��Ϣ����
	 * @param context
	 * @param status ��Ӧ΢����Status
	 */
	public void goToDetail(Context context, Status status) {
		Intent i = new Intent(context, WeiboDetail.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(WeiboDetail.STATUS, status);		
		i.putExtras(bundle);
		context.startActivity(i);
	}
	/**
	 * �����û����Ͻ���
	 * @param context
	 * @param userId ��Ӧ�û�id
	 */
	public void goToUserInfo(Context context, long userId) {
		Intent i = new Intent(context, UserInfo.class);
		Bundle bundle = new Bundle();
		bundle.putLong(UserInfo.USER_ID, userId);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	/**
	 * �����û����ϱ༭���棨��֧�ֱ༭�Լ������ϣ�
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
	 * ��ʾ��ע����
	 * @param context
	 * @param userId ��Ӧ���û�id
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
	 * ��ʾ�����΢������
	 * @param context
	 * @param userId ��Ӧ���û�id
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
	 * ��ʾ��˿����
	 * @param context
	 * @param userId ��Ӧ���û�id
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
	 * ��ʾ������棨δ��ɣ�
	 */
	public void ShowTopic() {

	}
	/**
	 * ��ʾ�ղؽ��棨��֧����ʾ�Լ����ղأ�
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
	 * ��ʾ���������棨�ݲ�֧�֣���SDK������δ�޸ģ�
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
