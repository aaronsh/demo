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

public class DesignCaseDb extends SQLiteOpenHelper {
	public static final int COL_INDEX_ID = 0;
	public static final int COL_INDEX_CLASS = 1;
	public static final int COL_INDEX_PATH = 2;
	public static final int COL_INDEX_LINK = 3;
	public static final int COL_INDEX_TEXT = 4;
	
	public static final String TBL_WEB_CASES = "web_cases";
	public static final String TBL_ANI_CASES = "ani_cases";
	public static final String TBL_EBOOK_CASES = "ebook_cases";
	public static final String TBL_PARTNER = "partner";
	public static final String TBL_TOP_GALLERY = "top_gallery";
	public static final String TBL_EFFECT_SHOW = "effect_show";
	public static final String TBL_HOUSE_SHOW = "house_show";
	
	private static final String[] TABLES = {TBL_WEB_CASES, TBL_ANI_CASES, TBL_EBOOK_CASES, TBL_PARTNER, TBL_TOP_GALLERY, TBL_EFFECT_SHOW, TBL_HOUSE_SHOW};
	
	protected static final int DATABASE_VERSION = 5;
	private static final String DB_NAME = "designcases.db";


	private static final String Key_id = "_id";
	private static final String Key_class = "_class";
	private static final String Key_path = "_path";
	private static final String Key_link = "_link";
	private static final String Key_name = "_name";
	private static final String TAG = "DesignCaseDb";

	private String TBL_NAME = null;

	private SQLiteDatabase mDB=null;

	public DesignCaseDb(Context c, String tbl) {
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
		StringBuilder cols = new StringBuilder("(");
		cols.append(Key_id);
		cols.append(" integer primary key autoincrement, ");
		cols.append(Key_class);
		cols.append(" text, ");
		cols.append(Key_path);
		cols.append(" text, ");
		cols.append(Key_link);
		cols.append(" text, ");
		cols.append(Key_name);
		cols.append(" text)");

		for( String tbl:TABLES ){
			StringBuilder b = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
			b.append(tbl);
			b.append(cols);
			db.execSQL(b.toString());
		}
	}
	
	public void onOpen (SQLiteDatabase db){
		Log.v(TAG, "onOpen");
		super.onOpen(db);
	}

	
	public void insert(String imgClass, String path, String link, String name)
	{
//		Log.v(TAG, "add "+imgClass+","+name+","+path);
		ContentValues values = new ContentValues();

		values.put(Key_class, imgClass);
		values.put(Key_path, path);
		values.put(Key_link, link);
		values.put(Key_name, name);
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
		String sql = "delete from \"" + TBL_NAME + "\"";
		mDB.execSQL(sql);
//		onCreate(mDB);
	}


	public Cursor query() {
		obtainWritableDatabase();
		Cursor c = mDB.query(TBL_NAME, null, null, null, null, null, null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			Log.v(TAG, c.getString(COL_INDEX_PATH));
			c.moveToNext();
		}
		return c;
	}

	public Cursor query(String caseClass) {
		obtainWritableDatabase();
		String[] cols = {Key_id, Key_class, Key_path, Key_link, Key_name};
		StringBuilder b = new StringBuilder(Key_class);
		b.append("=\"");
		b.append(caseClass);
		b.append("\"");
		Cursor c = mDB.query(TBL_NAME, cols, b.toString(), null, null, null, null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			Log.v(TAG, c.getString(COL_INDEX_CLASS));
			c.moveToNext();
		}
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
		for( String tbl:TABLES ){
			String sql = "drop table IF EXISTS " + tbl;
			db.execSQL(sql);
		}
		onCreate(db);
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
