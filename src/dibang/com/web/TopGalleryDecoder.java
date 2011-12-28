package dibang.com.web;

import java.util.LinkedList;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.android.camera.DesignCaseDb;
import dibang.com.Const;
import android.content.Context;



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
		LinkedList<HtmlHyperLink> images = new LinkedList<HtmlHyperLink>();
		for( Element e:list ){
			String link = e.attr("src");
			HtmlHyperLink img = new HtmlHyperLink();
			img.ImageUrl = URL_BASE + link;
			img.Extra = "";
			img.ForwardLink = "";
			img.Name = "";
			images.add(img);
		}

		syncImage(images, Const.FOLDER_top_gallery, DesignCaseDb.TBL_TOP_GALLERY);
		return null;
	}
}
