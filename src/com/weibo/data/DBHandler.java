package com.weibo.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import weibo4android.Count;
import weibo4android.Status;
import weibo4android.WeiboException;
import weibo4android.org.json.JSONException;
import weibo4android.org.json.JSONObject;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBHandler {
	private Context mContext;
	private List<Status> dataStatus;
	private HashMap<Long, Count> counts;

	public DBHandler(Context context) {
		mContext = context;
	}

	/**
	 * 获取数据库内所有微博Status
	 * 
	 * @return
	 */
	public List<Status> getStatus() {
		dataStatus = new ArrayList<Status>();
		SQLHelper dbHelper = new SQLHelper(mContext);
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		Cursor cursor = database.query(DB.HOME_TABLE, null, null, null, null,
				null, null);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
		while (cursor.moveToNext()) {
			String id = cursor.getString(cursor.getColumnIndexOrThrow(DB.ID));
			String content = cursor.getString(cursor.getColumnIndexOrThrow(DB.CONTENT));
			String pic = cursor.getString(cursor.getColumnIndexOrThrow(DB.PIC));
			Long create_at = cursor.getLong(cursor.getColumnIndexOrThrow(DB.CREATE_TIME));
			String source = cursor.getString(cursor.getColumnIndexOrThrow(DB.SOURCE));

			String uid = cursor.getString(cursor.getColumnIndexOrThrow(DB.UID));
			String nick = cursor.getString(cursor.getColumnIndexOrThrow(DB.NICK));
			String portrait = cursor.getString(cursor.getColumnIndexOrThrow(DB.PORTRAIT));
			String vip = cursor.getString(cursor.getColumnIndexOrThrow(DB.VIP));
			// 暂时取消地理信息
			// String longitude = cursor.getString(cursor
			// .getColumnIndexOrThrow(Sina.LONGITUDE));
			// String latitude = cursor.getString(cursor
			// .getColumnIndexOrThrow(Sina.LATITUDE));

			String rtID = cursor.getString(cursor
					.getColumnIndexOrThrow(DB.RTID));
			String rtContent = cursor.getString(cursor
					.getColumnIndexOrThrow(DB.RTCONTENT));
			String rtPic = cursor.getString(cursor
					.getColumnIndexOrThrow(DB.RTPIC));

			String rtUid = cursor.getString(cursor
					.getColumnIndexOrThrow(DB.RTUID));
			String rtNick = cursor.getString(cursor
					.getColumnIndexOrThrow(DB.RTNICK));

			// 构建微博Status
			JSONObject js = new JSONObject();
			JSONObject user = new JSONObject();
			JSONObject rt = new JSONObject();
			JSONObject rtUser = new JSONObject();

			try {
				if (rtContent == null || "".equals(rtContent)) {

				} else {
					rtUser.put("id", Long.parseLong(rtUid));
					rtUser.put("screen_name", rtNick);

					rt.put("id", Long.parseLong(rtID));
					rt.put("text", rtContent);
					rt.put("thumbnail_pic", rtPic);
					rt.put("user", rtUser);

					js.put("retweeted_status", rt);
				}
				user.put("id", Long.parseLong(uid));
				user.put("screen_name", nick);
				user.put("profile_image_url", portrait);
				user.put("verified", vip);
				js.put("user", user);

				js.put("id", Long.parseLong(id));
				js.put("text", content);
				js.put("thumbnail_pic", pic);
				js.put("created_at", dateFormat.format(new Date(create_at)));
				js.put("source", source);

				dataStatus.add(new Status(js));
			} catch (JSONException e) {
				e.printStackTrace();
				log(e.toString());
			} catch (WeiboException e) {
				e.printStackTrace();
				log(e.toString());
			}
		}
		cursor.close();
		database.close();
		dbHelper.close();
		return this.dataStatus;
	}

	/**
	 * 获取数据库内所有微博的转发数以及评论数
	 * 
	 * @return HashMap 以微博id为键，对应的微博Count为值
	 */
	public HashMap<Long, Count> getCounts() {
		counts = new HashMap<Long, Count>();
		SQLHelper dbHelper = new SQLHelper(mContext);
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		Cursor cursor = database.query(DB.HOME_TABLE, null, null, null, null,
				null, null);
		while (cursor.moveToNext()) {
			String id = cursor.getString(cursor.getColumnIndexOrThrow(DB.ID));
			String commentNum = cursor.getString(cursor.getColumnIndexOrThrow(DB.COMMENTNUM));
			String redirectNum = cursor.getString(cursor.getColumnIndexOrThrow(DB.RTNUM));

			// 构建对应的Count，并加入到HshMap中
			try {
				JSONObject countJ = new JSONObject();
				countJ.put("id", Long.parseLong(id));
				countJ.put("comments", Integer.parseInt(commentNum));
				countJ.put("rt", Integer.parseInt(redirectNum));
				Count count = new Count(countJ);
				counts.put(Long.parseLong(id), count);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				log(e.toString());
			} catch (JSONException e) {
				e.printStackTrace();
				log(e.toString());
			} catch (WeiboException e) {
				e.printStackTrace();
				log(e.toString());
			}
		}
		cursor.close();
		database.close();
		dbHelper.close();
		return counts;
	}

	/**
	 * 保存微博及其转发数&评论数
	 * 
	 * @param status
	 * @param count
	 */
	public void save(Status status, Count count) {
		long id = status.getId();
		String content = status.getText();
		String pic = status.getThumbnail_pic();
		String create_at = String.valueOf((status.getCreatedAt().getTime()));
		String source = status.getSource();
		long uid = status.getUser().getId();
		String nick = status.getUser().getScreenName();
		String portrait = status.getUser().getProfileImageURL().toString();
		boolean verified = status.getUser().isVerified();
		long redirectNum = count.getRt();
		long commentNum = count.getComments();

		if (status.getRetweeted_status() == null) {
			save(id, content, pic, create_at, source, uid, nick, portrait,
					verified, redirectNum, commentNum);
		} else { // 若存在转发微博，一起存入数据库中
			long rtId = status.getRetweeted_status().getId();
			String rtContent = status.getRetweeted_status().getText();
			String rtPic = status.getRetweeted_status().getThumbnail_pic();
			long rtUid = status.getRetweeted_status().getUser().getId();
			String rtNick = status.getRetweeted_status().getUser()
					.getScreenName();
			save(id, content, pic, create_at, source, uid, nick, portrait,
					verified, redirectNum, commentNum, rtId, rtContent, rtPic,
					rtUid, rtNick);
		}
	}

	/**
	 * 保存微博
	 * 
	 * @param id
	 * @param content
	 * @param pic
	 * @param create_at
	 * @param source
	 * @param uid
	 * @param nick
	 * @param portrait
	 * @param verified
	 * @param redirectNum
	 * @param commentNum
	 */
	private void save(long id, String content, String pic, String create_at,
			String source, long uid, String nick, String portrait,
			boolean verified, long redirectNum, long commentNum) {
		ContentValues values = new ContentValues();
		values.put(DB.ID, id);
		values.put(DB.CONTENT, content);
		values.put(DB.PIC, pic);
		values.put(DB.CREATE_TIME, create_at);
		values.put(DB.SOURCE, source);
		values.put(DB.UID, uid);
		values.put(DB.NICK, nick);
		values.put(DB.PORTRAIT, portrait);
		values.put(DB.VIP, String.valueOf(verified));
		values.put(DB.RTNUM, redirectNum);
		values.put(DB.COMMENTNUM, commentNum);

		SQLHelper dbHelper = new SQLHelper(mContext);
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.insertOrThrow(DB.HOME_TABLE, null, values);
		database.close();
		dbHelper.close();
	}

	/**
	 * 保存微博
	 * 
	 * @param id
	 * @param content
	 * @param pic
	 * @param create_at
	 * @param source
	 * @param uid
	 * @param nick
	 * @param portrait
	 * @param verified
	 * @param redirectNum
	 * @param commentNum
	 * @param rtId
	 * @param rtContent
	 * @param rtPic
	 * @param rtUid
	 * @param rtNick
	 */
	private void save(long id, String content, String pic, String create_at,
			String source, long uid, String nick, String portrait,
			boolean verified, long redirectNum, long commentNum, long rtId,
			String rtContent, String rtPic, long rtUid, String rtNick) {
		ContentValues values = new ContentValues();
		values.put(DB.ID, id);
		values.put(DB.CONTENT, content);
		values.put(DB.PIC, pic);
		values.put(DB.CREATE_TIME, create_at);
		values.put(DB.SOURCE, source);
		values.put(DB.UID, uid);
		values.put(DB.NICK, nick);
		values.put(DB.PORTRAIT, portrait);
		values.put(DB.VIP, String.valueOf(verified));
		values.put(DB.RTNUM, redirectNum);
		values.put(DB.COMMENTNUM, commentNum);
		values.put(DB.RTID, rtId);
		values.put(DB.RTCONTENT, rtContent);
		values.put(DB.RTPIC, rtPic);
		values.put(DB.RTUID, rtUid);
		values.put(DB.RTNICK, rtNick);

		SQLHelper dbHelper = new SQLHelper(mContext);
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.insertOrThrow(DB.HOME_TABLE, null, values);
		database.close();
		dbHelper.close();
	}

	/**
	 * 清除指定表内所有数据
	 * 
	 * @param tableName
	 */
	public void clearTable(String tableName) {
		SQLHelper sqlHelper = new SQLHelper(mContext);
		SQLiteDatabase database = sqlHelper.getWritableDatabase();
		database.delete(tableName, null, null);
		database.close();
	}

	void log(String msg) {
		Log.i("weibo", "DBOperate--" + msg);
	}

}
