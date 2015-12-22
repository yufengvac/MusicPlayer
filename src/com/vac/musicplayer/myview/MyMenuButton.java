package com.vac.musicplayer.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MyMenuButton extends View {

	private int defaultWidth=38;
	private int color = Color.rgb(0, 255, 0);
	private Paint mPaint,mCirclePaint;
	private int dividerHeight = 15;
	private int defaultRadius = 5;
	public MyMenuButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public void setColor(int color){
		this.color =color;
		mPaint.setColor(this.color);
		mCirclePaint.setColor(this.color);
		invalidate();
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setColor(color);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setStrokeWidth(3f);
		
		mCirclePaint = new Paint();
		mCirclePaint.setColor(color);
		mCirclePaint.setAntiAlias(true);
		mCirclePaint.setStyle(Paint.Style.STROKE);
		mCirclePaint.setStrokeWidth(2f);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawLine(0, 2, defaultWidth, 2, mPaint);
		canvas.drawLine(0, dividerHeight+2, defaultWidth-10, dividerHeight+2, mPaint);
		canvas.drawLine(0, dividerHeight*2+2, defaultWidth-15, dividerHeight*2+2, mPaint);
		
		canvas.drawCircle(defaultWidth-5, dividerHeight*2+2, defaultRadius, mCirclePaint);
		canvas.drawLine(defaultWidth, 7,defaultWidth, dividerHeight*2+2 -defaultRadius/2, mPaint);
		canvas.drawLine(defaultWidth, 7,defaultWidth+10, 10+dividerHeight,mPaint);
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
			width = getPaddingLeft()+getPaddingRight()+defaultWidth+10;
		}
		
		if (heightMode==MeasureSpec.EXACTLY) {
			height = heightSize;
		}else{
			height = getPaddingTop()+getPaddingBottom()+dividerHeight*2+8;
		}
		setMeasuredDimension(width, height);
	}
}
