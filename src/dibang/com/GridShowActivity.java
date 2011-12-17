package dibang.com;



import com.android.camera.DesignCaseDb;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import dibang.com.handle.BaseActivity;
import dibang.com.handle.WebUpdateNotification;
import dibang.com.handle.WebUpdateService;

public class GridShowActivity extends BaseActivity implements OnClickListener, OnItemClickListener  {

	private static final String TAG = "GridShowActivity";
	GridAdapter mAdapter = null;
	GridView mGrid = null;
	DesignCaseDb mDb = null;
	Cursor mCursor = null;
	int mUpdateEvent;
	private String mFilter = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_grid);
        mGrid = (GridView) findViewById(R.id.grid);
        
		int type = this.getIntent().getIntExtra("type", Const.UI_TYPE_WEBSITE_DESIGN);
		TextView text = (TextView)this.findViewById(R.id.text_title);
		text.setTextColor(Color.rgb(12, 74, 128));
		mUpdateEvent = 0xFF;
		switch(type){
		case  Const.UI_TYPE_WEBSITE_DESIGN:
			text.setText("网站设计");
			mDb = new DesignCaseDb(this, DesignCaseDb.TBL_WEB_CASES);
			mFilter = Const.WEB_PAGE_CLASS_ZUIXIN;
			mCursor = mDb.query(mFilter);
			Log.v(TAG, "get "+mCursor.getCount() + " from db");
			mAdapter = new GridAdapter(this, GridAdapter.ITEM_TYPE_IMAGE_TEXT, mCursor);
			mUpdateEvent = WebUpdateService.UPDATE_TASK_WEB_DESIGN;
			break;
		case  Const.UI_TYPE_3D_ANIMATION:
			text.setText("三维动画");
			mDb = new DesignCaseDb(this, DesignCaseDb.TBL_ANI_CASES);
			mCursor = mDb.query();
			mAdapter = new GridAdapter(this, GridAdapter.ITEM_TYPE_IMAGE_TEXT, mCursor);
			mUpdateEvent = WebUpdateService.UPDATE_TASK_ANI_DESIGN;
			break;
		case  Const.UI_TYPE_EFFECT_SHOW:
			text.setText("效果图");
			break;
		case  Const.UI_TYPE_HOUSE_SHOW:
			text.setText("户型图");
			break;
		case  Const.UI_TYPE_EMAGZIN:
			text.setText("电子杂志");
			mDb = new DesignCaseDb(this, DesignCaseDb.TBL_EBOOK_CASES);
			mCursor = mDb.query();
			mAdapter = new GridAdapter(this, GridAdapter.ITEM_TYPE_IMAGE_TEXT, mCursor);
			mUpdateEvent = WebUpdateService.UPDATE_TASK_EBOOK_DESIGN;
			break;
		case  Const.UI_TYPE_PARTNER:
			text.setText("合作伙伴");
			mDb = new DesignCaseDb(this, DesignCaseDb.TBL_PARTNER);
			mCursor = mDb.query();
			mAdapter = new GridAdapter(this, GridAdapter.ITEM_TYPE_IMAGE_ONLY, mCursor);
//			mGrid.setNumColumns(3);
			mSM.registerEvent(WebUpdateService.UPDATE_TASK_PARTNER, this);
			mUpdateEvent = WebUpdateService.UPDATE_TASK_PARTNER;
			break;
		}

		boolean showTopMenu = this.getIntent().getBooleanExtra("top_menu", true);
		if( !showTopMenu){
			Resources res = getResources();

			LinearLayout group = (LinearLayout)this.findViewById(R.id.linearLayout1);
			group.removeView(group.findViewById(R.id.linearLayout4));
			group.removeView(group.findViewById(R.id.top_menu));
			group.setBackgroundDrawable(res.getDrawable(R.drawable.top_menu_bg));
		}
		else{
			registTopButtonEvent();
		}
	
		

        mGrid.setAdapter(mAdapter);
        mGrid.setOnItemClickListener(this);
       
        
		onInitView(BaseActivity.PAGE_TYPE_HOME);
		registUpdateEvent(mUpdateEvent);
	}
	
	private void registTopButtonEvent()
	{
		int[] ids = {R.id.top_btn_zuixin, R.id.top_btn_bieshu, R.id.top_btn_gongyu, R.id.top_btn_shangye, R.id.top_btn_zonghe, R.id.top_btn_qiye};
		for( int id:ids){
			TextView v = (TextView)findViewById(id);
			v.setClickable(true);
			v.setOnClickListener(this);
		}
	}
		
	@Override
	public void onDestroy() {
		if( mCursor != null )
			mCursor.close();
		if( mDb != null ){
			mDb.close();
		}
		super.onDestroy();
	}
	

	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;

		switch (v.getId()) {
		case R.id.top_btn_zuixin:
			mFilter = Const.WEB_PAGE_CLASS_ZUIXIN;
			break;
		case R.id.top_btn_bieshu:
			mFilter = Const.WEB_PAGE_CLASS_BIESHU;
			break;
		case R.id.top_btn_gongyu:
			mFilter = Const.WEB_PAGE_CLASS_GONGYU;
			break;
		case R.id.top_btn_shangye:
			mFilter = Const.WEB_PAGE_CLASS_SHANGYE;
			break;
		case R.id.top_btn_zonghe:
			mFilter = Const.WEB_PAGE_CLASS_ZONGHE;
			break;
		case R.id.top_btn_qiye:
			mFilter = Const.WEB_PAGE_CLASS_QIYE;
			break;
		default:
			break;
		}

		if (mFilter != null) {
			if( mCursor != null ){
				mCursor.close();
			}
			mCursor = mDb.query(mFilter);
			Log.v(TAG, "get "+mCursor.getCount() + " from db");
			mAdapter = new GridAdapter(this, GridAdapter.ITEM_TYPE_IMAGE_TEXT, mCursor);
			 mGrid.setAdapter(mAdapter);
		}
		super.onClick(v);
	}
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onItemClick:"+arg1.getTag());
		String link = (String)arg1.getTag();
		if( link != null && link.length() > 0 && link.startsWith("http://") ){
			Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
			it.setClassName("com.Android.browser", "com.android.browser.BrowserActivity");
			startActivity(it);
		}


		Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onSyncFinished(int UpdateType) {
		// TODO Auto-generated method stub
		if( mUpdateEvent == UpdateType ){
			mCursor.close();
			if( mFilter != null && mFilter.length()>0 ){
				mCursor = mDb.query(mFilter);
			}
			else{
				mCursor = mDb.query();
			}
			mAdapter.reset(mCursor);
		}
	}
}
