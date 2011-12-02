package com.android.camera;


import android.content.Context;

public class EffectImageDb extends ImageDb {
	public static final String EFFECT_TBL = "effect";

	public EffectImageDb(Context c) {
		super(c);
	}


	@Override
	protected String getTableName(){
		return EFFECT_TBL;
	}
}