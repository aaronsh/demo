package dibang.com;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import dibang.com.handle.BaseActivity;

public class CompanyIntroduce extends BaseActivity {
	private WebView mWebView = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company);
		InitView();
	}

	/***
	 * 初始化布局属性
	 */
	private void InitView() {
		mWebView = (WebView) findViewById(R.id.webview);
		
		super.onInitView();
	}
}
