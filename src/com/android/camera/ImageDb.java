package com.android.camera;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

public class ImageDb extends SQLiteOpenHelper {
	public static final int COL_INDEX_ID = 0;
	public static final int COL_INDEX_CLASS = 1;
	public static final int COL_INDEX_PATH = 2;
	public static final int COL_INDEX_LINK = 3;
	
	public static final String TBL_EFFECT_SHOW = "effect_show";
	public static final String TBL_HOUSE_SHOW = "house_show";
	
	protected static final int DATABASE_VERSION = 2;
	private static final String DB_NAME = "rendering.db";


	private static final String Key_id = "_id";
	private static final String Key_class = "class";
	private static final String Key_path = "path";
	private static final String Key_link = "link";
	private static final String TAG = "ImageDb";

	private String TBL_NAME = null;

	private SQLiteDatabase mDB=null;

	public ImageDb(Context c, String tbl) {
		super(c, DB_NAME, null, DATABASE_VERSION);
		TBL_NAME = tbl;
	}


	@Override
	protected void finalize() {
		Log.v(TAG, "ImageDb.finalize()");
			close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		mDB = db;
		StringBuilder b = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
		b.append(TBL_EFFECT_SHOW);
		b.append("(_id integer primary key autoincrement, class text, path text, link text)");
		db.execSQL(b.toString());
		b = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
		b.append(TBL_HOUSE_SHOW);
		b.append("(_id integer primary key autoincrement, class text, path text, link text)");
		db.execSQL(b.toString());
	}
	
	public void onOpen (SQLiteDatabase db){
		Log.v(TAG, "onOpen");
		super.onOpen(db);
	}

	
	public void insert(String imgClass, String path, String link)
	{
		ContentValues values = new ContentValues();

		values.put(Key_class, imgClass);
		values.put(Key_path, path);
		values.put(Key_link, link);
		obtainWritableDatabase();
		mDB.insert(TBL_NAME, null, values);
	}
	
	public void clear()
	{
		obtainWritableDatabase();
		/*		
			try {
				// mDatabase.execSQL("TRUNCATE TABLE Users");
				db.execSQL("DELETE FROM " + TBL_NAME);
				db.execSQL("update sqlite_sequence set seq=0 where name=\"" + TBL_NAME +"\"");
			} catch (SQLException se) {
				se.printStackTrace();
			}
*/
		String sql = "drop table " + TBL_NAME;
		mDB.execSQL(sql);
		onCreate(mDB);
	}


	public Cursor query() {
		obtainWritableDatabase();
		Cursor c = mDB.query(TBL_NAME, null, null, null, null, null, null);
		return c;
	}


	public void del(int id) {
		obtainWritableDatabase();
		mDB.delete(TBL_NAME, "_id=?", new String[] { String.valueOf(id) });
	}

	public void close() {
		if (mDB != null) {
			mDB.close();
			mDB = null;
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "drop table " + TBL_NAME;
		mDB.execSQL(sql);
		onCreate(mDB);
	}

	private void obtainWritableDatabase() {
		if (mDB == null) {
			mDB = getWritableDatabase();
		} else if (mDB.isReadOnly()) {
			close();
			mDB = getWritableDatabase();
		}

	}
	
}