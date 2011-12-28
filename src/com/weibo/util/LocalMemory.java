package com.weibo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;

public class LocalMemory {
	public static final String PORTRAIT="pina"+"/"+"portrait"; //头像保存文件夹
	public static final String PRE="pina"+"/"+"pre"; //微博图片保存文件夹
	
	/**
	 * 保存图像至本地
	 * @param drawable
	 * @param filename 图像名
	 * @param cate PORTRAIT（头像） 或者 PRE（微博图片）
	 */
	public void saveDrawable(BitmapDrawable drawable, String filename,String cate) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED)) {
			return;
		}
		String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
		//先判断目录是否存在
		File dir = new File(sdcard + "/" + cate);
		if (!dir.exists()) { //不存在则创建
			dir.mkdirs();
		}
		//判断文件是否存在
		File image = new File(sdcard + "/" + cate + "/" + filename);
		if (!image.exists()) { //不存在则保存
			try {
				image.createNewFile();
				FileOutputStream fileOutputStream=new FileOutputStream(image);
				if(drawable.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)){
					fileOutputStream.flush();
				}
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
				log(e.toString());
			}
		}
	}
	/**
	 * 从本地取出图像
	 * @param filename 图像名
	 * @param cate PORTRAIT（头像） 或者 PRE（微博图片）
	 * @return
	 */
	public BitmapDrawable getDrawable(String filename,String cate){
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED)) {
			return null;
		}
		String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
		File image = new File(sdcard + "/" + cate+"/"+filename);
		if(image.exists()){
			try {
				FileInputStream fileInputStream=new FileInputStream(image);
				BitmapDrawable drawable=new BitmapDrawable(fileInputStream);
				return drawable;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	void log(String msg) {
		Log.i("weibo", "LocalMemory--" + msg);
	}
}
