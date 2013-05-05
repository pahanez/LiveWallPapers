package com.pahanez.mywall.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;

import com.pahanez.mywall.WallApplication;

public class Settings {
	private static Settings mInstance;
	private SharedPreferences mSharedPrefs;
	private static final String SHARED_PREFS_FILENAME = "time_wall_prefs";
	private static final String RANDOM_COLOR_KEY = "random_color";
	private static final String RANDOM_TEXTSIZE_KEY = "random_textsize";
	private static final String CUSTOM_COLOR_VALUE = "color_value";
	private static final String CUSTOM_TEXTSIZE_VALUE = "textsize_value";
	private static final int DEFAULT_TEXTSIZE_VALUE = 20;

	private Settings() {
		mSharedPrefs = WallApplication.getContext().getSharedPreferences(
				SHARED_PREFS_FILENAME, Context.MODE_PRIVATE);
	}

	public static synchronized Settings getInstance() {
		return (mInstance == null) ? (mInstance = new Settings()) : mInstance;
	}

	public boolean isRandomColor() {
		return mSharedPrefs.getBoolean(RANDOM_COLOR_KEY, true);
	}

	public void setRandomColor(boolean value) {
		Editor editor = mSharedPrefs.edit();
		editor.putBoolean(RANDOM_COLOR_KEY, value);
		editor.commit();
	}
	
	public int getCustomColor(){
		return mSharedPrefs.getInt(CUSTOM_COLOR_VALUE,Color.WHITE);
	}
	
	public boolean isRandomTextSize(){
		return mSharedPrefs.getBoolean(RANDOM_TEXTSIZE_KEY, true);
	}
	
	public void setRandomTextSize(boolean value){
		Editor editor = mSharedPrefs.edit();
		editor.putBoolean(RANDOM_TEXTSIZE_KEY, value);
		editor.commit();
	}
	
	public float getCustomTextSize(){
		return mSharedPrefs.getInt(CUSTOM_TEXTSIZE_VALUE , DEFAULT_TEXTSIZE_VALUE);
	}
}
