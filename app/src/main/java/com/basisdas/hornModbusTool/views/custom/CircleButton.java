package com.basisdas.hornModbusTool.views.custom;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageButton;

public  class CircleButton extends AppCompatImageButton
	{
	public CircleButton(Context context, AttributeSet attrs)
		{
		super(context, attrs);
		this.setPadding(0,0,0,0);
		setScaleType(ScaleType.FIT_XY);
		addOnLayoutChangeListener(new OnLayoutChangeListener() {
			@Override
			public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
				{
				GradientDrawable drawable =  new GradientDrawable();
				drawable.setCornerRadius((right-left) / 2f);
				setBackground(drawable);
				}
		});
		}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
		{
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int size;

		if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY ^ MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY)
			{
			if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY)
				size = width;
			else
				size = height;
			}
		else
			size = Math.min(width, height);
		setMeasuredDimension(size, size);
		}


	}
