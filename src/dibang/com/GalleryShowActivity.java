package dibang.com;



import android.os.Bundle;
import android.widget.GridView;
import dibang.com.handle.BaseActivity;

public class GalleryShowActivity extends BaseActivity {

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

        
		onInitView();
	}
}
