package dibang.com.web;

import java.io.IOException;
import java.util.LinkedList;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dibang.com.Const;

import android.util.Log;



public class TopGalleryDecoder extends WebBaseDecoder{
	private static final String TAG = "TopGalleryDecoder";
	private static final String URL_BASE = "http://www.depcn.com/mb/";

	@Override
	protected LinkedList<Object> decodeDocument(Document doc) throws Exception{
		Elements list = doc.select("img");
		test(list);
		for( Element e:list ){
			downloadImg(e.attr("src"));
		}

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
}
