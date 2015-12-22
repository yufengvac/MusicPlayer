package com.vac.musicplayer.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class MyPauseButton extends View {

	private int defaultWidth =7;
	private int defaultHeight = 38;
	private int dividerWidth = 17;
	private Paint mPaint;
	private int color =Color.rgb(0,255,0);
	private Rect rect;
	private Rect rect_right;
	
	public MyPauseButton(Context context) {
		super(context);
		initPaint();
	}
	public MyPauseButton(Context context,AttributeSet set) {
		super(context,set);
		initPaint();
	}
	
	public void setWidthAndHeightAndDivier(int width,int height,int dividerWidth){
		defaultWidth = width;
		defaultHeight = height;
		this.dividerWidth = dividerWidth;
		invalidate();
	}
	public void setColor(int color){
		this.color = color;
		mPaint.setColor(this.color);
		invalidate();
	}
	private void initPaint() {
		mPaint = new Paint();
		mPaint.setColor(color);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL);
		rect = new Rect(0,0,defaultWidth,defaultHeight);
		rect_right = new Rect(defaultWidth+dividerWidth,0
				,defaultWidth+dividerWidth+defaultWidth,defaultHeight);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawRect(rect, mPaint);
		canvas.drawRect(rect_right, mPaint);
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
			width = getPaddingLeft()+getPaddingRight()+defaultWidth*2+dividerWidth;
		}
		
		if (heightMode==MeasureSpec.EXACTLY) {
			height = heightSize;
		}else{
			height = getPaddingTop()+getPaddingBottom()+defaultHeight;
		}
		setMeasuredDimension(width, height);
	}

}
