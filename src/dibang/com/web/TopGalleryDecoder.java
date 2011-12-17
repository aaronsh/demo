package dibang.com.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.android.camera.DesignCaseDb;
import com.android.camera.ImageDb;

import dibang.com.Const;

import android.content.Context;
import android.util.Log;



public class TopGalleryDecoder extends WebBaseDecoder{
	private static final String TAG = "TopGalleryDecoder";
	private static final String URL_BASE = "http://www.depcn.com/mb/";

	public TopGalleryDecoder(Context cntx) {
		// TODO Auto-generated constructor stub
		super(cntx);
	}

	@Override
	protected LinkedList<Object> decodeDocument(Document doc) throws Exception{
		Elements list = doc.select("img");
		test(list);
		ArrayList<String> images = new ArrayList<String>();
		for( Element e:list ){
			String link = e.attr("src");
			images.add(link);
		}

		syncImage(images);
		return null;
	}
	
	private void downloadImg(String img)
	{
		Log.v(TAG, "downloadImg:"+URL_BASE+img);

        try {
			ImageDownloader.downFile(URL_BASE+img, Const.FOLDER_top_gallery, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void syncImage(ArrayList<String> links) {
		System.out.print("syncImage");
		DesignCaseDb db = new DesignCaseDb(mCntx, DesignCaseDb.TBL_TOP_GALLERY);
		db.clear();

		String folder = IOFile.getModuleFolder(Const.FOLDER_top_gallery);
		ArrayList<String> files = IOFile.getFileNameList(folder);
		// TODO Auto-generated method stub
		if( UpdateMode.getUpdateMode() == UpdateMode.FAST_UPDATE_MODE ){
			Iterator<String> itImg = links.iterator();
			while(itImg.hasNext()){
				String img = itImg.next();
				Iterator<String> itFile = files.iterator();
				while( itFile.hasNext() ){
					String file = itFile.next();
					if (img.endsWith(file)) {
						StringBuilder b = new StringBuilder(folder);
						b.append("/");
						b.append(file);
						db.insert("", b.toString(), "", "");
						
						itFile.remove();
						itImg.remove();
						break;
					}
				}

			}
		}

		// remove useless pictures
		for (String file : files) {
			deleteFile(file, folder);
		}

		System.out.print(links.toString());
		// download images
		for (String img : links) {
			Log.v(TAG, "download "+img);
			String file = IOFile.getFileName(img);
			StringBuilder b = new StringBuilder(folder);
			b.append("/");
			b.append(file);
			db.insert("", b.toString(), "", "");
			downloadImg(img);
		}

		db.close();
	}

	private void deleteFile(String file, String folder) {
		// TODO Auto-generated method stub
		String img;
		if (!IOFile.sdcardExist())
			return;
		StringBuilder b = new StringBuilder();
		b.append(folder);
		b.append("/");
		b.append(file);

		img = b.toString();
		Log.v(TAG, "delete "+img);
		File f = new File(img);
		f.delete();
	}

	
}
