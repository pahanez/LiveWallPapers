package com.pahanez.wallpaper.cpu.font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import android.app.Dialog;
import android.graphics.BlurMaskFilter;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.pahanez.wallpaper.cpu.MainExecutor;
import com.pahanez.wallpaper.cpu.WConstants;
import com.pahanez.wallpaper.cpu.WallApplication;
import com.pahanez.wallpaper.cpu.settings.Settings;
import com.pahanez.wallpaper.cpu.utils.WLog;

public class CustomFont implements OnFontChangedListener {

	//public static final String[] mFontsId = { "xs", "s", "m", "l", "xl" };
	public static final float[] mFontsFactor = { 0.125F, 0.175F, 0.3F, 0.6F, 1.0F };
	private int size;
	private int file;
	private Settings mSettings = Settings.getInstance();
	private HashMap<String, ArrayList<BitmapFont>> mFonts = new HashMap<String, ArrayList<BitmapFont>>();

	private AtomicReference<BitmapFont> mCustomFontReference = new AtomicReference<BitmapFont>();
	float one_cm = Gdx.graphics.getPpcY();

	public CustomFont() {
		createFont();

		mSettings.registerOnFontChangedListener(this);
		/*MainExecutor.getInstance().execute(new Runnable() {
			
			@Override
			public void run() {
				long time = System.currentTimeMillis();
				generateAllFonts();
				WLog.e(""+(System.currentTimeMillis() - time) + " , " + mFonts.size());
			}
		}); */
		WLog.e("!!!!!!!!!!!START!!!!!!!!!!!!!!!!!!");

	}

	public BitmapFont getmCustomFont() {
		return mCustomFontReference.get();
	}

	public void setmCustomFont(BitmapFont mCustomFont) {
		if(mCustomFontReference.get()!= null)
			mCustomFontReference.get().dispose();
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
		setmCustomFont(mustomFont);
		generator.dispose();
		
	}
	
	public void generateAllFonts(){
		for (int i = 0; i < WConstants.FONT_FILES.length; i++) {
			FileHandle fontFile = Gdx.files.internal(WConstants.FONTS_FOLDER + WConstants.FONT_FILES[i]);
			FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
			ArrayList<BitmapFont> list = new ArrayList<BitmapFont>();
			for (int j = 0; j < mFontsFactor.length; j++) {
				BitmapFont customFont = generator.generateFont((int) (mFontsFactor[j] * one_cm));
				list.add(customFont);
			}
			mFonts.put(WConstants.FONT_FILES[i], list);
			generator.dispose();
		}
	}

}
