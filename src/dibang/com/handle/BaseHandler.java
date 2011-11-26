package dibang.com.handle;




import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.Calendar;

public class BaseHandler
{
	public static final String PROGRESS_NORMAL = "加载中，请稍后。";
	Activity activity;
	String downloadUrl = "";
	String errorText = null;
	String information = "";
	boolean isShowError = false;
	double newVersion = 0.0D;
	ProgressDialog pDialog;

	public BaseHandler(Activity paramActivity)
	{
		this.activity = paramActivity;

	}

	public void checkVersion(boolean paramBoolean)
	{
		this.isShowError = paramBoolean;
		if (paramBoolean)
		{
			ProgressDialog localProgressDialog = progressDlg("正在检测更新");
			this.pDialog = localProgressDialog;
		}

	}

	public Menu createMenu(Menu paramMenu)
	{
		if (paramMenu.size() == 0)
		{
			paramMenu.add(0, 0, 0, "关于我们");
			paramMenu.add(0, 1, 1, "官网网站");
			paramMenu.add(0, 2, 2, "官方微博");
			paramMenu.add(0, 3, 3, "意见反馈");
			paramMenu.add(0, 4, 4, "检查更新");
			paramMenu.add(0, 5, 5, "退出软件");
		}
		return paramMenu;
	}

	public void createMenuSelected(MenuItem paramMenuItem)
	{

	}

	public void createQuestionMenu(Menu paramMenu)
	{
		paramMenu.add(0, 0, 0, "返回应用首页");
		paramMenu.add(0, 1, 1, "查看分享食谱");
		paramMenu.add(0, 2, 2, "查看减肥问答");
		paramMenu.add(0, 3, 3, "食谱评论通知");
		paramMenu.add(0, 4, 4, "问答回复通知");
		paramMenu.add(0, 5, 5, "我要反馈意见");
	}

	public void createQuestionMenuAction(MenuItem paramMenuItem)
	{
 
	}

	protected void dialog()
	{
		Activity localActivity = this.activity;
		AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(localActivity);
		AlertDialog.Builder localBuilder2 = localBuilder1.setMessage("确定要退出吗?");
		AlertDialog.Builder localBuilder3 = localBuilder1.setTitle("提示");

		AlertDialog.Builder localBuilder4 = localBuilder1.setPositiveButton("确认", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface paramDialogInterface, int paramInt)
			{
				paramDialogInterface.dismiss();
				try
				{
					ActivityManager localActivityManager = (ActivityManager)BaseHandler.this.activity.getSystemService("activity");
					String str = BaseHandler.this.activity.getPackageName();
					localActivityManager.restartPackage(str);
					Intent localIntent1 = new Intent("android.intent.action.MAIN");
					Intent localIntent2 = localIntent1.addCategory("android.intent.category.HOME");
					Intent localIntent3 = localIntent1.setFlags(268435456);
					BaseHandler.this.activity.startActivity(localIntent1);
					System.exit(0);
					return;
				}
				catch (Exception localException)
				{
				}
			}
		});

		AlertDialog.Builder localBuilder5 = localBuilder1.setNegativeButton("取消", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface paramDialogInterface, int paramInt)
			{
				paramDialogInterface.dismiss();
			}
		});
		localBuilder1.create().show();
	}

	public ProgressDialog progressDlg(String paramString)
	{
		ProgressDialog localProgressDialog = new ProgressDialog(activity);
		localProgressDialog.setProgressStyle(0);
		localProgressDialog.setTitle("提示");
		localProgressDialog.setMessage(paramString);
		localProgressDialog.setIndeterminate(false);
		localProgressDialog.setCancelable(true);
		localProgressDialog.setProgress(0);
		localProgressDialog.incrementProgressBy(1);

		localProgressDialog.setButton("取消", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface paramDialogInterface, int paramInt)
			{
				paramDialogInterface.cancel();
			}
		});
		localProgressDialog.show();
		return localProgressDialog;
	}

	protected void updateDialog()
	{
		Activity localActivity = this.activity;
		AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(localActivity);
		Spanned localSpanned = Html.fromHtml(this.information.replace("\r\n", "<br>"));
		AlertDialog.Builder localBuilder2 = localBuilder1.setMessage(localSpanned);
		AlertDialog.Builder localBuilder3 = localBuilder1.setTitle("有新版本拉！");



		AlertDialog.Builder localBuilder5 = localBuilder1.setNegativeButton("取消", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface paramDialogInterface, int paramInt)
			{
				paramDialogInterface.dismiss();
			}
		});
		localBuilder1.create().show();
	}
}

/* Location:           D:\bangso\dex2jar-0.0.7.11-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     COM.Bangso.Handler.BaseHandler
 * JD-Core Version:    0.6.0
 */