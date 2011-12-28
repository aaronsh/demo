package dibang.com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.android.camera.DesignCaseDb;

import dibang.com.web.IOFile;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {
	public final static int ITEM_TYPE_IMAGE_ONLY = 1;
	public final static int ITEM_TYPE_IMAGE_TEXT = 2;
	private static final String TAG = "GridAdapter";

	Context mCntx;
	private int mScreenWidth = 0;
	private int mScreenHeight = 0;
	int mItemType;
	LayoutInflater mInflater;
	Cursor mCursor = null;
	int mCursorCount = 0;

	private boolean WithSdCard = true;
	private ArrayList<String> mFileList = null;
	private HashMap<Integer, View> mViewList = new HashMap<Integer, View>();

	public GridAdapter(Context cntx, int itemType, Cursor cursor) {
		mCntx = cntx;
		mCursor = cursor;
		mCursorCount = mCursor.getCount();

		DisplayMetrics dm = new DisplayMetrics();
		dm = mCntx.getApplicationContext().getResources().getDisplayMetrics();
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;

		mInflater = LayoutInflater.from(mCntx);
		mItemType = itemType;

		WithSdCard = IOFile.sdcardExist();
		if (WithSdCard) {
			String path = IOFile.getModuleFolder(Const.FOLDER_partner);
			mFileList = IOFile.getFilePathList(path);
		}
	}

	private View createImageTextView(int position) {
		ImageView i = null;
		String title = "∫Î—Ù°£–Ò»’∞Æ…œ≥«";

		LinearLayout frm = (LinearLayout) mInflater.inflate(
				R.layout.grid_item_imgtxt_view, null);

		// new LinearLayout(mCntx);
		frm.setOrientation(LinearLayout.VERTICAL);
		i = new ImageView(mCntx);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new GridView.LayoutParams(mScreenWidth / 2,
				ViewGroup.LayoutParams.FILL_PARENT));
		int width = 0;
		int height = 0;
		if (mCursor != null) {
//			mCursor.moveToPosition(mCursorCount - position - 1);
			mCursor.moveToPosition( position );
			Log.v(TAG,
					"position:" + position + ",index:" + mCursor.getPosition()
							+ ","
							+ mCursor.getString(DesignCaseDb.COL_INDEX_TEXT));
			String pathName = mCursor.getString(DesignCaseDb.COL_INDEX_PATH);
			Log.v(TAG, "pathName:" + pathName);
			Bitmap bmp = BitmapFactory.decodeFile(pathName);
			if (bmp != null) {
				i.setImageBitmap(bmp);
				width = mScreenWidth / 2 - 24;
				height = (bmp.getHeight() * width) / bmp.getWidth();
			} else {
				Drawable icon = mCntx.getResources().getDrawable(

				R.drawable.grid_imgtxt_item);// info.activityInfo.loadIcon(mCntx.getPackageManager());

				i.setImageDrawable(icon);

				width = mScreenWidth / 2 - 24;
				height = (icon.getIntrinsicHeight() * width)
						/ icon.getIntrinsicWidth();

			}

			title = mCursor.getString(DesignCaseDb.COL_INDEX_TEXT);
			frm.setTag(mCursor.getString(DesignCaseDb.COL_INDEX_LINK));
		} else {
			Drawable icon = mCntx.getResources().getDrawable(

			R.drawable.grid_imgtxt_item);// info.activityInfo.loadIcon(mCntx.getPackageManager());

			i.setImageDrawable(icon);

			width = mScreenWidth / 2 - 24;
			height = (icon.getIntrinsicHeight() * width)
					/ icon.getIntrinsicWidth();
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
				height);
		params.setMargins(3, 3, 3, 3);
		params.gravity = Gravity.CENTER;
		i.setLayoutParams(params);
		LinearLayout container = (LinearLayout) frm
				.findViewById(R.id.icon_container);
		container.addView(i, 0);

		TextView titleView = (TextView) frm.findViewById(R.id.text);
		titleView.setText(title);
		titleView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
		// frm.addView(title);
		return frm;

	}

	private View buildImageView(int position) {
		ImageView i = null;

		LinearLayout frm = (LinearLayout) mInflater.inflate(
				R.layout.grid_item_imgtxt_view, null);

		// new LinearLayout(mCntx);
		frm.setOrientation(LinearLayout.VERTICAL);
		i = new ImageView(mCntx);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new GridView.LayoutParams(mScreenWidth / 2,
				ViewGroup.LayoutParams.FILL_PARENT));
		int width = 0;
		int height = 0;
		if (mCursor != null) {
			mCursor.moveToPosition(position);
			// mCursor.moveToNext();
			Log.v(TAG,
					"position:" + position + ",index:" + mCursor.getPosition()
							+ ","
							+ mCursor.getString(DesignCaseDb.COL_INDEX_PATH));
			String pathName = mCursor.getString(DesignCaseDb.COL_INDEX_PATH);
			Bitmap bmp = BitmapFactory.decodeFile(pathName);
			i.setImageBitmap(bmp);
			width = mScreenWidth / 2 - 24;
			height = (bmp.getHeight() * width) / bmp.getWidth();
		} else {
			Drawable icon = mCntx.getResources().getDrawable(

			R.drawable.grid_imgtxt_item);// info.activityInfo.loadIcon(mCntx.getPackageManager());

			i.setImageDrawable(icon);

			width = mScreenWidth / 2 - 24;
			height = (icon.getIntrinsicHeight() * width)
					/ icon.getIntrinsicWidth();
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
				height);
		params.setMargins(3, 3, 3, 3);
		params.gravity = Gravity.CENTER;
		i.setLayoutParams(params);
		LinearLayout container = (LinearLayout) frm
				.findViewById(R.id.icon_container);
		container.addView(i, 0);

		TextView titleView = (TextView) frm.findViewById(R.id.text);
		frm.removeView(titleView);
		return frm;

	}

	public View getView(int position, View convertView, ViewGroup parent) {

		Log.v(TAG, "getView(" + position + ")");
		View v = mViewList.get(position);
		if (v == null) {

			if (mItemType == ITEM_TYPE_IMAGE_TEXT) {
				v = createImageTextView(position);
			} else {
				v = buildImageView(position);
			}
			mViewList.put(position, v);
		}

		return v;

		// return i;
	}

	public final int getCount() {
		if (mCursor != null)
			return mCursorCount;
		if (WithSdCard)
			return mFileList.size();
		return 30;
	}

	public final Object getItem(int position) {
		if (WithSdCard)
			return mFileList.get(position);
		return null;
	}

	public final long getItemId(int position) {
		return position;
	}

	public void reset(Cursor cursor) {
		mCursor = cursor;
		mCursorCount = mCursor.getCount();
		mViewList.clear();
		notifyDataSetInvalidated();
		notifyDataSetChanged();
	}

}
