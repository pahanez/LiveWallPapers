package com.pahanez.mywall.gdx;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.android.AndroidWallpaperListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pahanez.mywall.WallController;
import com.pahanez.mywall.WallController.OnFontsLoadedCallback;
import com.pahanez.mywall.settings.Settings;
import com.pahanez.mywall.utils.WLog;

public class WallPaper extends Game implements AndroidWallpaperListener { //на удаление
	private static final String TAG = WallPaper.class.getSimpleName();

//	private Settings mSettings = Settings.getInstance();
	private FPSLogger mFpsLogger;
	private Stage mStage;
	public final static int DEFAULT_FRAME_INTERVAL = 30;
	// private BitmapFont mCustomFont;
	private Random mRandom = new Random();
	private int mWidth;
	private int mHeight;
	private ArrayList<Actor> mActors = new ArrayList<Actor>();
	
	private ShaderProgram mShaderProgram;
	private SpriteBatch mSpriteBatch;
	private Texture mTexture;
	private float walk = 0;
	private BitmapFont mCustomFont;
	
	@Override
	public void create() {
		Gdx.app.log(TAG, "Creating game");
		setScreen(getScreenOne());
		// TODO
		 FileHandle fontFile = Gdx.files.internal("fonts/merchant.ttf");
		 FreeTypeFontGenerator generator = new
		 FreeTypeFontGenerator(fontFile);
		 mCustomFont = generator.generateFont(24);
		 generator.dispose();

	}

	@Override
	public void render() {
		super.render();
	}

	private Screen getScreenOne() {
		return new ScreenOne();
	}

	private class ScreenOne implements Screen {

		public ScreenOne() {
			mWidth = Gdx.graphics.getWidth();
			mHeight = Gdx.graphics.getHeight();

			Gdx.app.log(TAG, "ScreenOne()_constructor");
			mStage = new Stage(mWidth, mHeight, true);
			initActorList();
			
			for (Actor actor : mActors) {
				updateActorState(mActors.indexOf(actor));
				mStage.addActor(actor);
			}
			
			ShaderProgram.pedantic = false;
			mShaderProgram = new ShaderProgram(VERT, FRAG);
			if (mShaderProgram.isCompiled() == false) {
				Gdx.app.log("wall_shader", mShaderProgram.getLog());
				Gdx.app.exit();
			}
			mSpriteBatch = new SpriteBatch();
			mSpriteBatch.setShader(mShaderProgram);
			mTexture = new Texture(new Pixmap(800, 480, Format.RGBA8888));
			
		}

		private void initActorList() {
			for (int i = 0; i < 50; i++) {
				mActors.add(new CustomActorTime());
			}
		}

		private void updateActorState(final int position) {
			final Actor actor = mActors.get(position);
			if (/*mSettings.isRandomColor()*/true)
				actor.setColor(mRandom.nextFloat(), mRandom.nextFloat(),
						mRandom.nextFloat(), 1.0F);
			else {
				Color c = new Color(/*mSettings.getCustomTextColor()*/);
				actor.setColor(c.g, c.b, c.a, c.r);
			}
			actor.setX(mRandom.nextInt(mWidth));
			actor.setY(mRandom.nextInt(mHeight));
			actor.addAction(sequence(CustomActions.getRandomAction(),
					run(new Runnable() {

						@Override
						public void run() {
							updateActorState(position);
						}
					})));
		}
		
		@Override
		public void render(float delta) {
			
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			/*
			int deltaTime = (int) (Gdx.graphics.getDeltaTime() * 1000);
			if (deltaTime > DEFAULT_FRAME_INTERVAL)
				deltaTime = DEFAULT_FRAME_INTERVAL;
			int timeToSleep = DEFAULT_FRAME_INTERVAL - deltaTime;
			if (timeToSleep > 0) {
				try {
					Thread.sleep(timeToSleep);
				} catch (InterruptedException e) {

				}
			}*/
			

			
			drawBackground();

			
			mStage.act(delta);
			mStage.draw();
			
		}
		
		private void drawBackground(){
			mSpriteBatch.begin();
			mShaderProgram.setUniformf("time", walk-= 0.002 );
			if(walk <= -300)
				walk = 0;
			mSpriteBatch.draw(mTexture, 0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
			mSpriteBatch.end();
		}

		@Override
		public void resize(int width, int height) {
			Gdx.app.log(TAG, "resize");
//			Color c = new Color(mSettings.getCustomBackgroundColor());
//			Gdx.gl.glClearColor(c.g, c.b, c.a, c.r); // spike
			
			mShaderProgram.begin();
			mShaderProgram.setUniformf("resolution", width, height);
			mShaderProgram.end();
			
			

		}

		@Override
		public void show() {
			Gdx.app.log(TAG, "show");
		}

		@Override
		public void hide() {
			Gdx.app.log(TAG, "hide");
		}

		@Override
		public void pause() {
			Gdx.app.log(TAG, "pause");
		}

		@Override
		public void resume() {
			Gdx.app.log(TAG, "resume");
		}

		@Override
		public void dispose() {
			mStage.dispose();
		}

	}

	private class CustomActorTime extends Actor {
		@Override
		public void draw(SpriteBatch batch, float parentAlpha) {
			super.draw(batch, parentAlpha);
			if (mCustomFont != null) {
				mCustomFont.setColor(getColor());
				mCustomFont.draw(batch, "Test", getX(), getY());
			}
		}

	}

	private int mLegacyX;

	@Override
	public void offsetChange(float xOffset, float yOffset, float xOffsetStep,
			float yOffsetStep, int xPixelOffset, int yPixelOffset) {
//		if (mStage != null)
//			for (Actor actor : mStage.getActors())
//				actor.setPosition(actor.getX() + (mLegacyX - xPixelOffset),
//						actor.getY());
		mLegacyX = xPixelOffset;
		// TODO Auto-generated method stub

	}

	@Override
	public void previewStateChange(boolean isPreview) {

	}

	final String VERT = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE
			+ ";\n" + "\n" + "uniform mat4 u_projTrans;" + "\n"
			+ "void main() {\n" + "\n" + "	gl_Position =  u_projTrans * "
			+ "\n" + ShaderProgram.POSITION_ATTRIBUTE + ";\n" + "}";

	final String FRAG = "#ifdef GL_ES" + " \n" + "precision highp float;"
			+ " \n" + "#endif" + " \n" +
			"uniform float time;" + " \n" + 
			"uniform vec2 resolution;" + " \n" +
			"void main(void){" + " \n" +
			"	vec2 position = gl_FragCoord.xy / max(resolution.x,resolution.y) ;" + " \n" +
			"	position.y-=time;" + " \n" +
			"	float g = cos(position.y * 75.0)*0.4 ;" + " \n" +
			"	vec3 col=vec3(0,g,0);" + " \n" + "	gl_FragColor=vec4(col, 1.0);"
			+ " \n" +
			"}";
}