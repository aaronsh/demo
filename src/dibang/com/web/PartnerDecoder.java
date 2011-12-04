package dibang.com.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

public class PartnerDecoder extends WebBaseDecoder {
	private static final String TAG = "TopGalleryDecoder";
	private static final String URL_BASE = "http://www.depcn.com/";

	

	public PartnerDecoder(Context cntx) {
		// TODO Auto-generated constructor stub
		super(cntx);
	}

	@Override
	protected LinkedList<Object> decodeDocument(Document doc) throws Exception {
		Elements list = doc.select("div[class=classicbox]");
		test(list);
		String folder = IOFile.getModuleFolder(Const.FOLDER_partner);

		ArrayList<HtmlHyperLink> links = new ArrayList<HtmlHyperLink>();
		for (Element e : list) {
			HtmlHyperLink l = getImgLink(e);
			if (l != null)
				links.add(l);
		}
		syncImage(links);

		return null;
	}

	private void syncImage(ArrayList<HtmlHyperLink> links) {
		System.out.print("syncImage");
		// TODO Auto-generated method stub
		DesignCaseDb db = new DesignCaseDb(mCntx, DesignCaseDb.TBL_PARTNER);
		db.clear();
		String folder = IOFile.getModuleFolder(Const.FOLDER_partner);
		ArrayList<String> files = IOFile.getFileNameList(folder);
		// TODO Auto-generated method stub
		boolean rmFile = false;
		Iterator<String> iterator = files.iterator();
		while(iterator.hasNext()){
			String file = iterator.next();
			rmFile = false;
			Iterator<HtmlHyperLink> it = links.iterator();
			while(it.hasNext()){
				HtmlHyperLink img = it.next();
				if (img.Image.endsWith(file)) {
					StringBuilder b = new StringBuilder(folder);
					b.append("/");
					b.append(file);
					db.insert(img.Extra, b.toString(), img.Link, img.Name);
					it.remove();
					rmFile = true;
				}
			}
			if( rmFile ){
				iterator.remove();
			}
		}
		db.close();
		
				// remove useless pictures
		for (String file : files) {
			deleteFile(file, folder);
		}

		System.out.print(links.toString());
		// download images
		for (HtmlHyperLink img : links) {
			Log.v(TAG, "download "+img.Image);
			try {
				ImageDownloader.downFile(img.Image, Const.FOLDER_partner,
						null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
			link.Link = a.attr("href");
		}
		else{
			link.Link = "";
		}
		link.Image = URL_BASE + img.attr("src");
		link.Name = "";
		link.Extra = "";
		return link;
}

}
