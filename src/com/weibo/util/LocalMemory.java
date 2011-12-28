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
	public static final String PORTRAIT="pina"+"/"+"portrait"; //ͷ�񱣴��ļ���
	public static final String PRE="pina"+"/"+"pre"; //΢��ͼƬ�����ļ���
	
	/**
	 * ����ͼ��������
	 * @param drawable
	 * @param filename ͼ����
	 * @param cate PORTRAIT��ͷ�� ���� PRE��΢��ͼƬ��
	 */
	public void saveDrawable(BitmapDrawable drawable, String filename,String cate) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED)) {
			return;
		}
		String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
		//���ж�Ŀ¼�Ƿ����
		File dir = new File(sdcard + "/" + cate);
		if (!dir.exists()) { //�������򴴽�
			dir.mkdirs();
		}
		//�ж��ļ��Ƿ����
		File image = new File(sdcard + "/" + cate + "/" + filename);
		if (!image.exists()) { //�������򱣴�
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
	 * �ӱ���ȡ��ͼ��
	 * @param filename ͼ����
	 * @param cate PORTRAIT��ͷ�� ���� PRE��΢��ͼƬ��
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
