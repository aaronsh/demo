package com.android.camera;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

public class ImageDb extends SQLiteOpenHelper {
	public static final int COL_INDEX_ID = 0;
	public static final int COL_INDEX_CAT = 1;
	public static final int COL_INDEX_PATH = 2;
	public static final int COL_INDEX_LINK = 3;
	
	protected static final int DATABASE_VERSION = 1;
	protected static final String DB_NAME = "images.db";


	private static final String Key_id = "_id";
	private static final String Key_category = "cat";
	private static final String Key_path = "path";
	private static final String Key_link = "link";

	private String TBL_NAME = null;

	private SQLiteDatabase db;

	public ImageDb(Context c) {
		super(c, DB_NAME, null, DATABASE_VERSION);
		TBL_NAME = getTableName();
	}


	@Override
	protected void finalize() {
			close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		TBL_NAME = getTableName();
		StringBuilder b = new StringBuilder("create table ");
		b.append(TBL_NAME);
		b.append("(_id integer primary key autoincrement,cat text, path text, link text)");
		db.execSQL(b.toString());
	}

	public void insert(ContentValues values) {
		SQLiteDatabase db = getWritableDatabase();
		db.insert(TBL_NAME, null, values);
		db.close();
	}
	
	public void insert(String cat, String path, String link)
	{
		ContentValues values = new ContentValues();

		values.put(Key_category, cat);
		values.put(Key_path, path);
		values.put(Key_link, link);
		obtainWritableDatabase();
		db.insert(TBL_NAME, null, values);
	}


	public Cursor query() {
		obtainWritableDatabase();
		Cursor c = db.query(TBL_NAME, null, null, null, null, null, null);
		return c;
	}


	public void del(int id) {
		obtainWritableDatabase();
		db.delete(TBL_NAME, "_id=?", new String[] { String.valueOf(id) });
	}

	public void close() {
		if (db != null) {
			db.close();
			db = null;
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "drop table " + TBL_NAME;
		db.execSQL(sql);
		onCreate(db);
	}

	private void obtainWritableDatabase() {
		if (db == null) {
			db = getWritableDatabase();
		} else if (db.isReadOnly()) {
			close();
			db = getWritableDatabase();
		}

	}
	
	protected String getTableName()
	{
		return "effect";
	}
}