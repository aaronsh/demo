package dibang.com.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.android.camera.DesignCaseDb;
import com.android.camera.ImageDb;

import dibang.com.Const;

import android.content.Context;
import android.util.Log;

public class EbookDecoder extends WebBaseDecoder {

	private static final String TAG = "EbookDecoder";
	
	private static final String BASE_URL = "http://www.depcn.com/";

	int mPages = 0;



	public EbookDecoder(Context cntx) {
		// TODO Auto-generated constructor stub
		super(cntx);
	}

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

		syncImage(links);
		
		return links;
	}	

	private LinkedList<Object> decodePageContent(Document doc) {
		Elements list = doc.select("div[id=boxs]");
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
		Element title = list.first();
		
		HtmlHyperLink link = new HtmlHyperLink();
		link.Name = title.text().trim();
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
	
	private void syncImage(LinkedList<Object> links) {
		System.out.print("syncImage");
		String path = null;

		path = Const.FOLDER_ebook_design;

		DesignCaseDb db = new DesignCaseDb(mCntx, DesignCaseDb.TBL_EBOOK_CASES);
		db.clear();
		String folder = IOFile.getModuleFolder(path);
		ArrayList<String> files = IOFile.getFileNameList(folder);
		// TODO Auto-generated method stub
		if( UpdateMode.getUpdateMode() == UpdateMode.FAST_UPDATE_MODE ){
			boolean rmFile = false;
			Iterator<String> iterator = files.iterator();
			while(iterator.hasNext()){
				String file = iterator.next();
				rmFile = false;
				Iterator<Object> it = links.iterator();
				while(it.hasNext()){
					HtmlHyperLink img = (HtmlHyperLink)it.next();
					if (img.ImageUrl.endsWith(file)) {
						StringBuilder b = new StringBuilder(folder);
						b.append("/");
						b.append(file);
						db.insert(img.Extra, b.toString(), img.ForwardLink, img.Name);
						it.remove();
						rmFile = true;
					}
				}
				if( rmFile ){
					iterator.remove();
				}
			}
		}

		// remove useless pictures
		for (String file : files) {
			deleteFile(file);
		}

		System.out.print(links.toString());
		// download images
		for (Object o : links) {
			HtmlHyperLink img = (HtmlHyperLink)o;
			Log.v(TAG, "download " + img);
			StringBuilder b = new StringBuilder(folder);
			b.append("/");
			b.append(IOFile.getFileName(img.ImageUrl));
			db.insert(img.Extra, b.toString(), img.ForwardLink, img.Name);
			try {
				ImageDownloader.downFile(img.ImageUrl, path, null);
				Log.v(TAG, "down img "+img.ImageUrl);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		db.close();
	}

	private void deleteFile(String file) {
		// TODO Auto-generated method stub
		String img;
		if (!IOFile.sdcardExist())
			return;
		StringBuilder b = new StringBuilder();
		b.append(IOFile.getModuleFolder(Const.FOLDER_ebook_design));
		b.append("/");
		b.append(file);

		img = b.toString();
		Log.v(TAG, "delete " + img);
		File f = new File(img);
		f.delete();
	}

}
