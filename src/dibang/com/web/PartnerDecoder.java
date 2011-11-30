package dibang.com.web;

import java.io.IOException;
import java.util.LinkedList;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dibang.com.Const;

import android.util.Log;



public class PartnerDecoder extends WebBaseDecoder{
	private static final String TAG = "TopGalleryDecoder";
	private static final String URL_BASE = "http://www.depcn.com/";

	@Override
	protected LinkedList<Object> decodeDocument(Document doc) throws Exception{
		Elements list = doc.select("div[class=classicbox]");
		test(list);
		for( Element e:list ){
			String file = getImgLink(e);
			if( file != null )
				downloadImg(file);
		}

		return null;
	}
	private String getImgLink(Element e)
	{
		Elements list = e.select("div[class=showpic]");
		if( list == null )
			return null;
		e = list.first();
		list = e.select("img");
		if( list == null )
			return null;
		e = list.first();
		return e.attr("src");
	}
	
	private void downloadImg(String img)
	{
		Log.v(TAG, "downloadImg:"+URL_BASE+img);

        try {
			ImageDownloader.downFile(URL_BASE+img, Const.FOLDER_partner, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
