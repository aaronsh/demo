package dibang.com.handle;

import dibang.com.CompanyIntroduce;
import dibang.com.ContactUs;
import dibang.com.DialogActivity;
import dibang.com.MainActivity;
import dibang.com.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public abstract class BaseActivity extends Activity implements OnItemClickListener,
		OnClickListener, WebUpdateNotification {
	protected final static int PAGE_TYPE_HOME = 0;
	protected final static int PAGE_TYPE_ABOUT = 1;
	protected final static int PAGE_TYPE_CONTACT = 2;
	protected final static int PAGE_TYPE_WEIBO = 3;
	protected final static int PAGE_TYPE_REFRESH = 4;
	protected final static int PAGE_TYPE_BACK = 5;

	private static final String TAG = "BaseActivity";
	protected BaseHandler mBaseHandler = null;
	protected BottomMenuHandler mBottomMenu = null;
	protected TopMenuHandler mTopMenu = null;
	protected ServerManager mSM = null;
	private final static int[] ButtonIds = { R.id.bottom_menu_home,
			R.id.bottom_menu_company, R.id.bottom_menu_contact_us,
			R.id.bottom_menu_weibo, R.id.bottom_menu_refresh,
			R.id.bottom_menu_back };

	private int mPageType;
	private int mUpdateEvent=0xFF;

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);

		mSM = ServerManager.getInstance(this);
		Log.v(TAG, "create " + this.getClass().toString());
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	public void registUpdateEvent(int event)
	{
		mUpdateEvent = event;
		if( event != 0xFF ){
			mSM.registerEvent(mUpdateEvent, this);
		}
	}

	public void onInitView(int type) {
		// Reference the Gallery view
		Gallery g = (Gallery) findViewById(R.id.gallery);
		if (g != null) {
			// Set the adapter to our custom adapter (below)
			g.setAdapter(new GalleryImageAdapter(this));

			// Set a item click listener, and just Toast the clicked position
			g.setOnItemClickListener(this);
		}

		// set bottom menu
		for (int index : ButtonIds) {
			ImageView btn = (ImageView) findViewById(index);
			if (btn != null)
				btn.setOnClickListener(this);
		}
		setPageType(type);
	}

	public void setPageType(int type) {
		mPageType = type;
		int count = ButtonIds.length;
		for (int index = 0; index < count; index++) {
			ImageView btn = (ImageView) findViewById(ButtonIds[index]);
			if (btn != null) {
				if (index == type) {
					btn.setBackgroundResource(R.drawable.bottom_btn_bg);
				} else {
					btn.setBackgroundColor(Color.TRANSPARENT);
				}
			}
		}
	}

	public void enableBackBtn() {
		ImageView btn = (ImageView) findViewById(R.id.bottom_menu_back);
		if (btn != null)
			btn.setVisibility(View.VISIBLE);

		btn = (ImageView) findViewById(R.id.bottom_menu_refresh);
		if (btn != null)
			btn.setVisibility(View.GONE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
		return super.onOptionsItemSelected(paramMenuItem);
	}

	public void onPause() {
		super.onPause();
	}

	public void onResume() {
		super.onResume();
		// MobclickAgent.onResume(this);
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show();
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		boolean wantResult = false;
		switch (v.getId()) {
		case R.id.bottom_menu_home:
			intent = new Intent(this, MainActivity.class);
			break;
		case R.id.bottom_menu_company:
			intent = new Intent(this, CompanyIntroduce.class);
			break;
		case R.id.bottom_menu_contact_us:
			intent = new Intent(this, ContactUs.class);
			break;
		case R.id.bottom_menu_weibo:
			break;
		case R.id.bottom_menu_refresh:
			if( mUpdateEvent != 0xFF ){
			intent = new Intent(this, DialogActivity.class);
			intent.putExtra(DialogActivity.KEY_DIALOG_TYPE, DialogActivity.DIALOG_TYPE_UPDATING);
			intent.putExtra(DialogActivity.KEY_WATCH_EVENT, mUpdateEvent);
			wantResult = true;
			}
			break;
		case R.id.bottom_menu_back:
			finish();
			break;
		default:
			break;
		}

		if (intent != null) {
			if( wantResult == true){
				startActivityForResult(intent, DialogActivity.ACTION_WAITING_FOR_UPDATING);
			}
			else{
				intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
			}
		}
	}

	public void onTopGalleryUpdated() {
		Gallery g = (Gallery) findViewById(R.id.gallery);
		if (g != null) {
			GalleryImageAdapter adapter = (GalleryImageAdapter) g.getAdapter();
			adapter.reset();
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onActivityResult");
		if (requestCode == DialogActivity.ACTION_WAITING_FOR_UPDATING) {
			// handle the reqeust from pedometer
			onSyncFinished();
			if( mUpdateEvent != 0xFF ){
				mSM.registerEvent(mUpdateEvent, this);
			}
		}
	}
	
	protected abstract void onSyncFinished();

	@Override
	public void onWebUpdateFinish(int UpdateType) {
		// TODO Auto-generated method stub
		onSyncFinished();
	}	
}
