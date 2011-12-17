package dibang.com.web;

public class UpdateMode
{
	public static final int FAST_UPDATE_MODE = 0;
	public static final int FULL_UPDATE_MODE = 1;
	
	private static int mMode=FAST_UPDATE_MODE;
	public static void setUpdateMode(int mode){
		mMode = mode;
	}
	public static int getUpdateMode()
	{
		return mMode;
	}
}

