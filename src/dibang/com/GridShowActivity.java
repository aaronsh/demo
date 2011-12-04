package dibang.com;



import com.android.camera.DesignCaseDb;

import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import dibang.com.handle.BaseActivity;
import dibang.com.handle.WebUpdateNotification;
import dibang.com.handle.WebUpdateService;

public class GridShowActivity extends BaseActivity implements WebUpdateNotification {

	GridAdapter mAdapter = null;
	GridView mGrid = null;
	DesignCaseDb mDb = null;
	Cursor mCursor = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_grid);
        mGrid = (GridView) findViewById(R.id.grid);
        
		int type = this.getIntent().getIntExtra("type", Const.UI_TYPE_WEBSITE_DESIGN);
		TextView text = (TextView)this.findViewById(R.id.text_title);
		switch(type){
		case  Const.UI_TYPE_WEBSITE_DESIGN:
			text.setText("网站设计");
			mDb = new DesignCaseDb(this, DesignCaseDb.TBL_WEB_CASES);
			mCursor = mDb.query();
			mAdapter = new GridAdapter(this, GridAdapter.ITEM_TYPE_IMAGE_TEXT, mCursor);
			break;
		case  Const.UI_TYPE_3D_ANIMATION:
			text.setText("三维动画");
			mDb = new DesignCaseDb(this, DesignCaseDb.TBL_ANI_CASES);
			mCursor = mDb.query();
			mAdapter = new GridAdapter(this, GridAdapter.ITEM_TYPE_IMAGE_TEXT, mCursor);
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
			break;
		case  Const.UI_TYPE_PARTNER:
			text.setText("合作伙伴");
			mAdapter = new GridAdapter(this, GridAdapter.ITEM_TYPE_IMAGE_ONLY, null);
			mGrid.setNumColumns(3);
			mSM.bindService(WebUpdateService.UPDATE_TASK_PARTNER, this);
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
	
		

        mGrid.setAdapter(mAdapter);
       
        
		onInitView();
	}
		
	@Override
	public void onDestroy() {
		if( mCursor != null )
			mCursor.close();
		if( mDb != null ){
			mDb.close();
		}
		mSM.unbindService();
		super.onDestroy();
	}
	
	@Override
	public void onWebUpdateFinish(int UpdateType) {
		// TODO Auto-generated method stub
		if( UpdateType == WebUpdateService.UPDATE_TASK_PARTNER ){
			mAdapter.reset();
		}
	}

}
