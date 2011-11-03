package dibang.com;



import android.os.Bundle;
import android.widget.GridView;
import dibang.com.handle.BaseActivity;

public class GridShowActivity extends BaseActivity {
	GridAdapter mAdapter = null;
	GridView mGrid = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_grid);
		InitView();
	}

	/***
	 * 初始化布局属性
	 */
	private void InitView() {

		mAdapter = new GridAdapter(this, GridAdapter.ITEM_TYPE_IMAGE_TEXT);
		
        mGrid = (GridView) findViewById(R.id.grid);
        mGrid.setAdapter(mAdapter);
        
		onInitView();
	}
}
