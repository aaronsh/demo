package dibang.com.web;



import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.android.camera.DesignCaseDb;



import android.content.Context;
import android.util.Log;

public class WebBaseDecoder {
	public static final int DECODE_STRING = 1;
	public static final int DECODE_FILE = 2;
	public static final int DECODE_URL_GET = 3;
	public static final int DECODE_URL_POST = 4;
	private static final String TAG = "WebBaseDecoder";
	

	private static final int SKIP_IMAGE=1;
	private static final int DOWNLOAD_IMAGE = 2;
	
	private int mDecodeMethod;
	protected String mDecodeWhich;
	private UrlParamList mUrlParams=null;
	protected Context mCntx;
	
	public WebBaseDecoder(int method, String which){
		mDecodeMethod = method;
		mDecodeWhich = which;
	}
	public WebBaseDecoder(){
	}
	
	public WebBaseDecoder(Context cntx)
	{
		mCntx = cntx;
	}
	
	public void init(int method, String which, UrlParamList params)
	{
		mDecodeMethod = method;
		mDecodeWhich = which;	
		mUrlParams = params;
	}
	
	public LinkedList<Object> decodeString(String html){
		return null;
	}

	public LinkedList<Object> decodeFile(String file) throws Exception{
		File input = new File(file); 
		Document doc = Jsoup.parse(input, "UTF-8", "www.example.com");
		return decodeDocument(doc);
	}

	public LinkedList<Object> decodeUrlGet(String url) throws Exception{
		String wholeUrl = buildWholeUrl(url, mUrlParams);
		Log.v(TAG, "decode url:"+wholeUrl);
		Document doc = Jsoup.connect(wholeUrl).get();
		LinkedList<Object> list = decodeDocument(doc);
		Log.v(TAG, "decode end");
		return list;
	}
	public LinkedList<Object> decodeUrlPost(String url) throws Exception{
		HashMap<String, String> map= new HashMap<String, String>();
		for(UrlParam param:mUrlParams){
			map.put(param.Key, param.Value);
		}
		Document doc = Jsoup.connect(url).data(map).post();
		LinkedList<Object> list = decodeDocument(doc);
		Log.v(TAG, "decode end");
		return list;
	}	
	
	public LinkedList<Object> decode() throws Exception{
		Document doc = null;
		switch(mDecodeMethod){
		case DECODE_STRING:
			break;
		case DECODE_FILE:
			break;
		case DECODE_URL_GET:
			return decodeUrlGet(mDecodeWhich);
		case DECODE_URL_POST:
			return decodeUrlPost(mDecodeWhich);
		}
		return null;
	}

	protected LinkedList<Object> decodeDocument(Document doc)  throws Exception{
		return null;
	}
	
	protected Elements filterElementAttr(Elements elements, String attr, String val){
		Elements list= new Elements();
		for (Element src : elements) {
			String attrVal = src.attr(attr);
			if( attrVal.equals(val)){
				list.add(src);
			}
		}
		return list;
	}
	
	protected Element firstChild(Element element, String tag)
	{
		Elements children = element.children();
		for(Element e:children){
			if( tag.compareTo(e.tagName()) == 0){
				return e;
			}
		}
		return null;
	}
	
	protected String getText(Elements elements, String attr,  String val) throws Exception{
		String text = "";
		for (Element src : elements) {
			String attrVal = src.attr(attr);
			if( attrVal.equals(val)){
				text = src.text();
				break;
			}
		}
		return text;
	}
	
	protected void test(Elements list)  throws Exception{
		if( list == null || list.size()==0 ){
			throw new Exception("无法满足查询条件");
		}
	}
	
	private String buildWholeUrl(String urlBase, UrlParamList params){
		StringBuilder strBuilder = new StringBuilder();
		boolean firstParam = true;
		if( params != null ){
			Iterator<UrlParam> it = params.iterator();
			while(it.hasNext()){ 
				try {
					UrlParam p = it.next(); 
					if( firstParam ){
						strBuilder.append("?");
						firstParam = false;
					}
					else{
						strBuilder.append("&");
					}
					strBuilder.append(p.Key);
					strBuilder.append("=");
					String val;
					val = URLEncoder.encode(p.Value, "UTF-8");
					strBuilder.append( val );
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
		}
		return urlBase + strBuilder.toString();
	}

	/*
		It synchronizes images from the list. All images in the list are in right order.
	*/
	protected void syncImage(LinkedList<HtmlHyperLink> links, String ImgFolder,
			String DbName) {
		System.out.print("syncImage");

		DesignCaseDb db = new DesignCaseDb(mCntx, DbName);
		db.clear();
		String folder = IOFile.getModuleFolder(ImgFolder);
		ArrayList<String> files = IOFile.getFileNameList(folder);
		// TODO Auto-generated method stub

		if (UpdateMode.getUpdateMode() == UpdateMode.FAST_UPDATE_MODE) {
			int action = DOWNLOAD_IMAGE;

			Iterator<HtmlHyperLink> itImg = links.iterator();
			while (itImg.hasNext()) {
				HtmlHyperLink img = itImg.next();
				String imgShortName = IOFile.getFileName(img.ImageUrl);
				StringBuilder b = new StringBuilder(folder);
				b.append("/");
				b.append(imgShortName);
				db.insert(img.Extra, b.toString(), img.ForwardLink, img.Name);
				action = DOWNLOAD_IMAGE;
				Iterator<String> itFile = files.iterator();
				while (itFile.hasNext()) {
					String file = itFile.next();
					if (img.ImageUrl.endsWith(file)) {
						action = SKIP_IMAGE;
						break;
					}
				}
				if (action == DOWNLOAD_IMAGE) {
					try {
						ImageDownloader.downFile(img.ImageUrl, ImgFolder, null);
						Log.v(TAG, "down img " + img.ImageUrl);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			ListIterator<String> localFiles = files.listIterator();
			while (localFiles.hasNext()) {
				String fileName = localFiles.next();
				ListIterator<HtmlHyperLink> images = links.listIterator();
				while (images.hasNext()) {
					HtmlHyperLink img = images.next();
					if (img.ImageUrl.endsWith(fileName)) {
						images.remove();
						localFiles.remove();
					}
				}
			}
			for (String toDelete : files) {
				deleteFile(toDelete, ImgFolder);
			}
		} else {
			// remove useless pictures
			for (String file : files) {
				deleteFile(file, ImgFolder);
			}

			System.out.print(links.toString());
			// download images
			Iterator<HtmlHyperLink> itImg = links.iterator();
			while (itImg.hasNext()) {
				HtmlHyperLink img = itImg.next();
				Log.v(TAG, "download " + img);
				StringBuilder b = new StringBuilder(folder);
				b.append("/");
				b.append(IOFile.getFileName(img.ImageUrl));
				db.insert(img.Extra, b.toString(), img.ForwardLink, img.Name);
				try {
					ImageDownloader.downFile(img.ImageUrl, ImgFolder, null);
					Log.v(TAG, "down img " + img.ImageUrl);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		db.close();
	}

	private void deleteFile(String file, String ImgFolder) {
		// TODO Auto-generated method stub
		String img;
		if (!IOFile.sdcardExist())
			return;
		StringBuilder b = new StringBuilder();
		b.append(IOFile.getModuleFolder(ImgFolder));
		b.append("/");
		b.append(file);

		img = b.toString();
		Log.v(TAG, "delete " + img);
		File f = new File(img);
		f.delete();
	}

	
}
