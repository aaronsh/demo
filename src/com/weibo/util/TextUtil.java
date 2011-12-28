package com.weibo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;

import com.weibo.Face;

public class TextUtil {
	/**
	 * ���� @****�� �� @****: ��@****[�ո�] <br>
	 * Html.fromHtml(String) *
	 * 
	 * @param str
	 * @return
	 */
	public static Spanned highLight(String str) {
		Pattern pattern = Pattern.compile("@[^\\s:��]+[:��\\s]");
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			String m = matcher.group();
			str = str.replace(m, "<font color=Navy>" + m + "</font>");
		}
		return Html.fromHtml(str);
	}

	/**
	 * ���� @****�� �� @****: ��@****[�ո�] <br>
	 * SpannableString.setSpan(Object what, int start, int end, int flags)
	 * 
	 * @param text
	 * @return
	 */
	// �����hightLight(String str) ��Ч�ʶԱ�?
	public static SpannableString light(CharSequence text) {
		SpannableString spannableString = new SpannableString(text);
		Pattern pattern = Pattern.compile("@[^\\s:��]+[:��\\s]");
		Matcher matcher = pattern.matcher(spannableString);
		while (matcher.find()) {
			spannableString.setSpan(new ForegroundColorSpan(Color.CYAN),
					matcher.start(), matcher.end(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return spannableString;
	}

	/**
	 * ��ʽ������ <br>
	 * ���ڱ���΢��ͼ�񣬽�ȡurl�����һ����Ϊͼ���ļ���
	 * 
	 * @param url
	 * @return
	 */
	public static String formatName(String url) {
		if (url == null || "".equals(url)) {
			return url;
		}
		int start = url.lastIndexOf("/");
		int end = url.lastIndexOf(".");
		if (start == -1 || end == -1) {
			return url;
		}
		return url.substring(start + 1, end);
	}

	/**
	 * ��ʽ����Դ
	 * 
	 * @param name
	 * @return
	 */
	public static String formatSource(String name) {
		if (name == null || "".equals(name)) {
			return name;
		}
		int start = name.indexOf(">");
		int end = name.lastIndexOf("<");
		if (start == -1 || end == -1) {
			return name;
		}
		return name.substring(start + 1, end);
	}
	/**
	 * ƥ����� �����������ĵ��ֱ���δ����ȷ��ʾ��
	 * @param text
	 * @param context
	 * @return
	 */
	public static SpannableString formatImage(CharSequence text, Context context) {
		SpannableString spannableString = new SpannableString(text);
		Pattern pattern = Pattern.compile("\\[[^0-9]{1,4}\\]"); //����ʽ�������������ĵ��ֱ��鶼�޷���ȷ�滻
		Matcher matcher = pattern.matcher(spannableString);
		while (matcher.find()) {
			String faceName = matcher.group();
			String key = faceName.substring(1, faceName.length() - 1);
			if (Face.getfaces(context).containsKey(key)) {
				spannableString.setSpan(new ImageSpan(context, Face.getfaces(
						context).get(key)), matcher.start(), matcher.end(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return spannableString;
	}
	/**
	 * ��text��@ĳ�˵����������ƥ��ı��������Ա�����ʾ
	 * @param text
	 * @param context
	 * @return
	 */
	public static SpannableString formatContent(CharSequence text,Context context) {
		SpannableString spannableString = new SpannableString(text);
		/*
		 * @[^\\s:��]+[:��\\s] ƥ��@ĳ��  \\[[^0-9]{1,4}\\] ƥ�����
		 */
		Pattern pattern = Pattern.compile("@[^\\s:��]+[:��\\s]|\\[[^0-9]{1,4}\\]");
		Matcher matcher = pattern.matcher(spannableString);
		while (matcher.find()) {
			String match=matcher.group();
			if(match.startsWith("@")){ //@ĳ�ˣ���������
				spannableString.setSpan(new ForegroundColorSpan(0xff0077ff),
						matcher.start(), matcher.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}else if(match.startsWith("[")){ //����
				String key = match.substring(1, match.length() - 1);
				if (Face.getfaces(context).containsKey(key)) {
					spannableString.setSpan(new ImageSpan(context, Face.getfaces(
							context).get(key)), matcher.start(), matcher.end(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
		}
		return spannableString;
	}
	static void Log(String msg){
		Log.i("weibo", "TextUtil--"+msg);
	}
}
