package dibang.com.web;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



import android.util.Log;

public class WebBaseDecoder {
	public static final int DECODE_STRING = 1;
	public static final int DECODE_FILE = 2;
	public static final int DECODE_URL_GET = 3;
	public static final int DECODE_URL_POST = 4;
	private static final String TAG = "WebBaseDecoder";
	
	private int mDecodeMethod;
	protected String mDecodeWhich;
	private UrlParamList mUrlParams=null;
	
	public WebBaseDecoder(int method, String which){
		mDecodeMethod = method;
		mDecodeWhich = which;
	}
	public WebBaseDecoder(){
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
}
