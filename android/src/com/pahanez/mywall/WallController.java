package com.pahanez.mywall;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import android.app.Application;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.pahanez.mywall.cpu.ProcessItem;
import com.pahanez.mywall.cpu.WPCPU;
import com.pahanez.mywall.gdx.CustomFonts;
import com.pahanez.mywall.settings.Settings;
import com.pahanez.mywall.utils.WLog;

public class WallController { //на удаление
	private static final String TAG = WallController.class.getSimpleName();

	private static WallController mInstance;
	private final static Random mRandom = new Random();

	public static synchronized WallController getInstance() {
		return (mInstance == null) ? mInstance = new WallController() : mInstance;
	}

	private static class ControllerHandler extends Handler {
		private WeakReference<WallController> mOutInstance;

		public ControllerHandler(WallController instance, Looper looper) {
			super(looper);
			mOutInstance = new WeakReference<WallController>(instance);
		}

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case WConstants.CPU_LOOP:

				removeMessages(WConstants.CPU_LOOP);
				mOutInstance.get().updateCpuData();
				//sendEmptyMessageDelayed(WConstants.CPU_LOOP, WConstants.CPU_DELAY);
				break;
			case WConstants.GENERATE_FONTS:
				mOutInstance.get()._generateFonts((OnFontsLoadedCallback) msg.obj);
				break;

			}

		}

	}

	private WallController() {
		mHandlerThread = new HandlerThread("wallctrl_thread");
		mHandlerThread.start();
		mHandler = new ControllerHandler(this, mHandlerThread.getLooper());
		initApp();
	}

	public void destroy() {
		mHandler.getLooper().quit();
		mHandler = null;
		mHandlerThread = null;
		mInstance = null;
	}

	private final static ArrayList<String> mCpuUsageList = new ArrayList<String>();
	
	private static String [] data;
	private Random mControllerRandom = new Random();
	private Typeface mCurrentTypeface; 

	private HandlerThread mHandlerThread;
	private Handler mHandler;
	private CustomFonts mFonts;
	private Process p;

	private void initApp() {
		// mFonts = new CustomFonts(); TODO
		//startCpuLoop();
		//testLoop();
	}

	// ~commands zone

	public BitmapFont getFont() {
		// TODO size
		return mFonts.getFont(2);
	}

	public Typeface getCustomTypeface(String typefaceName) {
		return Typeface.createFromAsset(WallApplication.getContext().getAssets(), "fonts/" + typefaceName);
	}

	public void startCpuLoop() {
		mHandler.obtainMessage(WConstants.CPU_LOOP).sendToTarget();
	}
	
	public void testLoop() {
		mHandler.obtainMessage(42).sendToTarget();
	}

	public void stopCpuLoop() {
		mHandler.removeMessages(WConstants.CPU_LOOP);
		WLog.i("p37td8", "" + mHandler.hasMessages(WConstants.CPU_LOOP));
	}

	public void updateCpuData() {
		fillData(WPCPU.getInstance().getRawTopData());
		
		data = new String[mCpuUsageList.size()];
			for (int i = 0; i < data.length; i++) {
				data[i] = new String(String.valueOf(mCpuUsageList.get(i)));
		}
	}

	private void fillData(List<String> list) {
		WLog.i(TAG, "fillData " + list.size());
		if (!mCpuUsageList.isEmpty())
			mCpuUsageList.clear();
		mCpuUsageList.addAll(list);
	}

	public String getCpuItem(int position) {
		if (data == null)
			return WallApplication.getContext().getString(R.string.cpu_loading);
		synchronized (data) {
			return data[position];
		}
			
	}
	String [] kimono;
	
	class Task implements Runnable {
		@Override
		public void run() {

			kimono = new String[mRandom.nextInt(40)];
			synchronized (kimono) {
				for (int i = 0; i < kimono.length; i++) {
					for (int j = 0; j < 10; j++) {
						kimono[i] = new StringBuffer().append((char) (mRandom.nextInt(70) + 30)).append((char) (mRandom.nextInt(70) + 30)).append((char) (mRandom.nextInt(70) + 30)).append((char) (mRandom.nextInt(70) + 30)).append((char) (mRandom.nextInt(70) + 30)).toString();
					}
				}
			}
		}

	}

	public void onOptionsChanged() {

	}

	public void generateFonts(OnFontsLoadedCallback callback) {
		Message msg = Message.obtain();
		msg.what = WConstants.GENERATE_FONTS;
		msg.obj = callback;
		mHandler.sendMessage(msg);
	}

	private void _generateFonts(OnFontsLoadedCallback callback) {
		HashMap<String, BitmapFont> mFontsMap = new HashMap<String, BitmapFont>();
		String fontName = "fonts/merchant.ttf";
		FileHandle fontFile;
		if (fontName == null)
			fontFile = Gdx.files.internal("fonts/merchant.ttf");
		else
			fontFile = Gdx.files.internal("fonts/" + fontName);
		float one_cm = Gdx.graphics.getPpcY();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
		long leg = System.currentTimeMillis();
		for (int i = 0; i < CustomFonts.mFontsId.length; i++) {
			WLog.e("p37td8", "res 150 : " + (System.currentTimeMillis() - leg));
			mFontsMap.put(CustomFonts.mFontsId[i], generator.generateFont(((int) (one_cm * CustomFonts.mFontsFactor[i]))));
		}
		WLog.e("p37td8", "pre 150 : " + mFontsMap.size());
		generator.dispose();
		callback.fontsLoaded(mFontsMap);
	}

	public interface OnFontsLoadedCallback {
		public void fontsLoaded(HashMap<String, BitmapFont> map);
	}
}
