package com.weibo.data;

public class DB {
	public static final String SINAWEIBO = "sina_weibo"; //databse name
	public static final String HOME_TABLE = "home_table"; //weibo table name
	
	/*
	 * weibo table field
	 *   *package*
	 */
	static final String ID="id"; //id
	static final String CONTENT = "content"; //content
	static final String PIC = "pic"; //content picture
	static final String CREATE_TIME = "time"; //create time
	static final String SOURCE = "src"; //source
	static final String COMMENTNUM = "commentnum"; //comment count
	static final String RTNUM = "rtnum"; //redirect count
	static final String LONGITUDE = "longitude"; //longitude
	static final String LATITUDE = "latitude"; //latitude
	
	static final String UID = "uid"; //user id
	static final String NICK = "nick"; //user nick
	static final String PORTRAIT = "portrait"; //user portrait
	static final String VIP = "vip"; //user verified
	
	static final String RTID = "rtid"; //rt id
	static final String RTCONTENT = "rtcontent"; //rt content
	static final String RTPIC="rtpic"; //rt picture
	
	static final String RTUID = "rtuid"; //rt user id
	static final String RTNICK = "rtnick"; //rt user nick

}
