package com.android.camera;


import android.content.Context;

public class HouseImageDb extends ImageDb {
	public static final String HOUSE_TBL = "house";

	public HouseImageDb(Context c) {
		super(c);
	}


	@Override
	protected String getTableName(){
		return HOUSE_TBL;
	}
}