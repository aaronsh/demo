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
			text.setText("��վ���");
			break;
		case  Const.UI_TYPE_3D_ANIMATION:
			text.setText("��ά����");
			break;
		case  Const.UI_TYPE_EFFECT_SHOW:
			text.setText("Ч��ͼ");
			break;
		case  Const.UI_TYPE_HOUSE_SHOW:
			text.setText("����ͼ");
			break;
		case  Const.UI_TYPE_EMAGZIN:
			text.setText("������־");
			break;
		case  Const.UI_TYPE_PARTNER:
			text.setText("�������");
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
