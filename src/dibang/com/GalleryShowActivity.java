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
	 * 初始化布局属性
	 */
	private void InitView() {
		mSView = (HorizontalScrollView)findViewById(R.id.horizontalScrollView1);
		
		mSView.setVerticalScrollBarEnabled(false);
		mSView.setHorizontalScrollBarEnabled(false);

		mBtnPrev = (ImageButton)findViewById(R.id.btn_prev);
		mBtnNext = (ImageButton)findViewById(R.id.btn_next);
		mBtnPrev.setOnClickListener(this);
		mBtnNext.setOnClickListener(this);
		onInitView();
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=null;
		if(mBtnPrev == v ){
			intent = new Intent(this, MainActivity.class);
		}
		if(mBtnNext  == v ){
			intent = new Intent(this, CompanyIntroduce.class);
		}
		Log.v(TAG, "GalleryShowActivity onClick");
		
	}	
}