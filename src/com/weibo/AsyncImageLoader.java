package com.weibo;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.weibo.util.LocalMemory;
import com.weibo.util.TextUtil;
/**
 * �첽����ͼ��<br>
 * ͼ�񱣴����<br>
 * 		����ͷ��sdcard-pina-portrait ���û�id����<br>
 * 		����΢��ͼƬ��sdcard-pina-pre ��ͼƬ��url�����һ������
 * @author starry
 *
 */
public class AsyncImageLoader {
	private ExecutorService executorService = Executors.newFixedThreadPool(3);
	private Map<Long, SoftReference<Drawable>> imageCache = new HashMap<Long, SoftReference<Drawable>>();
	private Handler handler = new Handler();
	private LocalMemory localMemory = new LocalMemory();

	private static AsyncImageLoader asyncImageLoader;

	public static AsyncImageLoader getInstance() {
		if (asyncImageLoader == null) {
			asyncImageLoader = new AsyncImageLoader();
		}
		return asyncImageLoader;
	}
	/**
	 * ����ͷ��
	 * @param id �û�id
	 * @param url ͷ��url
	 * @param iv ͷ��ImageView
	 */
	public void loadPortrait(final long id, final String url, final ImageView iv) {
		if (null == url || "".equals(url)) {
			return;
		}
		if (imageCache.containsKey(id)) { //ͼ������������У�ֱ�Ӽ���
			SoftReference<Drawable> softReference = imageCache.get(id);
			if (softReference != null) {
				if (softReference.get() != null) {
					log("load portrait not null");
					iv.setImageDrawable(softReference.get());
				}
			}
		}
		//�������̴߳ӱ��ػ��������ַ����ͼ��
		executorService.submit(new Runnable() {
			BitmapDrawable bitmapDrawable = null;

			@Override
			public void run() {
				bitmapDrawable = localMemory.getDrawable(String.valueOf(id),
						LocalMemory.PORTRAIT);
				if (bitmapDrawable == null) { // ������û��ָ��ͼƬ������������
					try {
						bitmapDrawable = new BitmapDrawable(new URL(url)
								.openStream()); // ��ȡͼƬ
						imageCache.put(id, new SoftReference<Drawable>(
								bitmapDrawable)); // ����������
						AsyncImageSaver.getInstance().saveImage(bitmapDrawable,
								String.valueOf(id), LocalMemory.PORTRAIT); // �첽����������
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				//hanlder�����̼߳���ͼ��
				handler.post(new Runnable() {

					@Override
					public void run() {
						iv.setImageDrawable(bitmapDrawable);
					}
				});
			}
		});
	}
	/**
	 * ����ͼƬ
	 * @param id ͼƬ����΢��id
	 * @param url ͼƬurl
	 * @param iv ��ʾͼƬ��ImageView
	 */
	public void loadPre(final long id, final String url, final ImageView iv) {
		if (null == url || "".equals(url)) {
			iv.setVisibility(View.GONE);
			return;
		}
		iv.setVisibility(View.VISIBLE);
		if (imageCache.containsKey(id)) { //ͼ��������������У�ֱ�Ӽ���
			SoftReference<Drawable> softReference = imageCache.get(id);
			if (softReference != null) {
				if (softReference.get() == null) {
					log("load pre null");
					iv.setImageDrawable(softReference.get());
				}
			}
		}
		//�������̴߳ӱ��ػ����������
		executorService.submit(new Runnable() {
			BitmapDrawable bitmapDrawable = null;

			@Override
			public void run() {
				bitmapDrawable = localMemory.getDrawable(TextUtil
						.formatName(url), LocalMemory.PRE);
				if (bitmapDrawable == null) { // ������û��ָ��ͼƬ������������
					try {
						bitmapDrawable = new BitmapDrawable(new URL(url)
								.openStream()); // ��ȡͼƬ
						imageCache.put(id, new SoftReference<Drawable>(
								bitmapDrawable)); // ����������
						AsyncImageSaver.getInstance()
								.saveImage(bitmapDrawable,
										TextUtil.formatName(url),
										LocalMemory.PRE); // �첽����������
					} catch (MalformedURLException e) {
						e.printStackTrace();
						log(e.toString());
					} catch (IOException e) {
						e.printStackTrace();
						log(e.toString());
					}
				}
				//handler�����̼߳���ͼ��
				handler.post(new Runnable() {

					@Override
					public void run() {
						iv.setImageDrawable(bitmapDrawable);
					}
				});
			}
		});

	}

	void log(String msg) {
		Log.i("weibo", "AsyncImageLoader--" + msg);
	}

}
