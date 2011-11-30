package dibang.com.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dibang.com.Const;

import android.util.Log;

public class PartnerDecoder extends WebBaseDecoder {
	private static final String TAG = "TopGalleryDecoder";
	private static final String URL_BASE = "http://www.depcn.com/";

	@Override
	protected LinkedList<Object> decodeDocument(Document doc) throws Exception {
		Elements list = doc.select("div[class=classicbox]");
		test(list);
		String folder = IOFile.getModuleFolder(Const.FOLDER_partner);
		ArrayList<String> files = IOFile.getFileNameList(folder);
		ArrayList<String> links = new ArrayList<String>();
		for (Element e : list) {
			String file = getImgLink(e);
			if (file != null)
				links.add(file);
		}
		syncImage(links, files);

		return null;
	}

	private void syncImage(ArrayList<String> links, ArrayList<String> files) {
		System.out.print("syncImage");
		// TODO Auto-generated method stub
		int imgSize = links.size();
		int fileSize = files.size();
		for (int i=0; i<imgSize; i++) {
			String img = links.get(i);
			for (int j=0; j<fileSize; j++) {
				String file = files.get(j);
				if (img.endsWith(file)) {
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
			deleteFile(file);
		}

		System.out.print(links.toString());
		// download images
		for (String img : links) {
			Log.v(TAG, "download "+img);
			try {
				ImageDownloader.downFile(URL_BASE + img, Const.FOLDER_partner,
						null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void deleteFile(String file) {
		// TODO Auto-generated method stub
		String img;
		if (!IOFile.sdcardExist())
			return;
		StringBuilder b = new StringBuilder();
		b.append(IOFile.getSdcardPath());
		b.append("/");
		b.append(Const.APP_PATH);
		b.append("/");
		b.append(Const.FOLDER_partner);
		b.append("/");
		b.append(file);

		img = b.toString();
		Log.v(TAG, "delete "+img);
		File f = new File(img);
		f.delete();
	}

	private String getImgLink(Element e) {
		Elements list = e.select("div[class=showpic]");
		if (list == null)
			return null;
		e = list.first();
		list = e.select("img");
		if (list == null)
			return null;
		e = list.first();
		return e.attr("src");
	}

}
