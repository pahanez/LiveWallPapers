package com.pahanez.mywall.utils;

import android.util.Log;

public class WLog {
	
	public static final int LOG_LEVEL = 6;
	
	public static final void e(String tag, String msg){
		if(LOG_LEVEL >= Log.ERROR){
			Log.e(tag, msg);
		}
	}
	public static final void d(String tag, String msg){
		if(LOG_LEVEL >= Log.DEBUG){
			Log.d(tag, msg);
		}
	}
	
	public static final void v(String tag, String msg){
		if(LOG_LEVEL >= Log.VERBOSE){
			Log.v(tag, msg);
		}
	}
	
	public static final void i(String tag, String msg){
		if(LOG_LEVEL >= Log.INFO){
			Log.i(tag, msg);
		}
	}
	
	public static final void w(String tag, String msg){
		if(LOG_LEVEL >= Log.WARN){
			Log.w(tag, msg);
		}
	}

}
