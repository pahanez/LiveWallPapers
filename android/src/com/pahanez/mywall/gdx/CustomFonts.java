package com.pahanez.mywall.gdx;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.pahanez.mywall.WallController;
import com.pahanez.mywall.WallController.OnFontsLoadedCallback;
import com.pahanez.mywall.settings.Settings;
import com.pahanez.mywall.utils.WLog;

public class CustomFonts implements OnFontsLoadedCallback { //TODO перепись
	private static final String TAG = CustomFonts.class.getSimpleName();
	public static final String[] mFontsId = { "xs", "s", "m", "l", "xl" };
	public static final float[] mFontsFactor = { 0.125F, 0.25F, 0.5F, 1, 2 };
	private HashMap<String, BitmapFont> mFontsMap;
	public CustomFonts() {
		mFontsMap = generateFonts();
	}

	public BitmapFont getFont(int size) {
			if (mFontsMap != null) {
				synchronized (this) {
				return mFontsMap.get(mFontsId[size]);
			}
		}
		return null;
	}
	
	public void setNewFontAssync(){
		WallController.getInstance().generateFonts(this);
	}

	@Override
	public void fontsLoaded(HashMap<String, BitmapFont> map) {
		synchronized (this) {
			mFontsMap = map;
		}
	}
	
	private HashMap<String, BitmapFont> generateFonts() {
		HashMap<String, BitmapFont> mFontsMap = new HashMap<String, BitmapFont>();
		String fontName = "fonts/merchant.ttf";
		FileHandle fontFile;
		WLog.e(fontName);
		if (fontName == null)
			fontFile = Gdx.files.internal("fonts/merchant.ttf");
		else
			fontFile = Gdx.files.internal("fonts/" + fontName);
		float one_cm = Gdx.graphics.getPpcY();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
		long leg = System.currentTimeMillis();
		for (int i = 0; i < CustomFonts.mFontsId.length; i++) {
			WLog.e("p37td8", "res 150 : " + (System.currentTimeMillis() - leg));
			mFontsMap.put(CustomFonts.mFontsId[i],
					generator.generateFont(((int) (one_cm * CustomFonts.mFontsFactor[i]))));
		}
		WLog.e("p37td8", "pre 150 : " + mFontsMap.size());
		generator.dispose();
		
		return mFontsMap;
	}

}
