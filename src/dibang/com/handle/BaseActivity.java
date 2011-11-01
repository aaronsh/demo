package dibang.com.handle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class BaseActivity extends Activity
{
	private static final String TAG = "BaseActivity";
	protected BaseHandler mBaseHandler = null;
	protected BottomMenuHandler mBottomMenu = null;
	protected TopMenuHandler mTopMenu = null;

	protected void onCreate(Bundle paramBundle)
	{
		super.onCreate(paramBundle);

		mBaseHandler =new BaseHandler(this);
        mBottomMenu = new BottomMenuHandler(this, BottomMenuHandler.HOME);
        mTopMenu = new TopMenuHandler(this, "");
        Log.v(TAG, "create "+this.getClass().toString());
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		mBaseHandler.createMenu(menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem paramMenuItem)
	{
		mBaseHandler.createMenuSelected(paramMenuItem);
		return true;
	}

	public void onPause()
	{
		super.onPause();
//		MobclickAgent.onPause(this);
	}

	public void onResume()
	{
		super.onResume();
//		MobclickAgent.onResume(this);
	}
}

/* Location:           E:\workspace\examples\apk-tools\dex2jar\classes_dex2jar.jar
 * Qualified Name:     COM.Bangso.Handler.BaseActivity
 * JD-Core Version:    0.6.0
 */