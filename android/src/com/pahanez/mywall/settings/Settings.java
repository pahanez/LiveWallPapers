package com.pahanez.mywall.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.pahanez.mywall.WConstants;
import com.pahanez.mywall.WallApplication;
import com.pahanez.mywall.font.OnFontChangedListener;
import com.pahanez.mywall.utils.WLog;

public class Settings {
	private static final String TAG = Settings.class.getSimpleName();
	
	private static Settings mInstance;
	private SharedPreferences mSharedPrefs;
	private static final String SHARED_PREFS_FILENAME = "time_wall_prefs";
	private static final String RANDOM_COLOR_KEY = "random_color";
	private static final String ANIMATION_BACKGROUND_ENABLED = "animated_background";
	private static final String MIN_TEXTSIZE_KEY = "min_textsize";
	private static final String MAX_TEXTSIZE_KEY = "max_textsize";
	private static final String FRAME_RATE_VALUE = "frame_rate";
	private static final String ELEMENTS_PER_FRAME_VALUE = "elements_per_frame";
	private static final String CUSTOM_COLOR_TEXT_VALUE = "text_color_value";
	private static final String CUSTOM_COLOR_BACKGROUND_VALUE = "background_color_value";
	private static final String CUSTOM_TEXTSIZE_VALUE = "textsize_value";
	private static final String EXTERNAL_FILE_PATH = "file_path";
	private static final String FONT = "font";
	private static final String DATA_TYPE_VALUE = "data_type";
	private static final String FONT_SIZE = "font_value";
	private static final int DEFAULT_FONT_VALUE = 1;
	private static final int DEFAULT_DATA_TYPE_VALUE = 1;
	private static final int DEFAULT_TEXTSIZE_VALUE = 20;
	private static final int DEFAULT_FRAME_RATE_VALUE = 30;
	private static final int DEFAULT_ELEMENTS_PER_FRAME_VALUE = 2;

	
	private OnFontChangedListener mOnFontChangedListener;
	private OnSettingsChangedListener mOnSettingsChangedListener;
	private Settings() {
		mSharedPrefs = WallApplication.getContext().getSharedPreferences(SHARED_PREFS_FILENAME, Context.MODE_PRIVATE);
	}

	public static synchronized Settings getInstance() {
		return (mInstance == null) ? (mInstance = new Settings()) : mInstance;
	}
	
	public void registerOnSettingsChangedListener(OnSettingsChangedListener mOnSettingsChangedListener){
		this.mOnSettingsChangedListener = mOnSettingsChangedListener;
	}
	
	private void removeOnSettingsChangedListener(){
		mOnSettingsChangedListener = null;
	}
	
	public void registerOnFontChangedListener(OnFontChangedListener mOnFontChangedListener){
		this.mOnFontChangedListener = mOnFontChangedListener;
	}
	
	private void removeOnFontChangedListener(){
		mOnFontChangedListener = null;
	}
	
	public void dispose(){
		Gdx.app.log(TAG, "dispose");
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
		mOnSettingsChangedListener.onSettingsChanged();
	}

	public void setCustomTextColor(int value) {
		Editor editor = mSharedPrefs.edit();
		editor.putInt(CUSTOM_COLOR_TEXT_VALUE, value);
		editor.commit();
		mOnSettingsChangedListener.onSettingsChanged();
	}

	public int getCustomTextColor() {
		return mSharedPrefs.getInt(CUSTOM_COLOR_TEXT_VALUE, Color.WHITE.toIntBits());
	}

	public void setCustomBackgroundColor(int value) {
		Editor editor = mSharedPrefs.edit();
		editor.putInt(CUSTOM_COLOR_BACKGROUND_VALUE, value);
		editor.commit();
		mOnSettingsChangedListener.onSettingsChanged();
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
		mOnSettingsChangedListener.onSettingsChanged();
	}

	public void setMinTextSize(int value) {
		Editor editor = mSharedPrefs.edit();
		editor.putInt(MIN_TEXTSIZE_KEY, value);
		editor.commit();
	}

	public void setMaxTextSize(int value) {
		Editor editor = mSharedPrefs.edit();
		editor.putInt(MAX_TEXTSIZE_KEY, value);
		editor.commit();
	}

	public float getMinTextSize() {
		return mSharedPrefs.getInt(MIN_TEXTSIZE_KEY, DEFAULT_TEXTSIZE_VALUE);
	}

	public float getMaxTextSize() {
		return mSharedPrefs.getInt(MAX_TEXTSIZE_KEY, DEFAULT_TEXTSIZE_VALUE);
	}

	public void setFrameRate(int value) {
		Editor editor = mSharedPrefs.edit();
		editor.putInt(FRAME_RATE_VALUE, value);
		editor.commit();
	}

	public int getFrameRate() {
		if (mSharedPrefs.getInt(FRAME_RATE_VALUE, DEFAULT_FRAME_RATE_VALUE) == 0)
			return 1;
		return mSharedPrefs.getInt(FRAME_RATE_VALUE, DEFAULT_FRAME_RATE_VALUE);
	}

	public void setElementsPerFrame(int value) {
		Editor editor = mSharedPrefs.edit();
		editor.putInt(ELEMENTS_PER_FRAME_VALUE, value);
		editor.commit();
	}

	public int getElementsPerFrame() {
		if (mSharedPrefs.getInt(ELEMENTS_PER_FRAME_VALUE, DEFAULT_ELEMENTS_PER_FRAME_VALUE) == 0)
			return 1;
		return mSharedPrefs.getInt(ELEMENTS_PER_FRAME_VALUE, DEFAULT_ELEMENTS_PER_FRAME_VALUE);
	}

	public void setDataType(int value) {
		Editor editor = mSharedPrefs.edit();
		editor.putInt(DATA_TYPE_VALUE, value);
		editor.commit();
	}

	public int getDataTypeValue() {
		return mSharedPrefs.getInt(DATA_TYPE_VALUE, DEFAULT_DATA_TYPE_VALUE);
	}

	public int getFontSize() {
		return mSharedPrefs.getInt(FONT_SIZE, DEFAULT_FONT_VALUE);
	}

	public void setFontSize(int value) {
		Editor editor = mSharedPrefs.edit();
		editor.putInt(FONT_SIZE, value);
		editor.commit();
		mOnFontChangedListener.fontChanged();
	
	}
	
	public void setFont(int font) {
		Editor editor = mSharedPrefs.edit();
		editor.putInt(FONT, font);
		editor.commit();
		mOnFontChangedListener.fontChanged();
	}

	public int getFont() {
		return mSharedPrefs.getInt(FONT, 0);
	}

}
