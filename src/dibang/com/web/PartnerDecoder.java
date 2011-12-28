package dibang.com.web;


import java.util.LinkedList;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.android.camera.DesignCaseDb;

import dibang.com.Const;

import android.content.Context;


public class PartnerDecoder extends WebBaseDecoder {
	private static final String TAG = "PartnerDecoder";
	private static final String URL_BASE = "http://www.depcn.com/";

	

	public PartnerDecoder(Context cntx) {
		// TODO Auto-generated constructor stub
		super(cntx);
	}

	@Override
	protected LinkedList<Object> decodeDocument(Document doc) throws Exception {
		Elements list = doc.select("div[class=classicbox]");
		test(list);

		LinkedList<HtmlHyperLink> links = new LinkedList<HtmlHyperLink>();
		for (Element e : list) {
			HtmlHyperLink l = getImgLink(e);
			if (l != null)
				links.add(l);
		}
		syncImage(links, Const.FOLDER_partner, DesignCaseDb.TBL_PARTNER);

		return null;
	}

	private HtmlHyperLink getImgLink(Element e) {
		Elements list = e.select("div[class=showpic]");
		if (list == null)
			return null;
		e = list.first();
		list = e.select("img");
		if( list == null )
			return null;
		Element img = list.first();
		
		HtmlHyperLink link = new HtmlHyperLink();
		Element a = img.parent();
		if( "a".compareTo(a.tagName()) == 0 ){
			link.ForwardLink = a.attr("href");
		}
		else{
			link.ForwardLink = "";
		}
		link.ImageUrl = URL_BASE + img.attr("src");
		link.Name = "";
		link.Extra = "";
		return link;
}

}
