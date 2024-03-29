package dibang.com;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.camera.DesignCaseDb;
import com.android.camera.ImageManager;
import com.android.camera.ViewImage;
import com.android.camera.gallery.IImage;
import com.android.camera.gallery.IImageList;

import dibang.com.handle.BaseActivity;
import dibang.com.handle.WebUpdateNotification;
import dibang.com.handle.WebUpdateService;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends BaseActivity implements OnItemClickListener {
	private static final String TAG = "MainActivity";
	private ListView listviews;
	EfficientAdapter mAdapter;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		InitView();
//		enableBackBtn();

		
		mSM.startService();
		mSM.bindService();
		registUpdateEvent(WebUpdateService.UPDATE_TASK_TOP_GALLERY);
		
		CheckVersionActivity.checkVersion(this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mSM.unbindService();
		mSM.stopService();
	}
	
	public void onPause() {
		super.onPause();
	}

	public void onResume() {
		super.onResume();
	}
	
	public void onBackPressed(){
		Intent intent = new Intent(this, DialogActivity.class);
		intent.putExtra(DialogActivity.KEY_DIALOG_TYPE, DialogActivity.DIALOG_TYPE_QUIT_CNFM);
		this.startActivityForResult(intent, DialogActivity.ACTION_GET_USER_SELECTION);

		//super.onBackPressed();
	}

	private void InitView() {
		listviews = (ListView) findViewById(R.id.listview);

		mAdapter = new EfficientAdapter(this);
		listviews.setAdapter(mAdapter);
		
		listviews.setOnItemClickListener(this);
		
		super.onInitView(BaseActivity.PAGE_TYPE_HOME);
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("img", R.drawable.icon);
		map.put("username", "网站设计");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "三维动画");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "效 果 图");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "戶 型 图");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "电 子 杂 知");
		list.add(map);
	
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "合 作 伙 伴");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "联 系 我 们");
		list.add(map);

		list.add(map);
		
		return list;
	}
	  private ImageManager.ImageListParam mParam;
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = null;
		Class<?> clz = null;
		switch(position){
		case 0:
			intent = new Intent(this, GridShowActivity.class);
			intent.putExtra("type", Const.UI_TYPE_WEBSITE_DESIGN);
			break;
		case 1:
			intent = new Intent(this, GridShowActivity.class);
			intent.putExtra("type", Const.UI_TYPE_3D_ANIMATION);
			intent.putExtra("top_menu", false);
			break;
		case 2:
            intent = new Intent(this, ViewImage.class);
			intent.putExtra("type", Const.UI_TYPE_EFFECT_SHOW);
            mParam = allImages(true);
            {
            intent.putExtra(ViewImage.KEY_IMAGE_LIST, mParam);
//            intent.setData(image.fullSizeImageUri());
            }
			break;
		case 3:
            intent = new Intent(this, ViewImage.class);
			intent.putExtra("type", Const.UI_TYPE_HOUSE_SHOW);
            mParam = allImages(true);
            intent.putExtra(ViewImage.KEY_IMAGE_LIST, mParam);
//            intent.setData(image.fullSizeImageUri());
			break;
		case 4:
			intent = new Intent(this, GridShowActivity.class);
			intent.putExtra("type", Const.UI_TYPE_EMAGZIN);
			intent.putExtra("top_menu", false);
			break;
		case 5:
			intent = new Intent(this, GridShowActivity.class);
			intent.putExtra("type", Const.UI_TYPE_PARTNER);
			intent.putExtra("top_menu", false);
			break;
		}
		if( intent != null )
			startActivity(intent);
	}
    // Returns the image list parameter which contains the subset of image/video
    // we want.
    private ImageManager.ImageListParam allImages(boolean storageAvailable) {
        if (!storageAvailable) {
            return ImageManager.getEmptyImageListParam();
        } else {

            return ImageManager.getImageListParam(
                    ImageManager.DataLocation.EXTERNAL,
                    ImageManager.INCLUDE_IMAGES,
                    ImageManager.SORT_DESCENDING,
                    null);
        }
    }



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (requestCode == DialogActivity.ACTION_GET_USER_SELECTION && data != null) {
			// handle the reqeust from pedometer

			int btn = data.getIntExtra(DialogActivity.KEY_CLICKED_BTN, R.id.button_ok);
			if( btn == R.id.button_ok){
				finish();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onSyncFinished(int UpdateType) {
		// TODO Auto-generated method stub
		
		Log.v(TAG, "onSyncFinished");
		if( UpdateType == WebUpdateService.UPDATE_TASK_TOP_GALLERY)
			onTopGalleryUpdated();
	}

	
}
