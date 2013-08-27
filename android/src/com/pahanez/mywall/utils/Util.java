package com.pahanez.mywall.utils;

import android.graphics.Typeface;

import com.pahanez.mywall.WallApplication;

public class Util {
	public static Typeface getCustomTypeface(String typefaceName) {
		return Typeface.createFromAsset(WallApplication.getContext().getAssets(), "fonts/" + typefaceName);
	}
}
