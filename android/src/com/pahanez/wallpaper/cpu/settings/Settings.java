package com.pahanez.wallpaper.cpu.settings;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.badlogic.gdx.graphics.Color;
import com.pahanez.wallpaper.cpu.WConstants;
import com.pahanez.wallpaper.cpu.WallApplication;
import com.pahanez.wallpaper.cpu.font.OnFontChangedListener;

public class Settings {
	private static Settings mInstance;
	private SharedPreferences mSharedPrefs;
	private static final String SHARED_PREFS_FILENAME = "time_wall_prefs";
	private static final String RANDOM_COLOR_KEY = "random_color";
	private static final String ANIMATION_BACKGROUND_ENABLED = "animated_background";
	private static final String PROCESS_QTY_VALUE = "process_qty";
	private static final String ELEMENTS_PER_FRAME_VALUE = "elements_per_frame";
	private static final String CUSTOM_COLOR_TEXT_VALUE = "text_color_value";
	private static final String CUSTOM_COLOR_BACKGROUND_VALUE = "background_color_value";
	private static final String FONT = "font";
	private static final String FONT_SIZE = "font_value";
	private static final int DEFAULT_FONT_VALUE = 1;
	private static final int DEFAULT_PROCESS_QTY_VALUE = 10;
	private static final int DEFAULT_ELEMENTS_PER_FRAME_VALUE = 2;

	
	private OnFontChangedListener mOnFontChangedListener;
	private ArrayList<OnSettingsChangedListener> mSettingsChangedListeners = new ArrayList<OnSettingsChangedListener>();
	private Settings() {
		mSharedPrefs = WallApplication.getContext().getSharedPreferences(SHARED_PREFS_FILENAME, Context.MODE_PRIVATE);
	}

	public static synchronized Settings getInstance() {
		return (mInstance == null) ? (mInstance = new Settings()) : mInstance;
	}
	
	public void registerOnSettingsChangedListener(OnSettingsChangedListener mOnSettingsChangedListener){
		synchronized (mSettingsChangedListeners) {
			mSettingsChangedListeners.add(mOnSettingsChangedListener);
		}
	}
	
	public void notifyOnSettingsChangedListeners(){
		synchronized (mSettingsChangedListeners) {
			for(OnSettingsChangedListener listener:mSettingsChangedListeners) listener.onSettingsChanged();
		}
	}
	
	private void removeOnSettingsChangedListener(){
		mSettingsChangedListeners.clear();
		mSettingsChangedListeners = null;
	}
	
	public void registerOnFontChangedListener(OnFontChangedListener mOnFontChangedListener){
		this.mOnFontChangedListener = mOnFontChangedListener;
	}
	
	private void removeOnFontChangedListener(){
		mOnFontChangedListener = null;
	}
	
	public void dispose(){
		removeOnFontChangedListener();
		removeOnSettingsChangedListener();
	}

	boolean isRandomTextColor() {
		return mSharedPrefs.getBoolean(RANDOM_COLOR_KEY, true);
	}

	void setRandomTextColor(boolean value) {
		Editor editor = mSharedPrefs.edit();
		editor.putBoolean(RANDOM_COLOR_KEY, value);
		editor.commit();
		notifyOnSettingsChangedListeners();
	}

	public void setCustomTextColor(int value) {
		Editor editor = mSharedPrefs.edit();
		editor.putInt(CUSTOM_COLOR_TEXT_VALUE, value);
		editor.commit();
		notifyOnSettingsChangedListeners();
	}

	public int getCustomTextColor() {
		return mSharedPrefs.getInt(CUSTOM_COLOR_TEXT_VALUE, Color.WHITE.toIntBits());
	}

	public void setCustomBackgroundColor(int value) {
		Editor editor = mSharedPrefs.edit();
		editor.putInt(CUSTOM_COLOR_BACKGROUND_VALUE, value);
		editor.commit();
		notifyOnSettingsChangedListeners();
	}

	public int getCustomBackgroundColor() {
		return mSharedPrefs.getInt(CUSTOM_COLOR_BACKGROUND_VALUE, WConstants.DEFAULT_BACKGROUND_COLOR);
	}

	public boolean isAnimatedBackground() {
		return mSharedPrefs.getBoolean(ANIMATION_BACKGROUND_ENABLED, true);
	}

	public void setAnimatedBackground(boolean value) {
		Editor editor = mSharedPrefs.edit();
		editor.putBoolean(ANIMATION_BACKGROUND_ENABLED, value);
		editor.commit();
		notifyOnSettingsChangedListeners();
	}

	public void setProcessQty(int value) {
		Editor editor = mSharedPrefs.edit();
		editor.putInt(PROCESS_QTY_VALUE, value);
		editor.commit();  
		notifyOnSettingsChangedListeners();
	}

	public int getProcessQty	() {
		if (mSharedPrefs.getInt(PROCESS_QTY_VALUE, DEFAULT_PROCESS_QTY_VALUE) == 0)
			return 1;
		return mSharedPrefs.getInt(PROCESS_QTY_VALUE, DEFAULT_PROCESS_QTY_VALUE);
	}

	public void setElementsCount(int value) {
		Editor editor = mSharedPrefs.edit();
		editor.putInt(ELEMENTS_PER_FRAME_VALUE, value);
		editor.commit();
		notifyOnSettingsChangedListeners();
	}

	public int getElementsCount() {
		if (mSharedPrefs.getInt(ELEMENTS_PER_FRAME_VALUE, DEFAULT_ELEMENTS_PER_FRAME_VALUE) == 0)
			return 1;
		return mSharedPrefs.getInt(ELEMENTS_PER_FRAME_VALUE, DEFAULT_ELEMENTS_PER_FRAME_VALUE);
	}

	public int getFontSize() {
		return mSharedPrefs.getInt(FONT_SIZE, DEFAULT_FONT_VALUE);
	}

	public void setFontSize(int value) {
		Editor editor = mSharedPrefs.edit();
		editor.putInt(FONT_SIZE, value);
		editor.commit();
		mOnFontChangedListener.fontChanged(value);
	
	}
	
	public void setFont(int font, Dialog d) {
		Editor editor = mSharedPrefs.edit();
		editor.putInt(FONT, font);
		editor.commit();
		mOnFontChangedListener.fontChanged(font);
	}

	public int getFont() {
		return mSharedPrefs.getInt(FONT, 0);
	}

}
