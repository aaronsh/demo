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

import dibang.com.Const;

import android.content.Context;
import android.util.Log;

public class RenderingDecoder extends WebBaseDecoder {
	public static final int DECODER_TYPE_EFFECT_SHOW = 0;
	public static final int DECODER_TYPE_HOUSE_SHOW = 1;
	
	private static final String TAG = "RenderingDecoder";
	
	private int mDecoderType = DECODER_TYPE_EFFECT_SHOW;
	
	public RenderingDecoder(Context cntx) {
		// TODO Auto-generated constructor stub
		super(cntx);
	}

	public void setDecoderType( int type)
	{
		mDecoderType = type;
	}

	@Override
	protected LinkedList<Object> decodeDocument(Document doc) throws Exception {
		Elements list = doc.select("div");
		test(list);
		LinkedList<HtmlHyperLink> links = new LinkedList<HtmlHyperLink>();
		for (Element e : list) {
			String attr = e.attr("class");
			if( attr.startsWith("leibie")){
				decodeImages(e, links);
			}
		}
		if(mDecoderType == DECODER_TYPE_EFFECT_SHOW){
			syncImage(links,Const.FOLDER_effect_show,DesignCaseDb.TBL_EFFECT_SHOW);
		}
		else{
			syncImage(links,Const.FOLDER_house_show,DesignCaseDb.TBL_HOUSE_SHOW);
		}		

		return null;
	}

	private void decodeImages(Element e, LinkedList<HtmlHyperLink> images) {
		// TODO Auto-generated method stub
		Element a = e.child(0);
		String Name = a.text();
		String Link = a.attr("href");
		
		RenderingImageDecoder decoder = new RenderingImageDecoder();
		String url = mDecodeWhich + Link;
		decoder.init(WebBaseDecoder.DECODE_URL_GET, url, null);
		decoder.setImageClass(Name);
		try {
			LinkedList<Object> list = decoder.decode();
			for(Object o:list ){
				images.add((HtmlHyperLink)o);
			}
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return ;
	}

}
