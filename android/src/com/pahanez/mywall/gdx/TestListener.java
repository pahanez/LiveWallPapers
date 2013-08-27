package com.pahanez.mywall.gdx;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.android.AndroidWallpaperListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pahanez.mywall.utils.WLog;

/*public class TestListener implements ApplicationListener,AndroidWallpaperListener{
 private BitmapFont mCustomFont;
 private SpriteBatch mBatch;
 private Random mRandom = new Random();
 @Override
 public void offsetChange(float xOffset, float yOffset, float xOffsetStep,
 float yOffsetStep, int xPixelOffset, int yPixelOffset) {
 // TODO Auto-generated method stub

 }

 @Override
 public void previewStateChange(boolean isPreview) {
 // TODO Auto-generated method stub

 }

 @Override
 public void create() {
 mBatch = new SpriteBatch();
 FileHandle fontFile = Gdx.files.internal("fonts/merchant.ttf");
 FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
 mCustomFont = generator.generateFont(24);
 generator.dispose();
 }

 @Override
 public void resize(int width, int height) {
 // TODO Auto-generated method stub

 }

 @Override
 public void render() {
 mBatch.begin();
 mCustomFont.draw(mBatch, "Alaska", mRandom.nextInt(480), mRandom.nextInt(800));
 mBatch.end();
 }

 @Override
 public void pause() {

 }

 @Override
 public void resume() {
 // TODO Auto-generated method stub

 }

 @Override
 public void dispose() {
 // TODO Auto-generated method stub

 }

 }*/

public class TestListener implements ApplicationListener, InputProcessor,AndroidWallpaperListener { //на удаление
	SpriteBatch mBatch;
	ShaderProgram shader;
	Texture mTexture;
	float time;
	Stage mStage;
	private int mWidth;
	private int mHeight;
	private ArrayList<Actor> mActors = new ArrayList<Actor>();
	private BitmapFont mCustomFont;
	private Random mRandom = new Random();
	
	final String VERT =  
			"attribute vec4 "+ShaderProgram.POSITION_ATTRIBUTE+";\n" + "\n" +
			"uniform mat4 u_projTrans;" + "\n" + 
			"void main() {\n" + "\n" +  
			"	gl_Position =  u_projTrans * " + "\n" + ShaderProgram.POSITION_ATTRIBUTE + ";\n" +
			"}";
	
//	final String FRAG = 
//			"void main() {\n" + "\n" +  
//			"	gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);\n" + "\n" + 
//			"}";
//	final String FRAG = "#ifdef GL_ES" + "\n" +
//			"precision lowp float;" + "\n" +
//			"#endif" + "\n" +
//			"uniform float time;" + "\n" +
//			"uniform vec2 mouse;" + "\n" +
//			"uniform vec2 resolution;" + "\n" +
//			"" + "\n" +
//			"float rand(vec2 co){" + "\n" +
//			"	return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);" + "\n" +
//			"}" + "\n" +
//			"" + "\n" +
//			"" + "\n" +
//			"void main( void ) {" + "\n" +
//			"	" + "\n" +
//			"	vec3 col = vec3(0.0,0.0,0.0);" + "\n" +
//			"	" + "\n" +
//			"	col.g += clamp(ceil(mod(gl_FragCoord.x, 10.0)) - 9.0, 0.0, 1.0) * 0.05;" + "\n" +
//			"	col.g += clamp(ceil(mod(gl_FragCoord.y, 10.0)) - 9.0, 0.0, 1.0) * 0.05;" + "\n" +
//			"	col.g = clamp(col.g, 0.0, 0.05);" + "\n" +
//			"	" + "\n" +
//			"	col.g += clamp(ceil(mod(gl_FragCoord.x, 50.0)) - 49.0, 0.0, 1.0) * 0.25;" + "\n" +
//			"	col.g += clamp(ceil(mod(gl_FragCoord.y, 50.0)) - 49.0, 0.0, 1.0) * 0.25;" + "\n" +
//			"	col.g = clamp(col.g, 0.0, 0.25);" + "\n" +
//			"	" + "\n" +
//			"	col.g += clamp(ceil(mod(gl_FragCoord.x, 100.0)) - 99.0, 0.0, 1.0);" + "\n" +
//			"	col.g += clamp(ceil(mod(gl_FragCoord.y, 100.0)) - 99.0, 0.0, 1.0);" + "\n" +
//			"	col.g = clamp(col.g, 0.0, 1.0);" + "\n" +
//			"	" + "\n" + 
//			"	vec2 mousePos = resolution.xy * mouse;" + "\n" +
//			"	col.g *= 1.0 - clamp(distance(gl_FragCoord.xy, gl_FragCoord.xy)/180.0, 0.0, 1.0);" + "\n" +
//			"	" + "\n" +
//			"	" + "\n" +
//			"	" + "\n" +
//			"	if ( rand(vec2(mod(gl_FragCoord.x, 10.0)*time, mod(gl_FragCoord.y, 50.0)*time)) <=0.5)" + "\n" +
//			"		col.g += clamp(distance(mousePos, gl_FragCoord.xy)/1715.0, 0.0, 0.1);" + "\n" +
//			"		col.g = clamp(col.g, 0.0, 1.0);" + "\n" +
//			"" + "\n" +
//			"	" + "\n" +
//			"	" + "\n" +
//			"	" + "\n" +
//			"	gl_FragColor = vec4(col,1.0);" + "\n" +
//			"}";
	
