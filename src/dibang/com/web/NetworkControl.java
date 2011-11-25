package dibang.com.web;

import android.content.Context;
import android.net.*;

public class NetworkControl
{
	public static class NetType
	{

		private String apn;
		private boolean isWap;
		private int port;
		private String proxy;
		private String typeName;

		public String getApn()
		{
			return apn;
		}

		public int getPort()
		{
			return port;
		}

		public String getProxy()
		{
			return proxy;
		}

		public String getTypeName()
		{
			return typeName;
		}

		public boolean isWap()
		{
			return isWap;
		}

		public void setApn(String s)
		{
			apn = s;
		}

		public void setPort(int i)
		{
			port = i;
		}

		public void setProxy(String s)
		{
			proxy = s;
		}

		public void setTypeName(String s)
		{
			typeName = s;
		}

		public void setWap(boolean flag)
		{
			isWap = flag;
		}

		public NetType()
		{
			apn = "";
			proxy = "";
			typeName = "";
			port = 0;
			isWap = false;
		}
	}


	public NetworkControl()
	{
	}

	public static NetType getNetType(Context context)
	{
		ConnectivityManager connectivitymanager = (ConnectivityManager)context.getSystemService("connectivity");
		NetType nettype = null;
		if (connectivitymanager == null){
			return null;
		}
		NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
		if (networkinfo == null)
		{
			return null;
		}
		String s = networkinfo.getTypeName();
		if (s.equalsIgnoreCase("WIFI"))
		{
			return null; /* Loop/switch isn't completed */
		}
		if (s.equalsIgnoreCase("MOBILE"))
		{
			String s1 = Proxy.getDefaultHost();
			if (s1 != null && !s1.equals(""))
			{
				nettype = new NetType();
				nettype.setProxy(s1);
				nettype.setPort(Proxy.getDefaultPort());
				nettype.setWap(true);
			}
		}
		return nettype;
	}

	public static boolean getNetworkState(Context context)
	{
		boolean flag;
		if (((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo() != null)
			flag = true;
		else
			flag = false;
		return flag;
	}
}
