package com.pahanez.mywall.paint;

import android.graphics.Color;
import android.graphics.Paint;

import com.pahanez.mywall.WallApplication;
import com.pahanez.mywall.utils.Settings;

public class MainPaint extends RawPaint{

	@Override
	public RawPaint initPaint() {
		mPaint.setAntiAlias(true); //wtf
		mPaint.setStrokeWidth(2); 
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStyle(Paint.Style.STROKE);
		return this;
	}

	@Override
	public RawPaint setShaders() {
		//TODO shaders
		return this;
	}

	@Override
	public RawPaint setTextSize() {
		if(Settings.getInstance().isRandomTextSize())
			mPaint.setTextSize(WallApplication.getRandom().nextFloat()*90);
		else
			mPaint.setTextSize(Settings.getInstance().getCustomTextSize());
		return this;
	}

	@Override
	public RawPaint setColor() {
		if(Settings.getInstance().isRandomColor())
			mPaint.setColor(Color.argb(255, WallApplication.getRandom().nextInt(255), WallApplication.getRandom().nextInt(255), WallApplication.getRandom().nextInt(255)));		
		else
			mPaint.setColor(Settings.getInstance().getCustomColor());
		
		return this;
	}

}