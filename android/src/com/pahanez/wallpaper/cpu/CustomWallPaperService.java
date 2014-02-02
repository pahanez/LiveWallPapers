package com.pahanez.wallpaper.cpu;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService;
import com.pahanez.wallpaper.cpu.gdx.WallScene;

public class CustomWallPaperService extends AndroidLiveWallpaperService {
	public Engine onCreateEngine() {
		return new AndroidWallpaperEngine(){
			@Override
			public void onVisibilityChanged(boolean visible) {
				MainExecutor.getInstance().setVisible(visible);
				super.onVisibilityChanged(visible);
			}
			
		};
	};
	
	@Override
	public void onCreateApplication() { 
		super.onCreateApplication();
		
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = true;
		initialize(new WallScene(), cfg);
	}
	
	

}