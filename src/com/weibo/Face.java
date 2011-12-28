package com.weibo;

import java.util.HashMap;

import dibang.com.R;

import android.content.Context;

public class Face {
	public static String[] faceNames=new String[]{"织","围观","威武","奥特曼","爱心传递","围脖","温暖帽子","手套"
			,"雪","雪人","落叶","照相机","浮云","帅","礼物","呵呵","嘻嘻","哈哈","爱你","晕","泪","馋嘴","抓狂","哼","可爱"
			,"怒","汗","害羞","睡觉","钱","偷笑","酷","衰","吃惊","闭嘴","鄙视","挖鼻屎","花心","鼓掌","失望",
			"思考","生病","亲亲","怒骂","太开心","懒得理你","左哼哼","右哼哼","嘘","委屈","吐","可怜","打哈气",
			"握手","耶","good","弱","不要","ok","赞","来","蛋糕","心","伤心","猪头","咖啡","话筒","干杯","绿丝带",
			"蜡烛","钟","微风","月亮","做鬼脸","给力","神马","互粉","萌","熊猫","鸭梨","兔子"};
	
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
//		geili.put("给力", R.drawable.geili_thumb);
//		HashMap<String,Integer> shenma=new HashMap<String, Integer>();
//		shenma.put("神马", R.drawable.horse2_thumb);
//		HashMap<String,Integer> hufen=new HashMap<String, Integer>();
//		hufen.put("互粉", R.drawable.hufen_thumb);
//		HashMap<String,Integer> meng=new HashMap<String, Integer>();
//		meng.put("萌", R.drawable.kawayi_thumb);
//		HashMap<String,Integer> xiongmao=new HashMap<String, Integer>();
//		xiongmao.put("熊猫", R.drawable.panda_thumb);
//		HashMap<String,Integer> yali=new HashMap<String, Integer>();
//		yali.put("鸭梨", R.drawable.pear_thumb);
//		HashMap<String,Integer> tuzi=new HashMap<String, Integer>();
//		tuzi.put("兔子", R.drawable.rabbit_thumb);
		
		faces.put("给力", R.drawable.geili_thumb);
		faces.put("神马", R.drawable.horse2_thumb);
		faces.put("互粉", R.drawable.hufen_thumb);
		faces.put("萌", R.drawable.kawayi_thumb);
		faces.put("熊猫", R.drawable.panda_thumb);
		faces.put("鸭梨", R.drawable.pear_thumb);
		faces.put("兔子", R.drawable.rabbit_thumb);
		
		return faces;
	}
}
