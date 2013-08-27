package com.pahanez.mywall.settings;

import com.badlogic.gdx.graphics.Color;

import android.graphics.Typeface;


public class SettingsHolder implements OnSettingsChangedListener{
	private Settings mSettings = Settings.getInstance();
	public static Typeface [] fonts = null;
	public static boolean isRandomTextColor = false;
	public static Color mCustomTextColor;
	
	public SettingsHolder() {
		onSettingsChanged();
		mSettings.registerOnSettingsChangedListener(this);
	}
	
	@Override
	public void onSettingsChanged() {
		isRandomTextColor = mSettings.isRandomTextColor();
		
		Color c = new Color(mSettings.getCustomTextColor());
		mCustomTextColor = new Color(c.g, c.b, c.a, c.r);
		
	}
	

}
