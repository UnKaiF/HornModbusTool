package com.basisdas.hornModbusTool.views.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.basisdas.hornModbusTool.R;
import com.basisdas.hornModbusTool.misc.InflateState;

public class ExpandButton extends CircleButton
	{

	public interface ClickListener
		{
			void onStateChanged(InflateState inflateState);
		}

	private InflateState mState;
	private ClickListener mClickListener;

	public ExpandButton(Context context, AttributeSet attrs)
		{
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.ExpandButton,
				0, 0);

		InflateState state;
		try
			{
			state = InflateState.values()[a.getInteger(R.styleable.ExpandButton_inflateState, 0)];
			}
		finally { a.recycle(); }
		setState(state);
		}



	@Override
	public boolean performClick()
		{
		super.performClick();
		int next = ((mState.ordinal() + 1) % InflateState.values().length);
		setState(InflateState.values()[next]);
		performEntityStateButtonClick();
		return true;
		}


	private void performEntityStateButtonClick()
		{
		if (mClickListener == null) return;
		mClickListener.onStateChanged(mState);
		}

	private void createDrawableState()
		{
		switch (mState)
			{
			case DEFLATED:
				setImageResource(R.drawable.circle_more_48);
				break;
			case INFLATED:
				setImageResource(R.drawable.circle_less_48);
				break;
			}
		}



	public InflateState getState()
		{
		return mState;
		}

	public void setState(InflateState state)
		{
		if (state == null) return;
		this.mState = state;
		createDrawableState();
		}

	public ClickListener getClickListener()
		{
		return mClickListener;
		}

	public void removeClickListener()
		{ this.mClickListener = null; }

	public void setClickListener(ClickListener clickListener)
		{
		this.mClickListener = clickListener;
		}

	@BindingAdapter("inflateState")
	public static void setState(ExpandButton expandButton, InflateState newState)
		{
		if (expandButton.mState != newState)
			{
			expandButton.setState(newState);
			}
		}

	@InverseBindingAdapter(attribute = "inflateState")
	public static InflateState getState(ExpandButton expandButton)
		{
		return expandButton.getState();
		}

	@BindingAdapter("inflateStateAttrChanged")
	public static void setListeners(ExpandButton expandButton, final InverseBindingListener attrChange)
		{
		expandButton.setClickListener( new ExpandButton.ClickListener()
			{
				@Override
				public void onStateChanged(InflateState inflateState)
					{
					attrChange.onChange();
					}
			});
		}


	}