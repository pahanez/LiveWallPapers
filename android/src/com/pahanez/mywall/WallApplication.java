package com.pahanez.mywall;

import java.util.Random;

import android.app.Application;
import android.content.Context;

import com.pahanez.mywall.utils.WLog;

public class WallApplication extends Application {
	private static final String TAG = WallApplication.class.getSimpleName();
	private static Random mRandom =  new Random();
	private static Context mContext;
	
	@Override
	public void onCreate() {
		super.onCreate();
		WLog.i(TAG, "onApplicationStarted()");
		mContext = getApplicationContext();
	}
	
	public static Random getRandom(){
		return mRandom;
	}
	
	public static Context getContext(){
		return mContext;
	}
}