	final String FRAG = "#ifdef GL_ES" +"\n" +
			"precision mediump float;" +"\n" +
			"#endif" +"\n" +
			"" +"\n" +
			"uniform float time;" +"\n" +
			"uniform vec2 mouse;" +"\n" +
			"uniform vec2 resolution;" +"\n" +
			"varying vec2 surfacePosition;" +"\n" +
			"" +"\n" +
			"" +"\n" +
			"#define MAX_ITER 32" +"\n" +
			"void main( void ) {" +"\n" +
			"" +"\n" +
			"	vec2 p = surfacePosition*4.0;" +"\n" +
			"	vec2 i = p;" +"\n" +
			"	float c = 0.0;" +"\n" +
			"	float inten = 0.8;" +"\n" +
			"	" +"\n" +
			"	for (int n = 0; n < MAX_ITER; n++) {" +"\n" +
			"" +"\n" +
			"" +"\n" +
			"" +"\n" +
			"		float t = time * (1.0 - (1.0 / float(n+1)));" +"\n" +
			"		c = length(vec2(" +"\n" +
			"			6.3 + (sin(i.x)*inten)," +"\n" +
			"			4.0 + (cos(i.y)*inten)" +"\n" +
			"			)" +"\n" +
			"		);" +"\n" +
			"		i = p + vec2(" +"\n" +
			"			cos(t - i.x) + sin(t + i.y)," +"\n" +
			"" +"\n" +
			"			sin(t - i.y) + cos(t + i.x)" +"\n" +
			"		);" +"\n" +
			"	}" +"\n" +
			"	gl_FragColor = vec4(vec3(cos(c))*vec3(0.95, 0.97, 1.8), 1.0);" +"\n" +
			"}";
	
	@Override
	public void create() {
		String vertexShader = Gdx.files.internal("data/lesson1.vert").readString();
		String fragmentShader= Gdx.files.internal("data/lesson1.frag").readString();
		mWidth = Gdx.graphics.getWidth();
		mHeight = Gdx.graphics.getHeight();
		mStage =  new Stage(mWidth, mHeight, true);
		
		initActorList();
		
		for (Actor actor : mActors) {
			updateActorState(mActors.indexOf(actor));
			mStage.addActor(actor);
		}
		
			ShaderProgram.pedantic = false;
			shader = new ShaderProgram(VERT, fragmentShader);
			if (shader.isCompiled() == false) {
				Gdx.app.log("ShaderTest", shader.getLog());
				Gdx.app.exit();
			}
			
			mBatch = new SpriteBatch(1000,shader);
			mBatch.setShader(shader);
			mTexture = new Texture(new Pixmap(800, 480, Format.RGBA8888));
			Gdx.input.setInputProcessor(this);
//			mTexture = new Texture(Gdx.files.internal("data/grass.png"), false);
			
			FileHandle fontFile = Gdx.files.internal("fonts/merchant.ttf");
			 FreeTypeFontGenerator generator = new
			 FreeTypeFontGenerator(fontFile);
			 mCustomFont = generator.generateFont(24);
			 generator.dispose();
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
	public void render() {
		WLog.e("render");
		Gdx.gl20.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
		// Gdx.gl20.glEnable(GL10.GL_BLEND);
		// Gdx.gl20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		mBatch.begin();
		shader.setUniformf("time", time-= 0.01 );
			if(time <= -3000)
				time = 0;
		mBatch.draw(mTexture, 0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		mBatch.end();
		
		mStage.act();
		mStage.draw();
	}

	@Override
	public void dispose() {
		shader.dispose();
	}

	@Override
	public void resize(int width, int height) {
		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		shader.begin();
		shader.setUniformf("resolution", width, height);
		shader.end();
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}


	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		WLog.e("" + screenX + " , " + screenY);
		shader.begin();
		shader.setUniformf("mouse_touch",new Vector2((float)screenX, (float)screenY));
		shader.end();
		return false;
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		shader.begin();
		shader.setUniformf("mouse_touch",new Vector2(0F, 0F));
		shader.end();
		return false;
	}


	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated 
		shader.begin();
		shader.setUniformf("mouse_touch",new Vector2((float)screenX, (float)screenY));
		shader.end();
		return false;
	}


	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
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

	int mLegacyX;
	@Override
	public void offsetChange(float xOffset, float yOffset, float xOffsetStep,
			float yOffsetStep, int xPixelOffset, int yPixelOffset) {
		if (mStage != null)
			for (Actor actor : mStage.getActors())
				actor.setPosition(actor.getX() + (mLegacyX - xPixelOffset),
						actor.getY());
		mLegacyX = xPixelOffset;		
	}
	@Override
	public void previewStateChange(boolean isPreview) {
		// TODO Auto-generated method stub
		
	}
	
	// @Override
	// public boolean needsGL20 () {
	// return true;
	// }
}
