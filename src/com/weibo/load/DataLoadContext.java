package com.weibo.load;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

public class DataLoadContext {
	public static final int STATUS_LIST = 0;
	public static final int COMMENT_LIST = 1;
	public static final int USER_LIST = 2;
	public static final int USER_INFO = 3;
	public static final int COUNT = 4;
	public static final int PIC = 5;
	
	private DataLoader dataLoader;
	public DataLoadContext(Context context,View v,int cate){
		switch (cate) {
		case STATUS_LIST:
			dataLoader = new WeiboDataLoader(context, (ListView) v);
			break;
		case COMMENT_LIST:
			break;
		case USER_LIST:
			break;
		case USER_INFO:
			break;
		case COUNT:
			break;
		case PIC:
			break;
		default:
			break;

		}
	}

	public void loadData() {
		if(dataLoader != null){
			dataLoader.asyncLoad();
		}
	}

}
