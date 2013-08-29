package com.pahanez.mywall.font;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.pahanez.mywall.MainExecutor;
import com.pahanez.mywall.WConstants;
import com.pahanez.mywall.settings.Settings;
import com.pahanez.mywall.utils.WLog;

public class CustomFont implements OnFontChangedListener {

	public static final String[] mFontsId = { "xs", "s", "m", "l", "xl" };
	public static final float[] mFontsFactor = { 0.125F, 0.25F, 0.5F, 1, 1.5F };
	private int size;
	private int file;
	private Settings mSettings = Settings.getInstance();

	private BitmapFont mCustomFont;
	float one_cm = Gdx.graphics.getPpcY();

	public CustomFont() {
		createFont();

		mSettings.registerOnFontChangedListener(this);

	}

	public synchronized BitmapFont getmCustomFont() {
		return mCustomFont;
	}

	public void setmCustomFont(BitmapFont mCustomFont) {
		this.mCustomFont = mCustomFont;
	}

	private int getSize() {
		return size;
	}

	@Override
	public void fontChanged() {
		MainExecutor.getInstance().execute(new Runnable() {

			@Override
			public void run() {
				
					createFont();
			}
		});
	}

	private synchronized void createFont() {
		size = mSettings.getFontSize();
		file = mSettings.getFont();
		
		
		FileHandle fontFile = Gdx.files.internal(WConstants.FONTS_FOLDER + WConstants.FONT_FILES[file]);
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
		mCustomFont = generator.generateFont((int) (mFontsFactor[getSize()] * one_cm));
		generator.dispose();
		
		try { 
			TimeUnit.SECONDS.sleep(1); //wtf
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
