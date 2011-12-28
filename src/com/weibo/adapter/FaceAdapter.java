package com.weibo.adapter;

import java.util.HashMap;

import com.weibo.Face;

import dibang.com.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class FaceAdapter extends BaseAdapter{
	private Context context;
	private String[] faceNames;
	private HashMap<String,Integer> faces;
	
	public FaceAdapter(Context context,String[] faceNames){
		this.context=context;
		this.faceNames=faceNames;
		this.faces=Face.getfaces(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return faceNames==null?0:faceNames.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView iv = null;
		if(convertView == null){
			convertView = View.inflate(context, R.layout.imageview, null);
			iv = (ImageView)convertView.findViewById(R.id.imageview_iv);
			
			convertView.setTag(iv);
		}else {
			iv = (ImageView) convertView.getTag();
		}
		if(faces.containsKey(Face.faceNames[position])){
			iv.setImageResource(faces.get(Face.faceNames[position]));
		}
		return convertView;
	}

}
