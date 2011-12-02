package dibang.com.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dibang.com.Const;

import android.util.Log;

public class RenderingImageDecoder extends WebBaseDecoder {
	private static final String TAG = "TopGalleryDecoder";
	private static final String URL_BASE = "http://www.depcn.com/pic/";
	
	private String mImageClass = null;
	
	protected void setImageClass(String imgCls)
	{
		mImageClass = imgCls;
	}

	@Override
	protected LinkedList<Object> decodeDocument(Document doc) throws Exception {
		Elements list = doc.select("div[id=thumbwrapper]");
		test(list);
		Element e = list.first();
		list = e.select("li");
		LinkedList<Object> links = new LinkedList<Object>();
		for (Element img : list) {
			HtmlHyperLink link = getImgLink(img);
			if (link != null)
				links.add(link);
		}

		return links;
	}

	private HtmlHyperLink getImgLink(Element e) {
		StringBuilder b = new StringBuilder(URL_BASE);
		b.append(e.attr("value"));
		b.append(".jpg");
		
		HtmlHyperLink link = new HtmlHyperLink();
		link.Name = mImageClass;
		link.Link = b.toString();
		return link;
	}

}
