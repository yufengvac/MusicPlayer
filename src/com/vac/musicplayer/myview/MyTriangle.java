package com.vac.musicplayer.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.vac.musicplayer.R;

public class MyTriangle extends View {

	private Path path;
	private Paint paint;
	private int length = 20;
	private int color = Color.rgb(230,26,195);

	public MyTriangle(Context context) {
		super(context);
		paint = new Paint();
		path = new Path();
		paint.setColor(color);
		paint.setAntiAlias(true);
	}

	public MyTriangle(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		path = new Path();
		paint.setColor(color);
		paint.setAntiAlias(true);
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.myTriangle);
		length = a.getDimensionPixelSize(R.styleable.myTriangle_length, 20);
		color = a.getColor(R.styleable.myTriangle_color,color);
		a.recycle();
	}

	public MyTriangle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.myTriangle, defStyle, 0);
		length = a.getDimensionPixelSize(R.styleable.myTriangle_length, 20);
		color = a.getColor(R.styleable.myTriangle_color,color);
		a.recycle();
		paint = new Paint();
		path = new Path();
		paint.setColor(color);
		paint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		path.moveTo(0, 0);
		path.lineTo((int) (length * 1.732), length);
		path.lineTo(0, length * 2);
		path.close();
		canvas.drawPath(path, paint);
	}

	public void setColor(int color) {
		this.color = color;
		paint.setColor(this.color);
		invalidate();
	}
	public void setLength(int length){
		this.length = length;
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int withMode = MeasureSpec.getMode(widthMeasureSpec);
		int withSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int width;
		int height;
		if (withMode == MeasureSpec.EXACTLY) {
			width = withSize;
		} else {
			width = (int) (length * 1.732) + getPaddingLeft()
					+ getPaddingRight();
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = length * 2 + getPaddingTop() + getPaddingBottom();
		}
		setMeasuredDimension(width, height);
	}

}
