package dibang.com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.camera.ImageManager;
import com.android.camera.ViewImage;
import com.android.camera.gallery.IImage;
import com.android.camera.gallery.IImageList;

import dibang.com.handle.BaseActivity;
import dibang.com.handle.WebUpdateNotification;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

public class MainActivity extends BaseActivity implements OnItemClickListener, WebUpdateNotification {
	private ListView listviews;
	EfficientAdapter mAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		InitView();
		
		mSM.startService();
		mSM.bindService(0, this);
		mSM.updateAll();
	}
	
	@Override
	public void onDestroy() {
		mSM.unbindService();
		mSM.stopService();
		super.onDestroy();
	}
	

	/***
	 * 初始化布局属性
	 */
	private void InitView() {
		listviews = (ListView) findViewById(R.id.listview);
		SimpleAdapter adapter = new SimpleAdapter(this, getData(),
				R.layout.list_view, new String[] { "img", "username" },
				new int[] { R.id.img, R.id.username });

		mAdapter = new EfficientAdapter(this);
		listviews.setAdapter(mAdapter);
		
		listviews.setOnItemClickListener(this);
		
		super.onInitView();
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
			intent.putExtra("type", GridShowActivity.TYPE_WEBSITE_DESIGN);
			break;
		case 1:
			intent = new Intent(this, GridShowActivity.class);
			intent.putExtra("type", GridShowActivity.TYPE_WEBSITE_DESIGN);
			break;
		case 2:
			intent = new Intent(this, GalleryShowActivity.class);
			intent.putExtra("type", GridShowActivity.TYPE_WEBSITE_DESIGN);
			break;
		case 3:
            intent = new Intent(this, ViewImage.class);
            mParam = allImages(true);
            IImageList mAllImages = ImageManager.makeImageList(getContentResolver(), mParam);
            IImage image = mAllImages.getImageAt(0);
            intent.putExtra(ViewImage.KEY_IMAGE_LIST, mParam);
            intent.setData(image.fullSizeImageUri());
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

	public void onWebUpdateFinish() {
		// TODO Auto-generated method stub
		
	}
}