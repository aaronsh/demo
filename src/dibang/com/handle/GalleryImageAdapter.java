package dibang.com.handle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import dibang.com.Const;
import dibang.com.R;
import dibang.com.web.IOFile;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;




public class GalleryImageAdapter extends BaseAdapter {
	int mGalleryItemBackground;
	private boolean WithSdCard = true;
	private ArrayList<String> mFileList = null;
	private HashMap<Integer, ImageView > mViewList = new HashMap<Integer, ImageView >(); 

	public GalleryImageAdapter(Context c) {
		mContext = c;
		// See res/values/attrs.xml for the <declare-styleable> that defines
		// Gallery1.

		TypedArray a = c.obtainStyledAttributes(R.styleable.Gallery1);
/*
		mGalleryItemBackground = a.getResourceId(
				R.styleable.Gallery1_android_galleryItemBackground, 0);
*/		
		mGalleryItemBackground = R.drawable.gallery_item_background;
		a.recycle();
	
		WithSdCard = IOFile.sdcardExist();
		if( WithSdCard ){
		String path = IOFile.getModuleFolder(Const.FOLDER_top_gallery);
		mFileList = IOFile.getFileList(path);
		}
	}

	public int getCount() {
		if( WithSdCard )
			return mFileList.size();
		return mImageIds.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView i = mViewList.get(position);
		if( i == null ){
				i = new ImageView(mContext);

	    if( WithSdCard ){
	    	Bitmap bmp = BitmapFactory.decodeFile(mFileList.get(position));
	    	i.setImageBitmap(bmp);
	    }
	    else{
		i.setImageResource(mImageIds[position]);
	    }
		i.setScaleType(ImageView.ScaleType.FIT_XY);
		i.setLayoutParams(new Gallery.LayoutParams(-1, 88));

		// The preferred Gallery item background
		i.setBackgroundResource(mGalleryItemBackground);
		
		mViewList.put(position, i);
		
		}
		return i;
	}

	private Context mContext;

	private Integer[] mImageIds = {
			R.drawable.title,
			R.drawable.logo
			/*			
			R.drawable.gallery_photo_2,
			R.drawable.gallery_photo_3,
			R.drawable.gallery_photo_4,
			R.drawable.gallery_photo_5,
			R.drawable.gallery_photo_6,
			R.drawable.gallery_photo_7,
			R.drawable.gallery_photo_8
*/			
	};

	public void reset()
		{
		String path = IOFile.getModuleFolder(Const.FOLDER_top_gallery);
		mFileList = IOFile.getFileList(path);
		Set<Integer> keys = mViewList.keySet();
		for(Integer key:keys )
		{
			ImageView v = mViewList.get(key);
			Bitmap bmp = BitmapFactory.decodeFile(mFileList.get(key));
			v.setImageBitmap(bmp);
			v.invalidate();
		}
		notifyDataSetChanged();
		}
}

