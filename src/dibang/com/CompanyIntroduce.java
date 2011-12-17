package dibang.com;

import android.os.Bundle;
import android.webkit.WebSettings.ZoomDensity;
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


	private void InitView() {
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.loadUrl("http://www.depcn.com/mb/mabout.html");
		mWebView.getSettings().setBuiltInZoomControls(true); //显示放大缩小 controler
		mWebView.getSettings().setSupportZoom(true); //可以缩放
		mWebView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);//默认缩放模式

		super.onInitView(BaseActivity.PAGE_TYPE_ABOUT);
	}


	@Override
	protected void onSyncFinished() {
		// TODO Auto-generated method stub
		
	}
}
