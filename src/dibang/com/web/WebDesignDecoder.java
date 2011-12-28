package dibang.com.web;

import java.util.LinkedList;

import com.android.camera.DesignCaseDb;
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
		LinkedList<HtmlHyperLink> links = new LinkedList<HtmlHyperLink>();
		for (int i = 0; i < size; i++) {
			decodePages(Pages[i], Classes[i], links);
		}
		syncImage(links,Const.FOLDER_web_design,DesignCaseDb.TBL_WEB_CASES);
		Log.v(TAG, "decodeUrlGet end");
		return null;
	}

	private void decodePages(String webBase, String text,
			LinkedList<HtmlHyperLink> images) {
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
}
