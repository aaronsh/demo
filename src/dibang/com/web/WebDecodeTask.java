package dibang.com.web;


import java.util.LinkedList;

import dibang.com.OnWebTaskFinish;
import dibang.com.web.WebBaseDecoder;
import android.os.AsyncTask;



public class WebDecodeTask extends AsyncTask<Object, Void, Void>
{
	private WebBaseDecoder mDecoder;
	private int mId;
	private String mError = null;
	private OnWebTaskFinish mNotifier=null;
	private LinkedList<Object> mList = null;
	
	public WebDecodeTask(WebBaseDecoder decoder, int id, OnWebTaskFinish notifier)
	{
		mDecoder = decoder;
		mId = id;
		mNotifier = notifier;
	}

	protected Void doInBackground(Object[] paramArrayOfObject)
	{

		try
		{
			mList = mDecoder.decode();
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			//System.out.print(e);
			mError = e.getMessage();
		}
		return null;
	}

	protected void onPostExecute(Void paramVoid)
	{
		mNotifier.onWebTaskFinished(mId, mList, mError);
	}
}
