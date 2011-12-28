package dibang.com.web;

import java.util.LinkedList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.util.Log;

public class WebDesignPagesDecoder extends WebBaseDecoder {

	private static final String TAG = "WebDesignPagesDecoder";
	private static final String BASE_URL = "http://www.depcn.com/";

	int mPages = 0;

	@Override
	protected LinkedList<Object> decodeDocument(Document doc) throws Exception {
		mPages = getPageCount(doc);
		LinkedList<Object> links = decodePageContent(doc);
		for (int page = 2; page <= mPages; page++) {

			String wholeUrl = String.format("%s?page=%d", mDecodeWhich, page);
			Log.v(TAG, "decoding:"+wholeUrl);
			doc = Jsoup.connect(wholeUrl).get();
			LinkedList<Object> more = decodePageContent(doc);
			links.addAll(more);
		}

		return links;
	}

	private LinkedList<Object> decodePageContent(Document doc) {
		Elements list = doc.select("div[id=box]");
		try {
			test(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LinkedList<Object> links = new LinkedList<Object>();
		for (Element e : list) {
			HtmlHyperLink l = getImageLine(e);
			if (l != null)
				links.add(l);
		}
		return links;
	}

	private HtmlHyperLink getImageLine(Element e) {
		// TODO Auto-generated method stub
		HtmlHyperLink link = new HtmlHyperLink();
		Elements list = e.getElementsByAttributeValue("class", "pic");
		if( list == null || list.size() != 1 ){
			return null;
		}
		Element a = list.first();
		a = a.child(0);
		link.ForwardLink = a.attr("href");
		a = a.child(0);
		link.ImageUrl = BASE_URL + a.attr("src");
		list = e.getElementsByAttributeValue("class", "showtitle");
		if( list == null || list.size() != 1 ){
			return null;
		}
		a = list.first();
		link.Name = a.text();
		
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
