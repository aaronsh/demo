package dibang.com;


import dibang.com.handle.BaseActivity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactUs extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_us);
		
		int type = this.getIntent().getIntExtra("type", Const.UI_TYPE_WEBSITE_DESIGN);
		TextView text = (TextView)this.findViewById(R.id.text_title);
		text.setText("联系我们");
		text.setTextColor(Color.rgb(12, 74, 128));


		onInitView(BaseActivity.PAGE_TYPE_CONTACT);
//		InitView();
	}
	@Override
	protected void onSyncFinished(int UpdateType) {
		// TODO Auto-generated method stub
		
	}
}
