package com.pahanez.wallpaper.cpu.gdx;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.util.ArrayList;
import java.util.Random;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidWallpaperListener;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pahanez.wallpaper.cpu.cpu.TopParser;
import com.pahanez.wallpaper.cpu.font.OnFontChangedListener;
import com.pahanez.wallpaper.cpu.settings.OnSettingsChangedListener;
import com.pahanez.wallpaper.cpu.settings.Settings;
import com.pahanez.wallpaper.cpu.settings.SettingsHolder;

public class WallScene implements ApplicationListener, AndroidWallpaperListener,OnSettingsChangedListener, OnFontChangedListener {


	private Stage mStage;
	public final static int DEFAULT_FRAME_INTERVAL = 60;
//	private CustomFont mCustomFont ;
	private BitmapFont font;
	private Random mRandom = new Random();
	private int mWidth;
	private int mHeight;
	private ArrayList<CustomActor> mActors = new ArrayList<CustomActor>();
	private TopParser mTopParser = new TopParser();
	private ShaderProgram mShaderProgram;
	private SpriteBatch mSpriteBatch;
	private CustomRenderer mBackgroundRenderer;
	private Texture mTexture;
	private float walk;

	final String VERT = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" + "\n" + "uniform mat4 u_projTrans;" + "\n" + "void main() {\n" + "\n" + "	gl_Position =  u_projTrans * " + "\n" + ShaderProgram.POSITION_ATTRIBUTE + ";\n" + "}";
	final String FRAG = "#ifdef GL_ES" + " \n" + "precision highp float;" + " \n" + "#endif" + " \n" + "uniform float time;" + " \n" + "uniform vec2 resolution;" + " \n" + "void main(void){" + " \n" + "	vec2 position = gl_FragCoord.xy / max(resolution.x,resolution.y) ;" + " \n" + "	position.y-=time;" + " \n" + "	float g = cos(position.y * 75.0)*0.4 ;" + " \n" + "	vec3 col=vec3(0,g,0);" + " \n" + "	gl_FragColor=vec4(col, 1.0);" + " \n" + "}";

	@Override
	public void render() {

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		int deltaTime = (int) (Gdx.graphics.getDeltaTime() * 1000);
		if (deltaTime > DEFAULT_FRAME_INTERVAL)
			deltaTime = DEFAULT_FRAME_INTERVAL;
		int timeToSleep = DEFAULT_FRAME_INTERVAL - deltaTime;
		if (timeToSleep > 0) {
			try {
				Thread.sleep(timeToSleep);
			} catch (InterruptedException e) {

			}
		} 
		mBackgroundRenderer.render();
			
		
		mStage.act(); 
		mStage.draw();

	}

	@Override
	public void previewStateChange(boolean isPreview) {

	}

	private class CustomActor extends Actor {
		private String value;
		public void setValue(String str){
			value = new String(str);
		}
		@Override
		public void draw(SpriteBatch batch, float parentAlpha) {
			super.draw(batch, parentAlpha);
//			mCustomFont.getmCustomFont().setColor(getColor());
			font.setColor(getColor());
			font.draw(batch, value, getX() - mWidth/4, getY());
//			mCustomFont.getmCustomFont().draw();
		}

	}

	int mLegacyX;

	@Override
	public void offsetChange(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
		if (mStage != null)
			for (Actor actor : mStage.getActors())
				actor.setPosition(actor.getX() + (mLegacyX - xPixelOffset), actor.getY());
		mLegacyX = xPixelOffset;
	}

	@Override
	public void create() {

		mWidth = Gdx.graphics.getWidth();
		mHeight = Gdx.graphics.getHeight();
		
//		mCustomFont = new CustomFont();
		font = new BitmapFont();
		font.setScale(3);
		new SettingsHolder();

		mStage = new Stage(mWidth, mHeight, true);
		
		Settings.getInstance().registerOnSettingsChangedListener(this);
		Settings.getInstance().registerOnFontChangedListener(this);
		
		onSettingsChanged();
		ShaderProgram.pedantic = false;
		mShaderProgram = new ShaderProgram(VERT, FRAG);
		if (mShaderProgram.isCompiled() == false) {
			Gdx.app.exit();
		}
		mSpriteBatch = new SpriteBatch();
		mSpriteBatch.setShader(mShaderProgram);
		mTexture = new Texture(new Pixmap(mWidth, mHeight, Format.RGBA8888));

	}

	private void initActorList() {
		if(!mActors.isEmpty()) {
			for(Actor actor:mActors){
				actor.clear();
				mStage.clear();
				actor = null;
			}
			mActors.clear();
		}
		
		for (int i = 0; i < SettingsHolder.mElementsCount; i++) {
			mActors.add(new CustomActor());
		}
		
		for (Actor actor : mActors) {
			updateActorState(mActors.indexOf(actor));
			mStage.addActor(actor);
		}
	}

	private void updateActorState(final int position) {
		if (position < SettingsHolder.mElementsCount) {
			final CustomActor actor = mActors.get(position);
			if (SettingsHolder.isRandomTextColor)
				actor.setColor(mRandom.nextFloat(), mRandom.nextFloat(), mRandom.nextFloat(), 1.0F);
			else {
				actor.setColor(SettingsHolder.mCustomTextColor);
			}
			actor.setX(mRandom.nextInt(mWidth));
			actor.setY(mRandom.nextInt(mHeight));
			actor.setValue(mTopParser.getRandomTopElement());
			
			actor.addAction(sequence(CustomActions.getRandomAction(), run(new Runnable() {

				@Override
				public void run() {
					updateActorState(position);
				}
			})));
		}
	}

	@Override
	public void resize(int width, int height) {
		mShaderProgram.begin();
		mShaderProgram.setUniformf("resolution", width, height);
		mShaderProgram.end();

	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		mStage.dispose();
		Settings.getInstance().dispose();
		
	}
	
	private interface CustomRenderer{
		void render();
	}
	
	private class AnimationRenderer implements CustomRenderer{

		@Override
		public void render() { 
			mSpriteBatch.begin();
			mShaderProgram.setUniformf("time", walk -= 0.002);
			if (walk <= -300)
				walk = 0;
			mSpriteBatch.draw(mTexture, 0, 0, mWidth, mHeight);
			mSpriteBatch.end();
		}
	}
	
	private class PlainRenderer implements CustomRenderer{

		@Override
		public void render() {
			Gdx.gl.glClearColor(SettingsHolder.mCustomBackgroundColor.r, SettingsHolder.mCustomBackgroundColor.g, SettingsHolder.mCustomBackgroundColor.b, 1.0F);
		}}
	

	@Override
	public void onSettingsChanged() {
		initActorList();
		mBackgroundRenderer = SettingsHolder.mIsAnimatedBackground ? new AnimationRenderer() : new PlainRenderer();
	}

	@Override
	public void fontChanged(int value) {
		font.setScale(value + 1);
	}
	
}
