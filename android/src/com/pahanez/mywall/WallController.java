package com.pahanez.mywall;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import android.graphics.Paint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.pahanez.mywall.cpu.ProcessItem;
import com.pahanez.mywall.cpu.WPCPU;
import com.pahanez.mywall.paint.MainPaint;
import com.pahanez.mywall.utils.Settings;
import com.pahanez.mywall.utils.WLog;

public class WallController {
	private static final String TAG = WallController.class.getSimpleName();
	// singleton zone
	private static WallController mInstance;

	public static synchronized WallController getInstance() {
		return (mInstance == null) ? mInstance = new WallController() : mInstance;
	}

	private WallController() {
		mHandlerThread = new HandlerThread("wallctrl_thread");
		mHandlerThread.start();
		mHandler = new Handler(mHandlerThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case WConstants.PAINT_RANDOMIZER_LOOP:
					removeMessages(WConstants.PAINT_RANDOMIZER_LOOP);
					randomPaintArray();
					sendEmptyMessageDelayed(WConstants.PAINT_RANDOMIZER_LOOP, WConstants.RANDOMIZER_DELAY);
					break;
				case WConstants.CPU_LOOP:

					removeMessages(WConstants.CPU_LOOP);
					updateCpuData();
					sendEmptyMessageDelayed(WConstants.CPU_LOOP, WConstants.CPU_DELAY);
					break;

				default:
					break;
				}
			}
		};
	}

	public void destroy() {
		mHandler.getLooper().quit();
		mHandler = null;
		mHandlerThread = null;
		mInstance = null;
	}

	// ~singleton zone

	private CopyOnWriteArrayList<Paint> mPaintList = new CopyOnWriteArrayList<Paint>();
	private CopyOnWriteArrayList<ProcessItem> mCpuUsageList = new CopyOnWriteArrayList<ProcessItem>();
	private CopyOnWriteArrayList<String> mFileDataList = new CopyOnWriteArrayList<String>();
	private Random mControllerRandom = new Random();
	
	private MainPaint mPaint = new MainPaint();

	private void randomPaintArray() {
		WLog.i(TAG, "randomPaintArray()");
		if (mPaintList.size() == 100) {
			for (int i = 0; i < 100; i++){
				mPaintList.set(i, mPaint.getPaint());
			}
		} else {
			for (int i = 0; i < 100; i++) {
				mPaintList.add(mPaint.getPaint());
			}
		}
	} 

	private HandlerThread mHandlerThread;
	private Handler mHandler;

	// ~commands zone

	public void startPaintLoop() {
		mHandler.obtainMessage(WConstants.PAINT_RANDOMIZER_LOOP).sendToTarget();
	}

	public Paint getPaint() {
		if (mPaintList.size() != 100) { 
			return mPaint.getPaint();
		} else {
			return mPaintList.get(mControllerRandom.nextInt(mPaintList.size()));
		}
	}

	public void startCpuLoop() {
		mHandler.obtainMessage(WConstants.CPU_LOOP).sendToTarget();
	}
	
	public void stopCpuLoop(){
		mHandler.removeMessages(WConstants.CPU_LOOP);
		WLog.i("p37td8","" + mHandler.hasMessages(WConstants.CPU_LOOP));
	}

	public void updateCpuData() {
		fillData(WPCPU.getInstance().getRawTopData());
	}

	private void fillData(List<ProcessItem> list) {
		WLog.i(TAG,"fillData " + list.size());
		if (!mCpuUsageList.isEmpty())
			mCpuUsageList.clear();
		mCpuUsageList.addAll(list);
	}
	public String getRandomCpuItem(){
		if(mCpuUsageList.isEmpty())
			return WallApplication.getContext().getString(R.string.cpu_loading);
		return mCpuUsageList.get(mControllerRandom.nextInt(mCpuUsageList.size())).getFullData();
	}
	
	public void cacheExternalFileData(){
		mFileDataList.clear();
		String path = Settings.getInstance().getExternalFilePath();
		if(path != null){
			try {
			File externalFile = new File(path);
			BufferedReader br = new BufferedReader(new FileReader(externalFile));
			String line;
				while((line = br.readLine()) != null)
					mFileDataList.add(line);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public String getRandomFileItem(){
		if(mFileDataList.isEmpty())
			return WallApplication.getContext().getString(R.string.cpu_loading);
		return mFileDataList.get(mControllerRandom.nextInt(mFileDataList.size()));
	}
	

}
