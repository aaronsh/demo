package dibang.com.web;

import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import dibang.com.Const;

public class IOFile {
	public static String FormetFileSize(long paramLong) {
		DecimalFormat localDecimalFormat = new DecimalFormat("#.00");
		String str2;
		if (paramLong < 1024L) {
			double d1 = paramLong;
			str2 = localDecimalFormat.format(d1) + "B";
		} else if (paramLong < 1048576L) {
			double d2 = paramLong / 1024.0D;
			str2 = localDecimalFormat.format(d2) + "K";
		} else if (paramLong < 1073741824L) {
			double d3 = paramLong / 1048576.0D;
			str2 = localDecimalFormat.format(d3) + "M";
		} else {
			double d4 = paramLong / 1073741824.0D;
			str2 = localDecimalFormat.format(d4) + "G";
		}

		return str2;
	}

	public static void createFolder(File paramFile) throws Exception {
		try {
			if (paramFile.isDirectory())
				return;
			boolean bool = paramFile.mkdir();
			return;
		} catch (Exception localException) {
		}
		throw new Exception("�½�Ŀ¼����");
	}

	public static void createFile(String file) throws Exception {
		File f = new File(file);
		if (f.exists())
			return;
		File p = f.getParentFile();
		if (!p.exists())
			p.mkdirs();
		f.createNewFile();
		return;
	}

	public static String createSDCardPath(String paramString) {
		return "/sdcard/" + paramString + "/";
	}

	public static void deleteFile(File paramFile) throws Exception {
		try {
			if (paramFile.isDirectory()) {
				File[] arrayOfFile = paramFile.listFiles();
				int size = arrayOfFile.length;
				int j = 0;
				for (; j < size; j++) {
					deleteFile(arrayOfFile[j]);
				}
			}
			paramFile.delete();
			return;
		} catch (Exception localException) {
		}
		throw new Exception("�ļ�ɾ��ʧ��");
	}

	public static String getFileName(String paramString) {
		int i = paramString.lastIndexOf("/") + 1;
		return paramString.substring(i);
	}

	public static long getFileSize(File paramFile) throws Exception {
		long l = 0L;
		if (paramFile.exists())
			l = new FileInputStream(paramFile).available();
		else {
			paramFile.createNewFile();
		}
		return l;
	}

	public static long getFolderSize(File paramFile) throws Exception {
		long totalSize = 0;
		try {
			File[] arrayOfFile = paramFile.listFiles();
			for (int i = 0; i < arrayOfFile.length; i++) {
				if (arrayOfFile[i].isDirectory()) {
					totalSize = totalSize + getFolderSize(arrayOfFile[i]);
				} else {
					totalSize = totalSize + getFileSize(arrayOfFile[i]);
				}
			}
			return totalSize;

		} catch (Exception localException) {
		}
		throw new Exception("ȡ�ļ��д���");
	}

	public static String getUserFolder(String s) {
		return s.toLowerCase().replace("http://192.168.1.200/blog/images/", "")
				.replace("http://www.bangso.com/blog/images/", "")
				.replace("/facepic/s_e8375d7cd983efcbf956da5937050ffc.jpg", "")
				.replace("/", "");
	}

	public static long getlist(File paramFile) {
		File[] arrayOfFile = paramFile.listFiles();
		long count = 0;
		for (int i = 0; i < arrayOfFile.length; i++) {
			if (arrayOfFile[i].isDirectory()) {
				count = count + getlist(arrayOfFile[i]);
			} else {
				count++;
			}
		}
		return count;
	}

	public static ArrayList<String> getFileList(String path) {
		ArrayList<String> list = new ArrayList<String>();
		File p = new File(path);
		File[] files = p.listFiles();
		if (files != null) {
			for (File f : files) {
				list.add(f.getPath());
			}
		}
		return list;
	}

	public static Boolean sdcardExist() {
		if (Environment.getExternalStorageState().equals("mounted")) {
			return true;
		}
		return false;
	}

	public static String getSdcardPath() {
		File sdcard = Environment.getExternalStorageDirectory();
		return sdcard.getPath();
	}
	
	public static String getModuleFolder(String module)
	{
		StringBuilder b = new StringBuilder();
		b.append(IOFile.getSdcardPath());
		b.append("/");
		b.append(Const.APP_PATH);
		b.append("/");
		b.append(module);
		return b.toString();
	}
}

/*
 * Location: E:\workspace\examples\apk-tools\dex2jar\classes_dex2jar.jar
 * Qualified Name: COM.Bangso.FunctionUtility.IOFile JD-Core Version: 0.6.0
 */