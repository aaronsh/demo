package com.weibo;

import java.util.HashMap;

import dibang.com.R;

import android.content.Context;

public class Face {
	public static String[] faceNames=new String[]{"֯","Χ��","����","������","���Ĵ���","Χ��","��ůñ��","����"
			,"ѩ","ѩ��","��Ҷ","�����","����","˧","����","�Ǻ�","����","����","����","��","��","����","ץ��","��","�ɰ�"
			,"ŭ","��","����","˯��","Ǯ","͵Ц","��","˥","�Ծ�","����","����","�ڱ�ʺ","����","����","ʧ��",
			"˼��","����","����","ŭ��","̫����","��������","��ߺ�","�Һߺ�","��","ί��","��","����","�����",
			"����","Ү","good","��","��Ҫ","ok","��","��","����","��","����","��ͷ","����","��Ͳ","�ɱ�","��˿��",
			"����","��","΢��","����","������","����","����","����","��","��è","Ѽ��","����"};
	
	private static HashMap<String,Integer> faces;
	
	public static HashMap<String,Integer> getfaces(Context context){
		if(faces!=null){
			return faces;
		}
		faces=new HashMap<String, Integer>();
		String faceName="";
		for(int i=217;i<=290;i++){
			faceName="face"+i;
			try {
				int id=R.drawable.class.getDeclaredField(faceName).getInt(context);
				faces.put(faceNames[i-217], id);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
//		HashMap<String,Integer> geili=new HashMap<String, Integer>();
//		geili.put("����", R.drawable.geili_thumb);
//		HashMap<String,Integer> shenma=new HashMap<String, Integer>();
//		shenma.put("����", R.drawable.horse2_thumb);
//		HashMap<String,Integer> hufen=new HashMap<String, Integer>();
//		hufen.put("����", R.drawable.hufen_thumb);
//		HashMap<String,Integer> meng=new HashMap<String, Integer>();
//		meng.put("��", R.drawable.kawayi_thumb);
//		HashMap<String,Integer> xiongmao=new HashMap<String, Integer>();
//		xiongmao.put("��è", R.drawable.panda_thumb);
//		HashMap<String,Integer> yali=new HashMap<String, Integer>();
//		yali.put("Ѽ��", R.drawable.pear_thumb);
//		HashMap<String,Integer> tuzi=new HashMap<String, Integer>();
//		tuzi.put("����", R.drawable.rabbit_thumb);
		
		faces.put("����", R.drawable.geili_thumb);
		faces.put("����", R.drawable.horse2_thumb);
		faces.put("����", R.drawable.hufen_thumb);
		faces.put("��", R.drawable.kawayi_thumb);
		faces.put("��è", R.drawable.panda_thumb);
		faces.put("Ѽ��", R.drawable.pear_thumb);
		faces.put("����", R.drawable.rabbit_thumb);
		
		return faces;
	}
}
