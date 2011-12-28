package dibang.com.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.android.camera.DesignCaseDb;
import com.android.camera.ImageDb;

import dibang.com.Const;

import android.content.Context;
import android.util.Log;

public class WebDesignDecoder extends WebBaseDecoder {

	private String[] Pages = { "http://www.depcn.com/work.asp",
			"http://www.depcn.com/bieshu.asp",
			"http://www.depcn.com/gongyu.asp",
			"http://www.depcn.com/shangye.asp",
			"http://www.depcn.com/zonghe.asp", "http://www.depcn.com/qiye.asp" };
	private String[] Classes = { Const.WEB_PAGE_CLASS_ZUIXIN,
			Const.WEB_PAGE_CLASS_BIESHU, Const.WEB_PAGE_CLASS_GONGYU,
			Const.WEB_PAGE_CLASS_SHANGYE, Const.WEB_PAGE_CLASS_ZONGHE,
			Const.WEB_PAGE_CLASS_QIYE };

	private static final String TAG = "WebDesignDecoder";




	public WebDesignDecoder(Context cntx) {
		// TODO Auto-generated constructor stub
		super(cntx);
	}

	@Override
	public LinkedList<Object> decodeUrlGet(String url) throws Exception {
		Log.v(TAG, "decodeUrlGet start");
		int size = Pages.length;
		ArrayList<HtmlHyperLink> links = new ArrayList<HtmlHyperLink>();
		for (int i = 0; i < size; i++) {
			decodePages(Pages[i], Classes[i], links);
		}
		syncImage(links);
		Log.v(TAG, "decodeUrlGet end");
		return null;
	}

	private void decodePages(String webBase, String text,
			ArrayList<HtmlHyperLink> images) {
		// TODO Auto-generated method stub

		WebDesignPagesDecoder decoder = new WebDesignPagesDecoder();
		decoder.init(WebBaseDecoder.DECODE_URL_GET, webBase, null);
		try {
			LinkedList<Object> list = decoder.decode();
			for (Object o : list) {
				HtmlHyperLink link = (HtmlHyperLink) o;
				link.Extra = text;
				images.add(link);
			}
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return;
	}

	private void syncImage(ArrayList<HtmlHyperLink> links) {
		System.out.print("syncImage");
		String path = null;

		path = Const.FOLDER_web_design;

		DesignCaseDb db = new DesignCaseDb(mCntx, DesignCaseDb.TBL_WEB_CASES);
		db.clear();
		String folder = IOFile.getModuleFolder(path);
		ArrayList<String> files = IOFile.getFileNameList(folder);
		// TODO Auto-generated method stub
		
		if( UpdateMode.getUpdateMode() == UpdateMode.FAST_UPDATE_MODE ){
			boolean rmFile = false;
			Iterator<String> itFile = files.iterator();
			while(itFile.hasNext()){
				String file = itFile.next();
				rmFile = false;
				Iterator<HtmlHyperLink> itImg = links.iterator();
				while(itImg.hasNext()){
					HtmlHyperLink img = itImg.next(); 
					if (img.Image.endsWith(file)) {
						StringBuilder b = new StringBuilder(folder);
						b.append("/");
						b.append(file);
						db.insert(img.Extra, b.toString(), img.Link, img.Name);
						itImg.remove();
						rmFile = true;
					}
				}
				if( rmFile ){
					itFile.remove();
				}

			}
		}

		// remove useless pictures
		for (String file : files) {
			deleteFile(file);
		}

		System.out.print(links.toString());
		// download images
		for (HtmlHyperLink img : links) {
			Log.v(TAG, "download " + img);
			StringBuilder b = new StringBuilder(folder);
			b.append("/");
			b.append(IOFile.getFileName(img.Image));
			db.insert(img.Extra, b.toString(), img.Link, img.Name);
			try {
				ImageDownloader.downFile(img.Image, path, null);
				Log.v(TAG, "down img "+img.Image);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		db.close();
	}

	private void deleteFile(String file) {
		// TODO Auto-generated method stub
		String img;
		if (!IOFile.sdcardExist())
			return;
		StringBuilder b = new StringBuilder();
		b.append(IOFile.getModuleFolder(Const.FOLDER_web_design));
		b.append("/");
		b.append(file);

		img = b.toString();
		Log.v(TAG, "delete " + img);
		File f = new File(img);
		f.delete();
	}

}
