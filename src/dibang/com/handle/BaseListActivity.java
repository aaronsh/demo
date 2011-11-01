package dibang.com.handle;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class BaseListActivity extends ListActivity
{
	private static final String TAG = "BaseListActivity";
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

	public boolean onCreateOptionsMenu(Menu paramMenu)
	{
		mBaseHandler.createMenu(paramMenu);
		return super.onCreateOptionsMenu(paramMenu);
	}

	public boolean onOptionsItemSelected(MenuItem paramMenuItem)
	{
		mBaseHandler.createMenuSelected(paramMenuItem);
		return true;
	}

	public void onPause()
	{
		super.onPause();
		//4bc    MobclickAgent.onPause(this);
	}

	public void onResume()
	{
		super.onResume();
		//4bc    MobclickAgent.onResume(this);
	}
}

/* Location:           E:\workspace\examples\apk-tools\dex2jar\classes_dex2jar.jar
 * Qualified Name:     COM.Bangso.Handler.BaseListActivity
 * JD-Core Version:    0.6.0
 */