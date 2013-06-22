package com.pahanez.mywall;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService;
import com.badlogic.gdx.graphics.GL10;


/*
 * This animated wallpaper draws a rotating wireframe cube.
 */
/*public class CustomWallPaperService extends WallpaperService {
	private static final String TAG = CustomWallPaperService.class.getSimpleName();

	private Time mTime = new Time(System.currentTimeMillis());
	private final Handler mHandler = new Handler();
	private WallController mController;
	private Settings mSettings;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public Engine onCreateEngine() {
		return new CubeEngine();
	}

	class CubeEngine extends Engine {

		private float mOffset;
		private float mTouchX = -1;
		private float mTouchY = -1;
		private long mStartTime;
		private float mCenterX;
		private float mCenterY;

		private Bitmap mBitmap;
		private Matrix mMatrix = new Matrix();
		private Canvas mCanvas;
		private Paint mTimePaint = new Paint();
		private Paint mOffsetPaint = new Paint();
		private MainPaint mPaint = new MainPaint();

		private final Runnable mDrawCube = new Runnable() {
			public void run() {
				drawFrame();
			}
		};
		private boolean mVisible;

		CubeEngine() {
			// Create a Paint to draw the lines for our cube
			mStartTime = SystemClock.elapsedRealtime();
		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);

			// By default we don't get touch events, so enable them.
			setTouchEventsEnabled(true);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			mHandler.removeCallbacks(mDrawCube);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			mVisible = visible;
			if (visible) {
				drawFrame();
			} else {
				mHandler.removeCallbacks(mDrawCube);
			}
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);
			WLog.i(TAG, "onSurfaceChanged");
			// store the center of the surface, so we can draw the cube in the
			// right spot
			mCenterX = width / 2.0f;
			mCenterY = height / 2.0f;
			mBitmap = Bitmap.createBitmap(width * 2, height, Bitmap.Config.RGB_565);
			mCanvas = new Canvas(mBitmap);
			drawFrame();
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			initPaint();
			startApp();
			super.onSurfaceCreated(holder);
		}
		private void startApp(){
			mController = WallController.getInstance();
			mSettings = Settings.getInstance();
			mController.startPaintLoop();
			
			switch (mSettings.getDataTypeValue()) {
			case WConstants.CPU_TYPE:
				mController.startCpuLoop();
				break;
			case WConstants.TIME_TYPE:
				
				break;
			case WConstants.FILE_TYPE:
				mController.cacheExternalFileData();
				break;

			default:
				break;
			}
		}

		private void initPaint() {
			WLog.i(TAG, "initPaint");
			mOffsetPaint.setColor(Color.BLUE);
			mTime.setTime(System.currentTimeMillis());
			mTimeValue = mTime.toString();
			// mOffsetPaint.setMaskFilter(new BlurMaskFilter(60, Blur.INNER));
			// mOffsetPaint.setShader(new LinearGradient(8f, 80f, 30f, 20f,
			// Color.RED,Color.WHITE, Shader.TileMode.MIRROR));
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			WLog.v(TAG, "onSurfaceDestroyed");
			mVisible = false;
			mHandler.removeCallbacks(mDrawCube);
			// mController.destroy();
		}

		@Override
		public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels) {
			WLog.i(TAG, "onSurfaceChanged");
			WLog.e("yOffset + " + yOffset + " xOffset " + xOffset + " xStep " + xStep + " yStep " + yStep + " xPixels " + xPixels + " yPixels " + yPixels);
			mLegacyPixels = xPixels;
			// mScreenCount = ((int)(1 / xStep))+1;
			drawFrame();
			// drawOffset(xPixels);
			 
			
		}
		private String mTimeValue; //>in controller reduce GC calls
		private int mLegacyPixels;


		
		 * Store the position of the touch event so we can use it for drawing
		 * later
		 
		@Override
		public void onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				mTouchX = event.getX();
				mTouchY = event.getY();
			} else {
				mTouchX = -1;
				mTouchY = -1;
			}
			super.onTouchEvent(event);
		}
		int tempRadius = 1;
		
		 * Draw one frame of the animation. This method gets called repeatedly
		 * by posting a delayed Runnable. You can do any drawing you want in
		 * here. This example draws a wireframe cube.
		 
		void drawFrame() {
			final SurfaceHolder holder = getSurfaceHolder();

			Canvas c = null;
			try {
				c = holder.lockCanvas();
				if (c != null) {
					// draw something
					c.drawColor(Color.CYAN);
					c.drawCircle(200, 200, tempRadius++, mOffsetPaint);
					if(tempRadius > 500)
						tempRadius = 1; //legacy circle 6% CPU
					draw();
					c.drawBitmap(mBitmap, mLegacyPixels, 0, mTimePaint);
					// c.drawBitmap(mBitmap, mMatrix, mTimePaint);

				}
			} finally {
				if (c != null)
					holder.unlockCanvasAndPost(c);
			}

			// Reschedule the next redraw
			mHandler.removeCallbacks(mDrawCube);
			if (mVisible) {
				mHandler.postDelayed(mDrawCube, 1000 / mSettings.getFrameRate());
			}
		}

		private void draw() {
//			mTime.setTime(System.currentTimeMillis());
			mCanvas.drawColor(mSettings.getCustomBackgroundColor());
			switch (mSettings.getDataTypeValue()) {
			case WConstants.CPU_TYPE:
				for (int i = 0; i < mSettings.getElementsPerFrame(); i++)
					mCanvas.drawText(mController.getRandomCpuItem(), WallApplication.getRandom().nextInt(880) - 100, WallApplication.getRandom().nextInt(1000) - 100, mPaint.getPaint()mController.getPaint());
				break;
			case WConstants.TIME_TYPE:
				for (int i = 0; i < mSettings.getElementsPerFrame(); i++)
					mCanvas.drawText(mTimeValue, WallApplication.getRandom().nextInt(880) - 100, WallApplication.getRandom().nextInt(1000) - 100, mPaint.getPaint()mController.getPaint());
				break;
			case WConstants.FILE_TYPE:
				for (int i = 0; i < mSettings.getElementsPerFrame(); i++)
					mCanvas.drawText(mController.getRandomFileItem(), WallApplication.getRandom().nextInt(880) - 100, WallApplication.getRandom().nextInt(1000) - 100, mPaint.getPaint()mController.getPaint());
				break;

			default:
				break;
			}
			
		}

	}

}*/
public class CustomWallPaperService extends AndroidLiveWallpaperService{
	@Override
	public void onCreateApplication() {
		super.onCreateApplication();
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = false;
		initialize(new ApplicationListener() {
			
			@Override
			public void resume() { 
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void resize(int width, int height) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void render() {
				// TODO Auto-generated method stub
				Gdx.gl.glClearColor(1, 0, 1, 1);
				Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			}
			
			@Override
			public void pause() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void create() {
				// TODO Auto-generated method stub
				
			}
		}, cfg);
	}
	
	
	
}