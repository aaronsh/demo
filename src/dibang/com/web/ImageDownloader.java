package dibang.com.web;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.*;
import java.net.*;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;

//Referenced classes of package COM.Bangso.FunctionUtility:
//			IOFile, NetworkControl

public class ImageDownloader
{

	public static final String PIC_CACHE = "FitMissCache";

	public ImageDownloader()
	{
	}
	private static URLConnection urlConnectionFactory(Context cntx, String s)
		throws IOException
	{
		NetworkControl.NetType nettype = NetworkControl.getNetType(cntx);
		URL url = new URL(s);
		URLConnection urlconnection;
		if (nettype != null && nettype.isWap())
		{
			java.net.Proxy.Type type = java.net.Proxy.Type.HTTP;
			String s1 = nettype.getProxy();
			int i = nettype.getPort();
			InetSocketAddress inetsocketaddress = new InetSocketAddress(s1, i);
			Proxy proxy = new Proxy(type, inetsocketaddress);
			urlconnection = url.openConnection(proxy);
			urlconnection.connect();
		} else
		{
			urlconnection = url.openConnection();
			urlconnection.connect();
		}
		return urlconnection;
	}

	public void downFile(String url, String s1, String s2)
		throws IOException
	{
		String s3;
		InputStream inputstream;
		if (!IOFile.sdcardExist())
			return;
		URLConnection urlconnection;
		if (s2 == null || s2 == "")
			s3 = IOFile.getFileName(url);
		else
			s3 = s2;
		s1 = (new StringBuilder("/sdcard/")).append(s1).append("/").toString();
		urlconnection = (new URL(url)).openConnection();
		urlconnection.connect();
		inputstream = urlconnection.getInputStream();
		if (urlconnection.getContentLength() <= 0)
			throw new RuntimeException("无法获知文件大小 ");
		if (inputstream == null)
			throw new RuntimeException("无法获取文件");
		FileOutputStream fileoutputstream;
		byte abyte0[];
		String s4 = String.valueOf(s1);
		String s5 = (new StringBuilder(s4)).append(s3).toString();
		fileoutputstream = new FileOutputStream(s5);
		abyte0 = new byte[1024];
		int size;
		int offset=0;
		while(true)
		{
			size = inputstream.read(abyte0);
			if( size == -1 ){
				break;
			}
			fileoutputstream.write(abyte0, offset, size);
			offset += size;
		}
		inputstream.close();
		fileoutputstream.close();
		return;
	}
	
}
