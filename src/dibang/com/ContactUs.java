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

		TextView text = (TextView) this.findViewById(R.id.text_title);
		text.setText("联系我们");
		text.setTextColor(Color.rgb(12, 74, 128));

		LinearLayout item;
		final int[] ids = { R.id.item_phone, R.id.item_fax, R.id.item_mobile,
				R.id.item_email, R.id.item_website, R.id.item_postcode,
				R.id.item_address, R.id.item_map };
		for (int id : ids) {
			item = (LinearLayout) findViewById(id);
			item.setOnClickListener(this);
		}
		onInitView(BaseActivity.PAGE_TYPE_CONTACT);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.item_phone:
			phoneUs("tel:025-83171365");
			break;
		case R.id.item_fax:
			// phoneUs("tel:025-83171375");
			break;
		case R.id.item_mobile:
			phoneUs("tel:13813385566");
			break;
		case R.id.item_email:
			emailUs();
			break;
		case R.id.item_website:
			websizeUs("http://www.depcn.com");
			break;
		case R.id.item_address:
		case R.id.item_map:
			mapUs();
			break;
		default:
			break;
		}
	}

	private void mapUs() {
		// TODO Auto-generated method stub
		Intent i = new Intent(
				Intent.ACTION_VIEW,
				Uri.parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q=32.0647,118.73955"));
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				& Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		i.setClassName("com.google.android.apps.maps",
				"com.google.android.maps.MapsActivity");
		startActivity(i);
	}

	private void websizeUs(String url) {
		// TODO Auto-generated method stub
		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}

	private void emailUs() {
		// TODO Auto-generated method stub
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		// 设置文本格式
		emailIntent.setType("text/plain");
		// 设置对方邮件地址
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				"jigi001@163.com");
		// 设置标题内容
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "联系我们");
		// 设置邮件文本内容
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "你好：");
		startActivity(Intent.createChooser(emailIntent, "Choose Email Client"));
	}

	// 打电话
	private void phoneUs(String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));
		ContactUs.this.startActivity(intent);
	}

	@Override
	protected void onSyncFinished(int UpdateType) {
		// TODO Auto-generated method stub

	}
}
