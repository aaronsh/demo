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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    public GridAdapter(Context cntx, int itemType){
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
    
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView i=null; 

        if (convertView == null) {
        	LinearLayout frm = new LinearLayout(mCntx);
        	frm.setOrientation(LinearLayout.VERTICAL);
            i = new ImageView(mCntx);
            i.setScaleType(ImageView.ScaleType.FIT_CENTER);
            i.setLayoutParams(new GridView.LayoutParams(mScreenWidth/2, ViewGroup.LayoutParams.FILL_PARENT));
            ResolveInfo info = mApps.get(position);
            Drawable icon = info.activityInfo.loadIcon(mCntx.getPackageManager());
            i.setImageDrawable(icon);
            int height = (icon.getIntrinsicHeight()*mScreenWidth)/(2*icon.getIntrinsicWidth());
            i.setLayoutParams(new GridView.LayoutParams(mScreenWidth/2, height));
            frm.addView(i);
            TextView title = new TextView(mCntx);
            title.setText("title");
            title.setGravity(Gravity.CENTER_HORIZONTAL);
            frm.addView(title);
        	return frm;
          
        } else {
            return convertView;
        }

//        return i;
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
