package com.pahanez.wallpaper.cpu;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.extension.ui.livewallpaper.BaseLiveWallpaperService;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.util.adt.color.Color;
import org.andengine.util.modifier.IModifier.DeepCopyNotSupportedException;
import org.andengine.util.modifier.IModifier.IModifierListener;

import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class CustomWallPaperService extends BaseLiveWallpaperService{ 

	private static int CAMERA_WIDTH = 480;
	private static int CAMERA_HEIGHT = 720;
	private Camera mCamera;
	private Font mFont;
	private Scene mScene;
	@Override
	public EngineOptions onCreateEngineOptions() {
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		 WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
		 wm.getDefaultDisplay().getMetrics(displayMetrics);
		 wm.getDefaultDisplay().getRotation();
		 CAMERA_WIDTH = displayMetrics.widthPixels;
		 CAMERA_HEIGHT = displayMetrics.heightPixels;
		 Log.e("tag",CAMERA_WIDTH + " , " + CAMERA_HEIGHT);
		 this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		  
		 return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,
		 new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
	}
	
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException {
		 
		mFont = FontFactory.create(getFontManager(), getTextureManager(), 512, 512, Typeface.createFromAsset(WallApplication.getContext().getAssets(), "fonts/xspace.ttf"), 42, true, android.graphics.Color.WHITE);
		getFontManager().loadFont(mFont);
		mFont.load();
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {
		mScene = new Scene();
//		Text text = new Text(300, 300, mFont, "Ki12ffs____1241", getVertexBufferObjectManager());
//		text.registerEntityModifier(new AlphaModifier(2, 1, 0));
//		Rectangle rect = new Rectangle(0, 0, 200, 200, getVertexBufferObjectManager());
//		rect.setColor(Color.WHITE);
//		text.setColor(Color.WHITE);
//		mScene.attachChild(text);
//		mScene.attachChild(rect);
		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws IOException {
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}}

