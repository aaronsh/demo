package dibang.com;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
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

	private List<ResolveInfo> mApps;
	Context mCntx;
	private int mScreenWidth = 0;
	private int mScreenHeight = 0;
	int mItemType;
	LayoutInflater mInflater;

	public GridAdapter(Context cntx, int itemType) {
		mCntx = cntx;
		loadApps();

		DisplayMetrics dm = new DisplayMetrics();
		dm = mCntx.getApplicationContext().getResources().getDisplayMetrics();
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;

		mInflater = LayoutInflater.from(mCntx);
		mItemType = itemType;
	}

	private void loadApps() {
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		mApps = mCntx.getPackageManager().queryIntentActivities(mainIntent, 0);
	}

	private View createImageTextView(int position) {
		ImageView i = null;

		LinearLayout frm = (LinearLayout) mInflater.inflate(
				R.layout.grid_item_imgtxt_view, null);

		// new LinearLayout(mCntx);
		frm.setOrientation(LinearLayout.VERTICAL);
		i = new ImageView(mCntx);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new GridView.LayoutParams(mScreenWidth / 2,
				ViewGroup.LayoutParams.FILL_PARENT));
		Drawable icon = mCntx.getResources().getDrawable(

				R.drawable.grid_imgtxt_item);// info.activityInfo.loadIcon(mCntx.getPackageManager());

		i.setImageDrawable(icon);
		int width = mScreenWidth / 2 - 24;
		int height = (icon.getIntrinsicHeight() * width)
				/ icon.getIntrinsicWidth();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
				height);
		params.setMargins(3, 3, 3, 3);
		params.gravity = Gravity.CENTER;
		i.setLayoutParams(params);
		LinearLayout container = (LinearLayout) frm
				.findViewById(R.id.icon_container);
		container.addView(i, 0);

		TextView title = (TextView) frm.findViewById(R.id.text);
		title.setText("∫Î—Ù°£–Ò»’∞Æ…œ≥«");
		title.setGravity(Gravity.CENTER_HORIZONTAL);
		// frm.addView(title);
		return frm;

	}

	private View buildImageView(int position) {
		ImageView i = null;

		LinearLayout frm = (LinearLayout) mInflater.inflate(
				R.layout.grid_item_img_view, null);
		// new LinearLayout(mCntx);
		frm.setOrientation(LinearLayout.VERTICAL);
		i = new ImageView(mCntx);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);

		LayoutParams param = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,

				ViewGroup.LayoutParams.FILL_PARENT);
		param.setMargins(3, 3, 3, 3);
		param.gravity = Gravity.CENTER;
		i.setLayoutParams(param);
		Drawable icon = mCntx.getResources().getDrawable(
				R.drawable.grid_img_item);// info.activityInfo.loadIcon(mCntx.getPackageManager());
		i.setImageDrawable(icon);

		frm.addView(i, 0);

		return frm;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {

			if (mItemType == ITEM_TYPE_IMAGE_TEXT) {
				return createImageTextView(position);
			} else {
				return buildImageView(position);
			}
		} else {
			return convertView;
		}


		// return i;
	}

	public final int getCount() {
		return mApps.size();
	}

	public final Object getItem(int position) {
		return mApps.get(position);
	}

	public final long getItemId(int position) {
		return position;
	}


}
