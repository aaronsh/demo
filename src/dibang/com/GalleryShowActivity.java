package dibang.com;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.AdapterView.OnItemClickListener;
import dibang.com.handle.BaseActivity;

public class GalleryShowActivity extends BaseActivity implements OnClickListener {
	HorizontalScrollView mSView;
	ImageButton mBtnPrev;
	ImageButton mBtnNext;
	private static final String TAG="GalleryShowActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_gallery);
		InitView();
	}

	/***
	 */
	private void InitView() {
		mSView = (HorizontalScrollView)findViewById(R.id.horizontalScrollView1);
		
		mSView.setVerticalScrollBarEnabled(false);
		mSView.setHorizontalScrollBarEnabled(false);

		mBtnPrev = (ImageButton)findViewById(R.id.btn_prev);
		mBtnNext = (ImageButton)findViewById(R.id.btn_next);
		mBtnPrev.setOnClickListener(this);
		mBtnNext.setOnClickListener(this);
		onInitView(BaseActivity.PAGE_TYPE_HOME);
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if(mBtnPrev == v ){
			mSView.scrollBy(-40, 0);
		}
		if(mBtnNext  == v ){
			mSView.scrollBy(40, 0);
		}
		Log.v(TAG, "GalleryShowActivity onClick");
		
	}

	@Override
	protected void onSyncFinished() {
		// TODO Auto-generated method stub
		
	}	
}
