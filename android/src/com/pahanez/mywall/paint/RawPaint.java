package com.pahanez.mywall.paint;

import android.graphics.Paint;

public abstract class RawPaint {
	protected Paint mPaint;
	
	public Paint getPaint(){
		return initPaint().setShaders().setTextSize().setColor().build();
	}
	
	protected abstract RawPaint initPaint();
	protected abstract RawPaint setShaders();
	protected abstract RawPaint setTextSize();
	protected abstract RawPaint setColor();
	
	private Paint build(){
		return mPaint;
	}
	
	
	
	
}
