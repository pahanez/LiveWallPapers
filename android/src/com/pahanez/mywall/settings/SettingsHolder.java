package com.pahanez.mywall.settings;

import android.graphics.Typeface;

import com.badlogic.gdx.graphics.Color;


public class SettingsHolder implements OnSettingsChangedListener{
	private Settings mSettings = Settings.getInstance();
	public static Typeface [] fonts = null;
	public static boolean isRandomTextColor = false;
	public static Color mCustomTextColor;
	public static Color mCustomBackgroundColor;
	public static boolean mIsAnimatedBackground;
	public static int mElementsCount;
	public static int mProcessCount;
	
	public SettingsHolder() {
		onSettingsChanged();
		mSettings.registerOnSettingsChangedListener(this);
	}
	
	@Override
	public void onSettingsChanged() {
		isRandomTextColor = mSettings.isRandomTextColor();
		mIsAnimatedBackground = mSettings.isAnimatedBackground();
		Color textColor = new Color(mSettings.getCustomTextColor());
		mCustomTextColor = new Color(textColor.g, textColor.b, textColor.a, textColor.r);
		Color backgroundColor = new Color(mSettings.getCustomBackgroundColor());
		mCustomBackgroundColor = new Color(backgroundColor.g, backgroundColor.b, backgroundColor.a, backgroundColor.r);
		mElementsCount = mSettings.getElementsCount();
		mProcessCount = mSettings.getProcessQty();
		
	}
	

}
