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


public class BaseActivity extends Activity implements OnItemClickListener, OnClickListener
{
	private static final String TAG = "BaseActivity";
	protected BaseHandler mBaseHandler = null;
	protected BottomMenuHandler mBottomMenu = null;
	protected TopMenuHandler mTopMenu = null;
	ImageButton mHomeBtn;
	ImageButton mCompanyBtn;
	ImageButton mContactUsBtn;
	ImageButton mWeiboBtn;
	ImageButton mRefreshBtn;
	ImageButton mBackBtn;

	protected void onCreate(Bundle paramBundle)
	{
		super.onCreate(paramBundle);


		Log.v(TAG, "create "+this.getClass().toString());
	}
	public void onInitView()
	{
		// Reference the Gallery view
		Gallery g = (Gallery) findViewById(R.id.gallery);
		if( g != null ){
			// Set the adapter to our custom adapter (below)
			g.setAdapter(new GalleryImageAdapter(this));

			// Set a item click listener, and just Toast the clicked position
			g.setOnItemClickListener(this);
		}
		
		//set bottom menu
		mHomeBtn = (ImageButton) findViewById(R.id.bottom_menu_home); 
		mCompanyBtn = (ImageButton) findViewById(R.id.bottom_menu_company); 
		mContactUsBtn = (ImageButton) findViewById(R.id.bottom_menu_contact_us); 
		mWeiboBtn = (ImageButton) findViewById(R.id.bottom_menu_weibo); 
		mRefreshBtn = (ImageButton) findViewById(R.id.bottom_menu_refresh); 
		
		if(mHomeBtn != null)
			mHomeBtn.setOnClickListener(this);
		if(mCompanyBtn != null)
			mCompanyBtn.setOnClickListener(this);
		if(mContactUsBtn != null)
			mContactUsBtn.setOnClickListener(this);
		if(mWeiboBtn != null)
			mWeiboBtn.setOnClickListener(this);
		if(mRefreshBtn != null)
			mRefreshBtn.setOnClickListener(this);		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem paramMenuItem)
	{
		return super.onOptionsItemSelected(paramMenuItem);
	}

	public void onPause()
	{
		super.onPause();
		//		MobclickAgent.onPause(this);
	}

	public void onResume()
	{
		super.onResume();
		//		MobclickAgent.onResume(this);
	}
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show();		
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=null;
		if(mHomeBtn == v ){
			intent = new Intent(this, MainActivity.class);
		}
		if(mCompanyBtn  == v ){
			intent = new Intent(this, CompanyIntroduce.class);
		}
		if(mContactUsBtn  == v ){
			intent = new Intent(this, ContactUs.class);
		}
		if(mWeiboBtn  == v ){
			
		}
		if(mRefreshBtn  == v ){
			
		}
		
		if( intent != null )
			intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
	}
}

