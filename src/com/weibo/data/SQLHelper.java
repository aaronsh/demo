package com.weibo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class SQLHelper extends SQLiteOpenHelper {
	private static final int VERSION = 1;

	private String create_home_table = "create table " + DB.HOME_TABLE + "("
			+ DB.ID + " TEXT," + DB.CONTENT + " TEXT," + DB.PIC + " TEXT,"
			+ DB.CREATE_TIME + " TEXT," + DB.SOURCE + " TEXT," + DB.COMMENTNUM
			+ " TEXT," + DB.RTNUM + " TEXT," + DB.UID + " TEXT," + DB.NICK
			+ " TEXT," + DB.PORTRAIT + " TEXT," + DB.VIP + " TEXT,"
			+ DB.LONGITUDE + " TEXT," + DB.LATITUDE + " TEXT," + DB.RTID
			+ " TEXT," + DB.RTCONTENT + " TEXT," + DB.RTPIC + " TEXT,"
			+ DB.RTUID + " TEXT," + DB.RTNICK + " TEXT)";

	public SQLHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public SQLHelper(Context context, String name, int version) {
		super(context, name, null, version);
	}

	public SQLHelper(Context context, String name) {
		super(context, name, null, VERSION);
	}

	public SQLHelper(Context context) {
		super(context, DB.SINAWEIBO, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(create_home_table);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
