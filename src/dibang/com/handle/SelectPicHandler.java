package dibang.com.handle;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import java.io.File;
import java.io.PrintStream;

public class SelectPicHandler
{
  public static int CAREMA_PICTURE;
  public static int SAVE_PICTURE = 1233;
  private Activity activity = null;
  private int aspectX;
  private int aspectY;
  private File cameraFile;
  private int height;
  private File saveFile;
  private int width;

  static
  {
    CAREMA_PICTURE = 1234;
  }

  public SelectPicHandler(Activity paramActivity, File paramFile, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.activity = paramActivity;
    this.saveFile = paramFile;
    this.width = paramInt1;
    this.height = paramInt2;
    this.aspectX = paramInt3;
    this.aspectY = paramInt4;
    StringBuilder localStringBuilder = new StringBuilder();
    File localFile1 = Environment.getExternalStorageDirectory();
    String str = localFile1 + "/FitMissCache/camera.jpg";
    File localFile2 = new File(str);
    this.cameraFile = localFile2;
  }

  private boolean checkSDCard()
  {
    if (Environment.getExternalStorageState().equals("mounted")){
    	return true;
    }
      System.out.println("sd卡不存在");
      return false;
  }

  private void editSelectPhoto()
  {
    Intent localIntent1 = new Intent("android.intent.action.GET_CONTENT");
    Intent localIntent2 = localIntent1.setType("image/*");
    Intent localIntent3 = localIntent1.putExtra("crop", "true");
    Intent localIntent4 = photoIntent(localIntent1);
    Activity localActivity = this.activity;
    int i = SAVE_PICTURE;
    localActivity.startActivityForResult(localIntent4, i);
  }

  private void openCamera()
  {
    try
    {
      Intent localIntent1 = new Intent("android.media.action.IMAGE_CAPTURE", null);
      Uri localUri = Uri.fromFile(this.cameraFile);
      Intent localIntent2 = localIntent1.putExtra("output", localUri);
      Activity localActivity = this.activity;
      int i = CAREMA_PICTURE;
      localActivity.startActivityForResult(localIntent1, i);
      return;
    }
    catch (Exception localException)
    {
      System.out.println("相机调用错误");
    }
  }

  private Intent photoIntent(Intent paramIntent)
  {
    Intent localIntent1 = paramIntent.putExtra("crop", "true");
    int i = this.aspectX;
    Intent localIntent2 = paramIntent.putExtra("aspectX", i);
    int j = this.aspectY;
    Intent localIntent3 = paramIntent.putExtra("aspectY", j);
    int k = this.width;
    Intent localIntent4 = paramIntent.putExtra("outputX", k);
    int m = this.height;
    Intent localIntent5 = paramIntent.putExtra("outputY", m);
    Uri localUri = Uri.fromFile(this.saveFile);
    Intent localIntent6 = paramIntent.putExtra("output", localUri);
    Intent localIntent7 = paramIntent.putExtra("outputFormat", "JPEG");
    return paramIntent;
  }

  public void editCameraPhoto()
  {
    Intent localIntent1 = new Intent("com.android.camera.action.CROP");
    Uri localUri = Uri.fromFile(this.cameraFile);
    Intent localIntent2 = localIntent1.setDataAndType(localUri, "image/*");
    Intent localIntent3 = photoIntent(localIntent1);
    Activity localActivity = this.activity;
    int i = SAVE_PICTURE;
    localActivity.startActivityForResult(localIntent3, i);
  }

  public void startCamera()
  {
    if (!checkSDCard())
      return;
    openCamera();
  }

  public void startPhotoList()
  {
    if (!checkSDCard())
      return;
    editSelectPhoto();
  }
}

/* Location:           D:\bangso\dex2jar-0.0.7.11-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     COM.Bangso.Handler.SelectPicHandler
 * JD-Core Version:    0.6.0
 */