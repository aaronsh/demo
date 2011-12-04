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

import com.android.camera.ImageDb;

import dibang.com.Const;

import android.content.Context;
import android.util.Log;

public class RenderingDecoder extends WebBaseDecoder {
	public static final int DECODER_TYPE_EFFECT_SHOW = 0;
	public static final int DECODER_TYPE_HOUSE_SHOW = 1;
	
	private static final String TAG = "RenderingDecoder";
	
	private int mDecoderType = DECODER_TYPE_EFFECT_SHOW;
	private Context mCntx;
	
	public void setDecoder(Context cntx, int type)
	{
		mCntx = cntx;
		mDecoderType = type;
	}

	@Override
	protected LinkedList<Object> decodeDocument(Document doc) throws Exception {
		Elements list = doc.select("div");
		test(list);
		ArrayList<HtmlHyperLink> links = new ArrayList<HtmlHyperLink>();
		for (Element e : list) {
			String attr = e.attr("class");
			if( attr.startsWith("leibie")){
				decodeImages(e, links);
			}
		}
		
		syncImage(links);

		return null;
	}

	private void decodeImages(Element e, ArrayList<HtmlHyperLink> images) {
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

	private void syncImage(ArrayList<HtmlHyperLink> links) {
		System.out.print("syncImage");
		String path = null;
		String tbl = null;
		
		if(mDecoderType == DECODER_TYPE_EFFECT_SHOW){
			path = Const.FOLDER_effect_show;
			tbl = ImageDb.TBL_EFFECT_SHOW;
		}
		else{
			path = Const.FOLDER_house_show;
			tbl = ImageDb.TBL_HOUSE_SHOW;
		}
		ImageDb db = new ImageDb(mCntx, tbl);
		db.clear();
		String folder = IOFile.getModuleFolder(path);
		ArrayList<String> files = IOFile.getFileNameList(folder);
		// TODO Auto-generated method stub
		int imgSize = links.size();
		int fileSize = files.size();
		for (int i=0; i<imgSize; i++) {
			HtmlHyperLink img = links.get(i);
			for (int j=0; j<fileSize; j++) {
				String file = files.get(j);
				if (img.Image.endsWith(file)) {
					StringBuilder b = new StringBuilder(folder);
					b.append("/");
					b.append(file);
					db.insert(img.Extra, b.toString(), img.Link);
					links.remove(i);
					files.remove(j);
					i--;
					imgSize--;
					fileSize--;
					break;
				}
			}

		}

		// remove useless pictures
		for (String file : files) {
			deleteFile(file, folder);
		}

		System.out.print(links.toString());
		// download images
		for (HtmlHyperLink img : links) {
			Log.v(TAG, "download "+img);
			StringBuilder b = new StringBuilder(folder);
			b.append("/");
			b.append(IOFile.getFileName(img.Image));
			db.insert(img.Extra, b.toString(), img.Link);
			try {
				ImageDownloader.downFile(img.Image, path,
						null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		db.close();
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

}
