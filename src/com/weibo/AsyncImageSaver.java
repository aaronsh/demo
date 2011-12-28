package com.weibo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.drawable.BitmapDrawable;

import com.weibo.util.LocalMemory;
/**
 * 异步图像保存
 * @author starry
 *
 */
public class AsyncImageSaver {
	private ExecutorService executorService=Executors.newSingleThreadExecutor();
	
	private static AsyncImageSaver asyncImageSaver;
	public static AsyncImageSaver getInstance(){
		if(asyncImageSaver == null){
			asyncImageSaver = new AsyncImageSaver();
		}
		return asyncImageSaver;
	}
	
	public void saveImage(final BitmapDrawable drawable,final String name,final String cate){
		executorService.submit(new Runnable() {
			
			@Override
			public void run() {
				new LocalMemory().saveDrawable(drawable, name, cate);
			}
		});
	}

}
