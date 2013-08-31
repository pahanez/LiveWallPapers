package com.pahanez.mywall.font;

import java.util.concurrent.atomic.AtomicReference;

import android.app.Dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.pahanez.mywall.MainExecutor;
import com.pahanez.mywall.WConstants;
import com.pahanez.mywall.settings.Settings;

public class CustomFont implements OnFontChangedListener {

	//public static final String[] mFontsId = { "xs", "s", "m", "l", "xl" };
	public static final float[] mFontsFactor = { 0.125F, 0.175F, 0.3F, 0.6F, 1.0F };
	private int size;
	private int file;
	private Settings mSettings = Settings.getInstance();

	private AtomicReference<BitmapFont> mCustomFontReference = new AtomicReference<BitmapFont>();
	float one_cm = Gdx.graphics.getPpcY();

	public CustomFont() {
		createFont();

		mSettings.registerOnFontChangedListener(this);

	}

	public BitmapFont getmCustomFont() {
		return mCustomFontReference.get();
	}

	public void setmCustomFont(BitmapFont mCustomFont) {
		mCustomFontReference.set(mCustomFont);
	}

	private int getSize() {
		return size;
	}

	@Override
	public void fontChanged(final Dialog d) {
		MainExecutor.getInstance().execute(new Runnable() {

			@Override
			public void run() {
					createFont();
					d.dismiss();
			}
		});
	}

	private void createFont() {
		size = mSettings.getFontSize();
		file = mSettings.getFont();
		
		
		FileHandle fontFile = Gdx.files.internal(WConstants.FONTS_FOLDER + WConstants.FONT_FILES[file]);
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
		BitmapFont mustomFont = generator.generateFont((int) (mFontsFactor[getSize()] * one_cm));
		generator.dispose();
		
		setmCustomFont(mustomFont);
		
	}

}
