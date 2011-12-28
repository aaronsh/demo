package dibang.com.web;

import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.android.camera.DesignCaseDb;


import dibang.com.Const;

import android.content.Context;
import android.util.Log;

public class AniDesignDecoder extends WebBaseDecoder {

	private static final String TAG = "AniDesignDecoder";
	
	private static final String BASE_URL = "http://www.depcn.com/";

	int mPages = 0;


	public AniDesignDecoder(Context cntx) {
		// TODO Auto-generated constructor stub
		super(cntx);
	}


	@Override
	protected LinkedList<Object> decodeDocument(Document doc) throws Exception {
		mPages = getPageCount(doc);
		LinkedList<HtmlHyperLink> links = decodePageContent(doc);
		for (int page = 2; page <= mPages; page++) {

			String wholeUrl = String.format("%s?page=%d", mDecodeWhich, page);
			Log.v(TAG, "decoding:"+wholeUrl);
			doc = Jsoup.connect(wholeUrl).get();
			LinkedList<HtmlHyperLink> more = decodePageContent(doc);
			links.addAll(more);
		}

		syncImage(links, Const.FOLDER_ani_design, DesignCaseDb.TBL_ANI_CASES);
		
		return null;
	}	

	private LinkedList<HtmlHyperLink> decodePageContent(Document doc) {
		Elements list = doc.select("div[class=videobox]");
		try {
			test(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LinkedList<HtmlHyperLink> links = new LinkedList<HtmlHyperLink>();
		for (Element e : list) {
			HtmlHyperLink l = getImageLine(e);
			if (l != null)
				links.add(l);
		}
		return links;
	}

	private HtmlHyperLink getImageLine(Element e) {
		// TODO Auto-generated method stub
		Elements list = e.getElementsByAttributeValue("class", "showpic");
		if( list == null || list.size() != 1 ){
			return null;
		}
		Element a = firstChild( list.first(), "a");
		if( a == null ){
			return null;
		}
		Element img = firstChild(a, "img");
		if( img == null ){
			return null;
		}
		
		list =  e.getElementsByAttributeValue("class", "showtitle");
		if( list == null || list.size() != 1 ){
			return null;
		}
		Element title = firstChild(list.first(), "a");
		if( title == null ){
			return null;
		}
		
		HtmlHyperLink link = new HtmlHyperLink();
		link.Name = title.text();
		String addr = a.attr("href");
		if( !addr.startsWith("http://" ) )
			addr = BASE_URL + addr;
		link.ForwardLink = addr;
		link.ImageUrl = BASE_URL + img.attr("src");
		link.Extra = "";
		return link;
	}

	private int getPageCount(Document doc) {
		// TODO Auto-generated method stub
		Elements list = doc.select("span[class=current]");
		if( list == null || list.size() != 1 ){
			return 0;
		}
		int pages = 0;
		Element e = list.first();
		while (e != null) {
			String text = e.text();
			try {
				int index = Integer.valueOf(text);
				if (index > pages)
					pages = index;
			} catch (NumberFormatException ex) {
//				ex.printStackTrace();
			}
			e = e.nextElementSibling();
		}
		Log.v(TAG, "pages:" + pages);
		return pages;
	}



}
