package com.pahanez.mywall.cpu;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.pahanez.mywall.utils.WLog;

public class DataManager {
	private static final String TAG = DataManager.class.getSimpleName();
	
	private static DataManager mInstance;
	private static CopyOnWriteArrayList<ProcessItem> mCpuUsageList = new CopyOnWriteArrayList<ProcessItem>();
	
	private static final int TASKS_DELAY 	= 2000;
	
	private void fillData(List<ProcessItem> list){
		if(!mCpuUsageList.isEmpty())
			mCpuUsageList.clear();
		mCpuUsageList.addAll(list);
	}
	
	public static synchronized DataManager getInstance(){
		return (mInstance == null ? mInstance = new DataManager() : mInstance);
	}
	
	
	public synchronized void startGetData(){
		WLog.i(TAG,"startGetData()");
		if(!mHandlerThread.isAlive()){
			
			mHandlerThread.start();
			mLooper = mHandlerThread.getLooper();
			
			mHandler =  new Handler(mLooper){
				
				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case UPDATE_DATA:
						WLog.i(TAG,"UPDATE_DATA in mHandler");
						fillData(WPCPU.getInstance().getRawTopData());
						this.removeMessages(UPDATE_DATA);
						this.sendEmptyMessageDelayed(UPDATE_DATA, TASKS_DELAY);
						break;
					case STOP_UPDATING:
						WLog.i(TAG,"STOP_UPDATING in mHandler");
						this.removeMessages(UPDATE_DATA);
						break;
						
						
					}
					
				}
			};
			
			mHandler.sendEmptyMessageDelayed(UPDATE_DATA, TASKS_DELAY);
		}
	}
	
	public synchronized void stopGetData(){
		WLog.i(TAG,"stopGetData()");
		mHandler.sendEmptyMessage(STOP_UPDATING);
	}
	
	public synchronized List<ProcessItem> getCpuData(){
		return mCpuUsageList;
	}
	
	//-// Handler zone \/
	private HandlerThread mHandlerThread = new HandlerThread("Data_Manager_Handler");
	private static final int UPDATE_DATA 	= 10;
	private static final int STOP_UPDATING 	= 11;
	
	private Looper mLooper;
	private Handler mHandler;
	//-// Handler zone /\
	
}
