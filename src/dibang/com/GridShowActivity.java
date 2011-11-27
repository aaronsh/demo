package dibang.com;



import android.content.res.Resources;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import dibang.com.handle.BaseActivity;

public class GridShowActivity extends BaseActivity {

	GridAdapter mAdapter = null;
	GridView mGrid = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_grid);
        mGrid = (GridView) findViewById(R.id.grid);
        
		int type = this.getIntent().getIntExtra("type", Const.UI_TYPE_WEBSITE_DESIGN);
		TextView text = (TextView)this.findViewById(R.id.text_title);
		switch(type){
		case  Const.UI_TYPE_WEBSITE_DESIGN:
			text.setText("��վ���");
			mAdapter = new GridAdapter(this, GridAdapter.ITEM_TYPE_IMAGE_TEXT);
			break;
		case  Const.UI_TYPE_3D_ANIMATION:
			text.setText("��ά����");
			mAdapter = new GridAdapter(this, GridAdapter.ITEM_TYPE_IMAGE_TEXT);
			break;
		case  Const.UI_TYPE_EFFECT_SHOW:
			text.setText("Ч��ͼ");
			break;
		case  Const.UI_TYPE_HOUSE_SHOW:
			text.setText("����ͼ");
			break;
		case  Const.UI_TYPE_EMAGZIN:
			text.setText("������־");
			mAdapter = new GridAdapter(this, GridAdapter.ITEM_TYPE_IMAGE_TEXT);
			break;
		case  Const.UI_TYPE_PARTNER:
			text.setText("�������");
			mAdapter = new GridAdapter(this, GridAdapter.ITEM_TYPE_IMAGE_ONLY);
			mGrid.setNumColumns(3);
			break;
		}

		boolean showTopMenu = this.getIntent().getBooleanExtra("top_menu", true);
		if( !showTopMenu){
			Resources res = getResources();
			text.setBackgroundDrawable(res.getDrawable(R.drawable.top_menu_bg));
			LinearLayout group = (LinearLayout)this.findViewById(R.id.linearLayout1);
			group.removeView(group.findViewById(R.id.linearLayout4));
			group.removeView(group.findViewById(R.id.top_menu));
		}
	
		

        mGrid.setAdapter(mAdapter);
       
        
		onInitView();
	}

}
