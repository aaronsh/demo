package dibang.com;



import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;
import dibang.com.handle.BaseActivity;

public class GridShowActivity extends BaseActivity {
	public static final int TYPE_WEBSITE_DESIGN = 1;
	public static final int TYPE_3D_ANIMATION = 2;
	public static final int TYPE_EFFECT_SHOW = 3;
	public static final int TYPE_HOUSE_SHOW = 4;
	public static final int TYPE_EMAGZIN = 5;
	public static final int TYPE_PARTNER = 6;
	GridAdapter mAdapter = null;
	GridView mGrid = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_grid);
		int type = this.getIntent().getIntExtra("type", TYPE_WEBSITE_DESIGN);
		TextView text = (TextView)this.findViewById(R.id.text_title);
		switch(type){
		case  TYPE_WEBSITE_DESIGN:
			text.setText("��վ���");
			break;
		case  TYPE_3D_ANIMATION:
			text.setText("��ά����");
			break;
		case  TYPE_EFFECT_SHOW:
			text.setText("Ч��ͼ");
			break;
		case  TYPE_HOUSE_SHOW:
			text.setText("����ͼ");
			break;
		case  TYPE_EMAGZIN:
			text.setText("������־");
			break;
		case  TYPE_PARTNER:
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
