package dibang.com.web;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.*;
import java.net.*;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;

import dibang.com.Const;



//Referenced classes of package COM.Bangso.FunctionUtility:
//			IOFile, NetworkControl

public class ImageDownloader {

	public static final String PIC_CACHE = "FitMissCache";

	public ImageDownloader() {
	}

	private static URLConnection urlConnectionFactory(Context cntx, String s)
			throws IOException {
		NetworkControl.NetType nettype = NetworkControl.getNetType(cntx);
		URL url = new URL(s);
		URLConnection urlconnection;
		if (nettype != null && nettype.isWap()) {
			java.net.Proxy.Type type = java.net.Proxy.Type.HTTP;
			String s1 = nettype.getProxy();
			int i = nettype.getPort();
			InetSocketAddress inetsocketaddress = new InetSocketAddress(s1, i);
			Proxy proxy = new Proxy(type, inetsocketaddress);
			urlconnection = url.openConnection(proxy);
			urlconnection.connect();
		} else {
			urlconnection = url.openConnection();
			urlconnection.connect();
		}
		return urlconnection;
	}

	public static void downFile(String url, String path, String s2)
			throws IOException {
		String img;
		InputStream inputstream;
		if (!IOFile.sdcardExist())
			return;
		img = IOFile.getFileName(url);
		StringBuilder b = new StringBuilder();
		b.append(IOFile.getSdcardPath());
		b.append("/");
		b.append(Const.APP_PATH);
		b.append("/");
		b.append(path);
		b.append("/");
		b.append(img);

		img = b.toString();

		URLConnection urlconnection;
		urlconnection = (new URL(url)).openConnection();
		urlconnection.connect();
		inputstream = urlconnection.getInputStream();
		if (urlconnection.getContentLength() <= 0)
			throw new RuntimeException("无法获知文件大小 ");
		if (inputstream == null)
			throw new RuntimeException("无法获取文件");
		
		try {
			IOFile.createFile(img);

			FileOutputStream fileoutputstream = new FileOutputStream(img);
			byte buf[] = new byte[1024];
			int size;
			while (true) {
				size = inputstream.read(buf);
				if (size == -1) {
					break;
				}
				fileoutputstream.write(buf, 0, size);
			}
			inputstream.close();
			fileoutputstream.flush();
			fileoutputstream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

}
