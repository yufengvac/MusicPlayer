package com.vac.musicplayer.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MyProgressbar extends View {

	private Paint mBgPaint,mProgressPaint;
	private int defaultWidth;
	private int progressColor = Color.rgb(0, 0, 255);
	private int bgColor = Color.parseColor("#b4b5b6");
	
	private int max=300;
	private int curProgress = 0;
	public MyProgressbar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	public void setBgColor(int color){
		this.bgColor = color;
		mBgPaint.setColor(bgColor);
		invalidate();
	}
	public void setProgressColor(int color){
		this.progressColor = color;
		mProgressPaint.setColor(progressColor);
		invalidate();
	}
	
	public void setWidth(int width){
		this.defaultWidth = width;
		invalidate();
	}
	
	private void init(Context context){
		defaultWidth = context.getResources().getDisplayMetrics().widthPixels;
		mBgPaint = new Paint();
		mBgPaint.setColor(bgColor);
		mBgPaint.setAntiAlias(true);
		mBgPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mBgPaint.setStyle(Paint.Style.FILL);
		mBgPaint.setStrokeWidth(6f);
		
		mProgressPaint = new Paint();
		mProgressPaint.setColor(progressColor);
		mProgressPaint.setAntiAlias(true);
		mProgressPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mProgressPaint.setStyle(Paint.Style.FILL);
		mProgressPaint.setStrokeWidth(6f);
	}
	public void setProgress(int progress){
		curProgress = progress;
		invalidate();
	}
	public int getMax(){
		return max;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawLine(0, 0, defaultWidth, 0, mBgPaint);
		if (curProgress>0&&curProgress<=max) {
			int stopX = (int) (curProgress*1.0/max *defaultWidth);
			canvas.drawLine(0, 0, stopX, 0, mProgressPaint);
		}
		super.onDraw(canvas);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int width;
		int height;
		if (widthMode==MeasureSpec.EXACTLY) {
			width = widthSize;
		}else{
			width = defaultWidth;
		}
		
		if (heightMode==MeasureSpec.EXACTLY) {
			height = heightSize;
		}else{
			height = getPaddingTop()+getPaddingBottom()+3;
		}
		setMeasuredDimension(width, height);
	}
}
