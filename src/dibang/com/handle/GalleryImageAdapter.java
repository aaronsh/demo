package dibang.com.handle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.android.camera.DesignCaseDb;

import dibang.com.Const;
import dibang.com.R;
import dibang.com.web.IOFile;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryImageAdapter extends BaseAdapter {
	private static final String TAG = "GalleryImageAdapter";
	int mGalleryItemBackground;
	private boolean WithSdCard = true;
	private HashMap<Integer, ImageView> mViewList = new HashMap<Integer, ImageView>();
	DesignCaseDb mDb = null;
	Cursor mCursor = null;
	int mCursorCount = 0;

	public GalleryImageAdapter(Context c) {
		mContext = c;
		// See res/values/attrs.xml for the <declare-styleable> that defines
		// Gallery1.

		TypedArray a = c.obtainStyledAttributes(R.styleable.Gallery1);
		/*
		 * mGalleryItemBackground = a.getResourceId(
		 * R.styleable.Gallery1_android_galleryItemBackground, 0);
		 */
		mGalleryItemBackground = R.drawable.gallery_item_background;
		a.recycle();

		mDb = new DesignCaseDb(c, DesignCaseDb.TBL_TOP_GALLERY);
		mCursor = mDb.query();
		mCursorCount = mCursor.getCount();
	}

	@Override
	public int getCount() {
		if( mCursorCount > 0 )
			return mCursorCount;
		return mImageIds.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView i = mViewList.get(position);
		if (i == null) {
			i = new ImageView(mContext);
			if( mCursorCount > 0 ){
				mCursor.moveToPosition(mCursorCount - position - 1);
				Log.v(TAG, "position:"+position+",index:"+ mCursor.getPosition() +","+mCursor.getString(DesignCaseDb.COL_INDEX_TEXT));
				String pathName = mCursor.getString(DesignCaseDb.COL_INDEX_PATH);
				Bitmap bmp = BitmapFactory.decodeFile(pathName);
				i.setImageBitmap(bmp);
			} else {
				i.setImageResource(mImageIds[position]);
			}
			i.setScaleType(ImageView.ScaleType.FIT_XY);
			i.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.FILL_PARENT, Gallery.LayoutParams.WRAP_CONTENT));

			// The preferred Gallery item background
			i.setBackgroundResource(mGalleryItemBackground);

			mViewList.put(position, i);

		}
		return i;
	}

	private Context mContext;

	private Integer[] mImageIds = { R.drawable.title, R.drawable.logo	};

	public void reset() {
		Log.v(TAG, "reset gallery");
		mCursor.close();
		mCursor = mDb.query();
		int prevCount = mCursorCount;
		mCursorCount = mCursor.getCount();
		for(int i=0; i<mCursorCount && i<mViewList.size(); i++){
			ImageView v = mViewList.get(i);
			mCursor.moveToPosition(mCursorCount - i - 1 );
			Log.v(TAG, "position:"+i+",index:"+ mCursor.getPosition() +","+mCursor.getString(DesignCaseDb.COL_INDEX_TEXT));
			String pathName = mCursor.getString(DesignCaseDb.COL_INDEX_PATH);
			Log.v(TAG, "reset:"+pathName);
			Bitmap bmp = BitmapFactory.decodeFile(pathName);
			v.setImageBitmap(bmp);
			v.invalidate();
		}
		if(mCursorCount != prevCount){
			notifyDataSetChanged();
		}
	}

	public void close(){
		mCursor.close();
		mDb.close();
	}
}
