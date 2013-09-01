package com.pahanez.wallpaper.cpu.utils;

import android.graphics.Typeface;

import com.pahanez.wallpaper.cpu.WallApplication;

public class Util {
	public static Typeface getCustomTypeface(String typefaceName) {
		return Typeface.createFromAsset(WallApplication.getContext().getAssets(), "fonts/" + typefaceName);
	}
}
