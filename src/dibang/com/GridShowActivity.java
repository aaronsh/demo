package dibang.com;



import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;
import dibang.com.handle.BaseActivity;

public class GridShowActivity extends BaseActivity {

	GridAdapter mAdapter = null;
	GridView mGrid = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_grid);
		int type = this.getIntent().getIntExtra("type", Const.UI_TYPE_WEBSITE_DESIGN);
		TextView text = (TextView)this.findViewById(R.id.text_title);
		switch(type){
		case  Const.UI_TYPE_WEBSITE_DESIGN:
			text.setText("网站设计");
			break;
		case  Const.UI_TYPE_3D_ANIMATION:
			text.setText("三维动画");
			break;
		case  Const.UI_TYPE_EFFECT_SHOW:
			text.setText("效果图");
			break;
		case  Const.UI_TYPE_HOUSE_SHOW:
			text.setText("户型图");
			break;
		case  Const.UI_TYPE_EMAGZIN:
			text.setText("电子杂志");
			break;
		case  Const.UI_TYPE_PARTNER:
			text.setText("合作伙伴");
			break;
		}
		InitView();
	}


	private void InitView() {

		mAdapter = new GridAdapter(this, GridAdapter.ITEM_TYPE_IMAGE_TEXT);
		
        mGrid = (GridView) findViewById(R.id.grid);
        mGrid.setAdapter(mAdapter);
        
		onInitView();
	}
}
