package dibang.com.handle;

import dibang.com.CompanyIntroduce;
import dibang.com.ContactUs;
import dibang.com.MainActivity;
import dibang.com.R;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BaseActivity extends Activity implements OnItemClickListener,
		OnClickListener {
	private static final String TAG = "BaseActivity";
	protected BaseHandler mBaseHandler = null;
	protected BottomMenuHandler mBottomMenu = null;
	protected TopMenuHandler mTopMenu = null;
	protected ServerManager mSM = null;


	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);

		mSM = new ServerManager(this);
		Log.v(TAG, "create " + this.getClass().toString());
	}

	public void onInitView() {
		// Reference the Gallery view
		Gallery g = (Gallery) findViewById(R.id.gallery);
		if (g != null) {
			// Set the adapter to our custom adapter (below)
			g.setAdapter(new GalleryImageAdapter(this));

			// Set a item click listener, and just Toast the clicked position
			g.setOnItemClickListener(this);
		}

		// set bottom menu
		ImageButton btn = (ImageButton) findViewById(R.id.bottom_menu_home);
		if (btn != null)
			btn.setOnClickListener(this);
		
		btn = (ImageButton) findViewById(R.id.bottom_menu_company);
		if (btn != null)
			btn.setOnClickListener(this);
		
		btn = (ImageButton) findViewById(R.id.bottom_menu_contact_us);
		if (btn != null)
			btn.setOnClickListener(this);
		
		btn = (ImageButton) findViewById(R.id.bottom_menu_weibo);
		if (btn != null)
			btn.setOnClickListener(this);
		
		btn = (ImageButton) findViewById(R.id.bottom_menu_refresh);
		if (btn != null)
			btn.setOnClickListener(this);

		btn = (ImageButton) findViewById(R.id.bottom_menu_back);
		if (btn != null)
			btn.setOnClickListener(this);
	}
	
	public void enableBackBtn(){
		ImageButton btn = (ImageButton) findViewById(R.id.bottom_menu_back);
		if (btn != null)
			btn.setVisibility(View.VISIBLE);
		
		btn = (ImageButton) findViewById(R.id.bottom_menu_refresh);
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
			break;
		case R.id.bottom_menu_back:
			finish();
			break;
		default:
			break;
		}

		if (intent != null) {
			intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
		}
	}

	public void onTopGalleryUpdated()
		{
		Gallery g = (Gallery) findViewById(R.id.gallery);
		if( g != null ){
			GalleryImageAdapter adapter = (GalleryImageAdapter)g.getAdapter();
			adapter.reset();
		}
		}
}
